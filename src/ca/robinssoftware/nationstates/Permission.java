package ca.robinssoftware.nationstates;

import org.bukkit.command.CommandSender;

public enum Permission {
    
    COMMAND, HELP, CREATE, DISBAND, INFO, JOIN, LEAVE, CLAIM, UNCLAIM, CLAIMS, COMPASS, ADMIN;
    
    public boolean get(CommandSender sender) {
        return sender.isOp() || sender.hasPermission("nationstates." + name().toLowerCase());
    }
    
}
