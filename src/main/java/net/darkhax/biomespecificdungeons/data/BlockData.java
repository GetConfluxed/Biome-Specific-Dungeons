package net.darkhax.biomespecificdungeons.data;

import com.google.gson.annotations.Expose;

import net.darkhax.bookshelf.lib.WeightedSelector.WeightedEntry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockData {
    
    @Expose
    private ResourceLocation block;
    
    @Expose
    private int meta = 0;
    
    @Expose
    private int weight = 5;
    
    public WeightedEntry<IBlockState> build() {
        
        final Block blockVal = ForgeRegistries.BLOCKS.getValue(block);
        
        if (blockVal != null) {
            
            final IBlockState state = blockVal.getStateFromMeta(this.meta);
            
            if (state != null) {
                
                return new WeightedEntry<>(state, this.weight);
            }
        }
        
        return null;
    }
}