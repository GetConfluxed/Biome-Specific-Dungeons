package net.darkhax.biomespecificdungeons.worldgen;

import java.util.Random;

import net.darkhax.biomespecificdungeons.BiomeSpecificDungeons;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = BiomeSpecificDungeons.MODID)
public class WorldGenEventHandler {

    @SubscribeEvent
    public void onGenInitialized (Populate event) {

        if (event.getType() == EventType.DUNGEON) {

            final BlockPos chunkOrigin = new BlockPos(event.getChunkX() * 16, 0, event.getChunkZ() * 16);
            final Random rand = event.getRand();

            boolean cancelOriginal = false;

            for (int j2 = 0; j2 < 3; ++j2) {

                final int i3 = rand.nextInt(16) + 8;
                final int l3 = rand.nextInt(256);
                final int l1 = rand.nextInt(16) + 8;

                final BlockPos genPos = chunkOrigin.add(i3, l3, l1);

                final Dungeon dungeon = BiomeSpecificDungeons.REGISTRY.getDungeonForBiome(event.getWorld().getBiome(genPos));

                if (dungeon != null && dungeon.getWorldGen().generate(event.getWorld(), rand, genPos)) {

                    BiomeSpecificDungeons.LOG.info("Generated dungeon at {}.", genPos.toString());
                    cancelOriginal = true;
                }
            }

            if (cancelOriginal) {

                event.setResult(Result.DENY);
            }
        }
    }
    
    @SubscribeEvent
    public static void onRightClick (PlayerInteractEvent.RightClickItem event) {
        
        if (!event.getWorld().isRemote && event.getItemStack().hasTagCompound()) {
            
            String dungeonId = event.getItemStack().getTagCompound().getString("DebugBiomeDungeonId");
            Dungeon dungeon = BiomeSpecificDungeons.REGISTRY.getDungeons().get(new ResourceLocation(dungeonId));
            
            if (dungeon != null) {
                
                dungeon.getWorldGen().generateDebug(event.getWorld(), event.getWorld().rand, event.getEntityPlayer().getPosition().offset(EnumFacing.DOWN, 1));
                
                if (!event.getEntityPlayer().isCreative()) {
                    
                    event.getItemStack().shrink(1);
                }
            }
        }
    }
}