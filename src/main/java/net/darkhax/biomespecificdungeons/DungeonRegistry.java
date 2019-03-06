package net.darkhax.biomespecificdungeons;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.darkhax.biomespecificdungeons.data.DungeonData;
import net.darkhax.biomespecificdungeons.data.SpawnerData;
import net.darkhax.biomespecificdungeons.worldgen.Dungeon;
import net.darkhax.bookshelf.adapters.NBTTagCompoundAdapter;
import net.darkhax.bookshelf.adapters.RegistryEntryAdapter;
import net.darkhax.bookshelf.adapters.ResourceLocationTypeAdapter;
import net.darkhax.bookshelf.dataloader.DataLoader;
import net.darkhax.bookshelf.dataloader.sources.DataProviderAddons;
import net.darkhax.bookshelf.dataloader.sources.DataProviderConfigs;
import net.darkhax.bookshelf.dataloader.sources.DataProviderModsOverridable;
import net.darkhax.bookshelf.lib.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class DungeonRegistry {
    
    private static final Gson GSON;
    
    private Multimap<Biome, Dungeon> biomeToDungeons = ArrayListMultimap.create();
    private Map<ResourceLocation, Dungeon> dungeons = new HashMap<>();
    private List<SpawnerData> tempSpawnerData = new ArrayList<>();
    
    public void loadData () {
        
        dungeons.clear();
        biomeToDungeons.clear();
        tempSpawnerData.clear();
        
        final DataLoader loader = new DataLoader(BiomeSpecificDungeons.LOG);
        loader.addDataProvider(new DataProviderModsOverridable(BiomeSpecificDungeons.MODID));
        loader.addDataProvider(new DataProviderAddons(BiomeSpecificDungeons.MODID));
        loader.addDataProvider(new DataProviderConfigs(BiomeSpecificDungeons.MODID));
        loader.addProcessor("dungeons", this::loadDungeon);
        loader.addProcessor("spawner", this::loadSpawnerEntry);
        loader.loadData();
        
        for (SpawnerData data : tempSpawnerData) {
         
            for (ResourceLocation dungeonId : data.getDungeons()) {
                
                Dungeon dungeon = dungeons.get(dungeonId);
                
                if (dungeon != null) {
                    
                    dungeon.addSpawnerData(data);
                }
            }
        }
        
        tempSpawnerData.clear();
    }
    
    private void loadDungeon(ResourceLocation id, BufferedReader reader) {
        
        DungeonData dungeonData = GSON.fromJson(reader, DungeonData.class);
        
        if (dungeonData != null) {
            
            if (dungeonData.getIdentifier() == null && id != null) {
                
                dungeonData.setIdentifier(id);
            }
            
            final Dungeon dungeon = new Dungeon(dungeonData);
            this.dungeons.put(dungeon.getIdentifier(), dungeon);
            
            for (Biome dungeonBiome : dungeon.getValidBiomes()) {
                this.biomeToDungeons.put(dungeonBiome, dungeon);
            }
        }
    }
    
    private void loadSpawnerEntry(ResourceLocation id, BufferedReader reader) {
        
        SpawnerData spawnerData = GSON.fromJson(reader, SpawnerData.class);
        
        if (spawnerData != null) {
            
            this.tempSpawnerData.add(spawnerData);
        }
    }

    public Map<ResourceLocation, Dungeon> getDungeons() {
        
        return Collections.unmodifiableMap(this.dungeons);
    }
    
    public Dungeon getDungeonForBiome(Biome biome) {
        
        List<Dungeon> dungeonsForBiome = (List<Dungeon>) this.biomeToDungeons.get(biome); 
        return dungeonsForBiome.isEmpty() ? null : dungeonsForBiome.get(Constants.RANDOM.nextInt(dungeonsForBiome.size()));
    }
    
    static {
        
        final GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        builder.registerTypeAdapter(ResourceLocation.class, new ResourceLocationTypeAdapter());
        builder.registerTypeAdapter(EntityEntry.class, new RegistryEntryAdapter<>(ForgeRegistries.ENTITIES));
        builder.registerTypeAdapter(NBTTagCompound.class, new NBTTagCompoundAdapter());
        GSON = builder.create();
    }
}
