package ca.robinssoftware.nationstates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class DefaultTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
        case 1:
            List<String> help = new ArrayList<>();

            if (Permission.CLAIM.get(sender))
                help.add("claim");
            if (Permission.CONFIRM.get(sender))
                help.add("confirm");
            if (Permission.CREATE.get(sender))
                help.add("create");
            if (Permission.DEMOTE.get(sender))
                help.add("demote");
            if (Permission.DISBAND.get(sender))
                help.add("disband");
            if (Permission.HELP.get(sender))
                help.add("help");
            if (Permission.INFO.get(sender))
                help.add("info");
            if (Permission.JOIN.get(sender))
                help.add("join");
            if (Permission.LEAVE.get(sender))
                help.add("leave");
            if (Permission.MEMBERS.get(sender))
                help.add("members");
            if (Permission.NAME.get(sender))
                help.add("name");
            if (Permission.PROMOTE.get(sender))
                help.add("promote");
            if (Permission.UNCLAIM.get(sender))
                help.add("unclaim");

            return help;
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
