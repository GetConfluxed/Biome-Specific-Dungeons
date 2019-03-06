package net.darkhax.biomespecificdungeons.data;

import java.util.List;

import com.google.gson.annotations.Expose;

import net.minecraft.util.ResourceLocation;

public class DungeonData {
    
    @Expose
    private ResourceLocation identifier;
    
    @Expose
    private List<BlockData> wallBlocks;
    
    @Expose
    private List<BlockData> floorBlocks;
    
    @Expose
    private List<String> validBiomes;
    
    @Expose
    private List<LootTableData> lootTables;

    public ResourceLocation getIdentifier () {
        
        return identifier;
    }

    public List<BlockData> getWallBlocks () {
        
        return wallBlocks;
    }

    public List<BlockData> getFloorBlocks () {
        
        return floorBlocks;
    }

    public List<String> getValidBiomes () {
        
        return validBiomes;
    }

    public List<LootTableData> getLootTables () {
        
        return lootTables;
    }

    public void setIdentifier (ResourceLocation identifier) {
        
        this.identifier = identifier;
    }
}