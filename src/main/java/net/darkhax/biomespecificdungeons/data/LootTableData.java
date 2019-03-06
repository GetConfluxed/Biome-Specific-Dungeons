package net.darkhax.biomespecificdungeons.data;

import com.google.gson.annotations.Expose;

import net.minecraft.util.ResourceLocation;

public class LootTableData {
    
    @Expose
    private ResourceLocation lootTable;
    
    @Expose
    private int weight = 5;

    public ResourceLocation getLootTable () {
        
        return lootTable;
    }

    public int getWeight () {
        
        return weight;
    }
}