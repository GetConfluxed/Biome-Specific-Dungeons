package net.darkhax.biomespecificdungeons.data;

import java.util.List;

import com.google.gson.annotations.Expose;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;

public class SpawnerData {
    
    @Expose
    private List<ResourceLocation> dungeons;
    
    @Expose
    private EntityEntry entity;
    
    @Expose
    private NBTTagCompound tag;
    
    @Expose
    private int weight = 10;

    public EntityEntry getEntity () {
        
        return entity;
    }

    public NBTTagCompound getTag () {
        
        return tag;
    }

    public int getWeight () {
        
        return weight;
    }

    public List<ResourceLocation> getDungeons () {
        
        return dungeons;
    }
}
