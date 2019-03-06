package net.darkhax.biomespecificdungeons.commands;

import net.darkhax.biomespecificdungeons.BiomeSpecificDungeons;
import net.darkhax.bookshelf.command.Command;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandReload extends Command {
    
    @Override
    public String getName () {
        
        return "reload";
    }
    
    @Override
    public int getRequiredPermissionLevel () {
        
        return 2;
    }
    
    @Override
    public String getUsage (final ICommandSender sender) {
        
        return "/bsdungeons reload";
    }
    
    @Override
    public void execute (final MinecraftServer server, final ICommandSender sender, final String[] args) throws CommandException {
        
        BiomeSpecificDungeons.REGISTRY.loadData();
        sender.sendMessage(new TextComponentString("Loaded " + BiomeSpecificDungeons.REGISTRY.getDungeons().size() + " new dungeons."));
    }
}