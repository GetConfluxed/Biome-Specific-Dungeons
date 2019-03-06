package net.darkhax.biomespecificdungeons.commands;

import net.darkhax.bookshelf.command.CommandTree;
import net.minecraft.command.ICommandSender;

public class CommandTreeBiomeSpecificDungeons extends CommandTree {
    
    public CommandTreeBiomeSpecificDungeons() {
        
        this.addSubcommand(new CommandReload());
        this.addSubcommand(new CommandDebugItems());
    }
    
    @Override
    public int getRequiredPermissionLevel () {
        
        return 0;
    }
    
    @Override
    public String getName () {
        
        return "bsdungeons";
    }
    
    @Override
    public String getUsage (ICommandSender sender) {
        
        return "/bsdungeons";
    }
}
