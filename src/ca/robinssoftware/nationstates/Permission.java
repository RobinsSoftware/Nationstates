package ca.robinssoftware.nationstates;

import org.bukkit.command.CommandSender;

public enum Permission {
    
    HELP, CREATE, DISBAND, INFO, JOIN, LEAVE, CLAIM, CONFIRM, UNCLAIM, ADMIN, MEMBERS, PROMOTE, DEMOTE, NAME;
    
    public boolean check(CommandSender sender) {
        return sender.isOp() || sender.hasPermission("nationstates." + name().toLowerCase());
    }
    
}
