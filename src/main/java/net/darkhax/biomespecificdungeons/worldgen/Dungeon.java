package net.darkhax.biomespecificdungeons.worldgen;

import java.util.HashSet;
import java.util.Set;

import net.darkhax.biomespecificdungeons.data.BlockData;
import net.darkhax.biomespecificdungeons.data.DungeonData;
import net.darkhax.biomespecificdungeons.data.LootTableData;
import net.darkhax.biomespecificdungeons.data.SpawnerData;
import net.darkhax.bookshelf.lib.WeightedSelector;
import net.darkhax.bookshelf.lib.WeightedSelector.WeightedEntry;
import net.darkhax.bookshelf.util.BiomeUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Dungeon {

    private ResourceLocation identifier;
    
    private WeightedSelector<IBlockState> wallBlocks;
    
    private WeightedSelector<IBlockState> floorBlocks;
    
    private Set<Biome> validBiomes;
    
    private WeightedSelector<ResourceLocation> lootTables;
    
    private WeightedSelector<SpawnerData> spawnerData;
    
    private WorldGenBiomeDungeon worldGenBiomeDungeon;
    
    public Dungeon(DungeonData data) {
        
        identifier = data.getIdentifier();
        wallBlocks = new WeightedSelector<>();
        floorBlocks = new WeightedSelector<>();
        validBiomes = new HashSet<>();
        lootTables = new WeightedSelector<>();
        spawnerData = new WeightedSelector<>();
        
        for (BlockData wallBlock : data.getWallBlocks()) {
            
            final WeightedEntry<IBlockState> entry = wallBlock.build(); 
            
            if (entry != null) {
                
                wallBlocks.addEntry(entry);
            }
        }
        
        for (BlockData floorBlock : data.getFloorBlocks()) {
            
            final WeightedEntry<IBlockState> entry = floorBlock.build(); 
            
            if (entry != null) {
                
                floorBlocks.addEntry(entry);
            }
        }
        
        for (String biomeTag : data.getValidBiomes()) {
            
            if (biomeTag.startsWith("tag:") && biomeTag.length() > 4) {
                
                this.validBiomes.addAll(BiomeUtils.getBiomesForType(biomeTag.substring(4)));
            }
            
            else {
                
                Biome biomeValue = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biomeTag));
                
                if (biomeValue != null) {
                    
                    this.validBiomes.add(biomeValue);
                }
            }
        }
        
        for (LootTableData tableData : data.getLootTables()) {
            
            this.lootTables.addEntry(tableData.getLootTable(), tableData.getWeight());
        }
        
        this.worldGenBiomeDungeon = new WorldGenBiomeDungeon(this);
    }
    
    public void addSpawnerData(SpawnerData data) {
        
        this.spawnerData.addEntry(data, data.getWeight());
    }

    public ResourceLocation getIdentifier () {
        
        return identifier;
    }

    public WeightedSelector<IBlockState> getWallBlocks () {
        
        return wallBlocks;
    }

    public WeightedSelector<IBlockState> getFloorBlocks () {
        
        return floorBlocks;
    }

    public Set<Biome> getValidBiomes () {
        
        return validBiomes;
    }

    public WeightedSelector<ResourceLocation> getLootTables () {
        
        return lootTables;
    }

    public WeightedSelector<SpawnerData> getSpawnerData () {
        
        return spawnerData;
    }
    
    public WorldGenBiomeDungeon getWorldGen () {
        
        return worldGenBiomeDungeon;
    }
}