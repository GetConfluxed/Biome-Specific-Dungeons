package net.darkhax.biomespecificdungeons.worldgen;

import java.util.Random;

import net.darkhax.biomespecificdungeons.data.DungeonData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenBiomeDungeon extends WorldGenerator {

    private final Dungeon data;
    private final IBlockState chest;
    private final IBlockState spawner;
    
    public WorldGenBiomeDungeon (Dungeon data) {

        this.data = data;
        this.chest = Blocks.CHEST.getDefaultState();
        this.spawner = Blocks.MOB_SPAWNER.getDefaultState();
    }

    @Override
    public boolean generate (World world, Random rand, BlockPos position) {
        
        final int randomXOffset = rand.nextInt(2) + 2;
        final int xMin = -randomXOffset - 1;
        final int xMax = randomXOffset + 1;
        final int randomZOffset = rand.nextInt(2) + 2;
        final int zMin = -randomZOffset - 1;
        final int zMax = randomZOffset + 1;

        if (this.hasValidPosition(world, position, xMin, zMin, xMax, zMax)) {

            this.generateDungeonBlocks(world, rand, position, xMin, zMin, xMax, zMax, false);
            this.generateDungeonTileEntities(world, rand, position, randomXOffset, randomZOffset);
            return true;
        }

        return false;
    }

    public void generateDebug (World world, Random rand, BlockPos position) {

        final int randomXOffset = rand.nextInt(2) + 2;
        final int xMin = -randomXOffset - 1;
        final int xMax = randomXOffset + 1;
        final int randomZOffset = rand.nextInt(2) + 2;
        final int zMin = -randomZOffset - 1;
        final int zMax = randomZOffset + 1;
        
        this.generateDungeonBlocks(world, rand, position, xMin, zMin, xMax, zMax, true);
        this.generateDungeonTileEntities(world, rand, position, randomXOffset, randomZOffset);
    }

    public boolean hasValidPosition (World world, BlockPos position, int xMin, int zMin, int xMax, int zMax) {
        
        int exposedAirSpaces = 0;
        
        for (int xOffset = xMin; xOffset <= xMax; xOffset++) {

            for (int yOffset = -1; yOffset <= 4; yOffset++) {

                for (int zOffset = zMin; zOffset <= zMax; zOffset++) {

                    final BlockPos currentPosition = position.add(xOffset, yOffset, zOffset);

                    // Check that the bottom and top of the dungeon has solid blocks.
                    if ((yOffset == -1 || yOffset == 4) && !world.getBlockState(currentPosition).getMaterial().isSolid()) {

                        return false;
                    }

                    // Check if the edge pieces have any exposed air blocks.
                    if ((xOffset == xMin || xOffset == xMax || zOffset == zMin || zOffset == zMax) && yOffset == 0 && world.isAirBlock(currentPosition) && world.isAirBlock(currentPosition.up())) {

                        exposedAirSpaces++;
                    }
                }
            }
        }

        // Make sure there are not too few or too many exposed air spaces.
        return exposedAirSpaces >= 1 && exposedAirSpaces <= 5;
    }

    public void generateDungeonBlocks (World world, Random rand, BlockPos position, int xMin, int zMin, int xMax, int zMax, boolean debugGen) {

        for (int xOffset = xMin; xOffset <= xMax; xOffset++) {

            for (int yOffset = 3; yOffset >= -1; yOffset--) {

                for (int zOffset = zMin; zOffset <= zMax; zOffset++) {

                    final BlockPos currentPos = position.add(xOffset, yOffset, zOffset);
                    
                    // Remove space in center of dungeon
                    if (xOffset != xMin && yOffset != -1 && zOffset != zMin && xOffset != xMax && yOffset != 4 && zOffset != zMax) {
                        
                        world.setBlockToAir(currentPos);
                    }

                    // Filling walls and floor.
                    else if ((world.getBlockState(currentPos).getMaterial().isSolid() || debugGen) && world.getBlockState(currentPos).getBlock() != Blocks.CHEST) {

                        // Floors
                        if (yOffset == -1) {

                            world.setBlockState(currentPos, this.data.getFloorBlocks().getRandomEntry().getEntry(), 2);
                        }

                        // Walls
                        else {

                            world.setBlockState(currentPos, this.data.getWallBlocks().getRandomEntry().getEntry(), 2);
                        }
                    }
                }
            }
        }
    }

    public void generateDungeonTileEntities (World world, Random rand, BlockPos position, int randomXOffset, int randomZOffset) {

        // Attempt to place chests in the dungeon. (max 2 chests)
        for (int chestNumber = 0; chestNumber < 2; ++chestNumber) {

            // Attempt to spawn the chest at a random position. (3 attempts)
            for (int spawnAttempt = 0; spawnAttempt < 3; ++spawnAttempt) {

                // Randomize the position to attempt the spawn.
                final int currentX = position.getX() + rand.nextInt(randomXOffset * 2 + 1) - randomXOffset;
                final int currentZ = position.getZ() + rand.nextInt(randomZOffset * 2 + 1) - randomZOffset;
                final BlockPos currentPos = new BlockPos(currentX, position.getY(), currentZ);
                
                // Chests can only spawn in non solid blocks.
                if (world.isAirBlock(currentPos)) {

                    boolean isValidChestPos = false;
                    
                    // Check is position is valid for chests.
                    for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                        final BlockPos pos = currentPos.offset(enumfacing);
                        if (world.getBlockState(pos).getMaterial().isSolid()) {
                            isValidChestPos = true;
                            break;
                        }
                    }
                    
                    // If position is valid, place the chest block, and fill it with loot.
                    if (isValidChestPos) {

                        this.generateChest(world, currentPos, rand);
                        break;
                    }
                }
            }
        }
        
        this.generateSpawner(world, rand, position);
    }
    
    public void generateChest (World world, BlockPos currentPos, Random rand) {

        world.setBlockState(currentPos, Blocks.CHEST.correctFacing(world, currentPos, this.chest), 2);
        final TileEntity chestTile = world.getTileEntity(currentPos);
        
        if (chestTile instanceof TileEntityChest) {

            ((TileEntityChest) chestTile).setLootTable(this.data.getLootTables().getRandomEntry().getEntry(), rand.nextLong());
        }
    }

    public void generateSpawner (World world, Random rand, BlockPos position) {
        
        world.setBlockState(position, this.spawner, 2);
        final TileEntity spawnerEntity = world.getTileEntity(position);
        
        if (spawnerEntity instanceof TileEntityMobSpawner) {

            ((TileEntityMobSpawner) spawnerEntity).getSpawnerBaseLogic().setEntityId(this.data.getSpawnerData().getRandomEntry().getEntry().getEntity().getRegistryName());
        }
    }
}