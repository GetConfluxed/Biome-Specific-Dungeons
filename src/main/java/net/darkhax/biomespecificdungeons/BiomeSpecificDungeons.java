package net.darkhax.biomespecificdungeons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.biomespecificdungeons.commands.CommandTreeBiomeSpecificDungeons;
import net.darkhax.biomespecificdungeons.worldgen.WorldGenEventHandler;
import net.darkhax.bookshelf.BookshelfRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BiomeSpecificDungeons.MODID, name = BiomeSpecificDungeons.NAME, acceptableRemoteVersions = "*", dependencies = "required-after:bookshelf@[2.3.577,)", version = "@VERSION@", certificateFingerprint = "@FINGERPRINT@")
public class BiomeSpecificDungeons {
    
    // Properties
    public static final String MODID = "biomespecificdungeons";
    public static final String NAME = "Biome Specific Dungeons";
    public static final Logger LOG = LogManager.getLogger(NAME);
    public static final DungeonRegistry REGISTRY = new DungeonRegistry();
    
    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {

        MinecraftForge.TERRAIN_GEN_BUS.register(new WorldGenEventHandler());
        BookshelfRegistry.addCommand(new CommandTreeBiomeSpecificDungeons());
    }
    
    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        
        REGISTRY.loadData();
    }
}