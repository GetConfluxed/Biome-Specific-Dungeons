package net.darkhax.biomespecificdungeons.commands;

import java.util.Map.Entry;

import net.darkhax.biomespecificdungeons.BiomeSpecificDungeons;
import net.darkhax.biomespecificdungeons.worldgen.Dungeon;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public class CommandDebugItems extends Command {
    
    @Override
    public String getName () {
        
        return "debug";
    }
    
    @Override
    public int getRequiredPermissionLevel () {
        
        return 2;
    }
    
    @Override
    public String getUsage (final ICommandSender sender) {
        
        return "/bsdungeons debug";
    }
    
    @Override
    public void execute (final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        
        final EntityPlayer player = getCommandSenderAsPlayer(sender);
        
        if (player != null) {
           
            for (Entry<ResourceLocation, Dungeon> entry : BiomeSpecificDungeons.REGISTRY.getDungeons().entrySet()) {
                
                ItemStack debugItem = new ItemStack(Items.PAPER);
                debugItem.setStackDisplayName(entry.getKey().toString());
                debugItem.getTagCompound().setString("DebugBiomeDungeonId", entry.getKey().toString());
                player.addItemStackToInventory(debugItem);
            }
        }
    }
}