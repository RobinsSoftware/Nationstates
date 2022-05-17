package ca.robinssoftware.nationstates;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class DefaultTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
        case 1:
            return List.of("join", "leave", "disband", "create", "claim", "unclaim", "info", "members", "invite",
                    "claims", "help", "uninvite", "promote", "demote");
        case 2:
            switch (args[0].toLowerCase()) {
            case "join":
            case "j":
            case "members":
            case "m":
            case "info":
            case "i":
            case "claims":
            case "cs":
                // return nations
            case "disband":
            case "d":
                // only return nations if admin, else none
                if (Permission.ADMIN.get(sender))
                    return null;
                else
                    return List.of();
            case "invite":
            case "inv":
            case "uninvite":
            case "uinv":
            case "promote":
            case "p":
            case "demote":
            case "de":
                return null; // return player names
            default:
                return List.of(); // return nothing
            }
        }

        return null;
    }

}
