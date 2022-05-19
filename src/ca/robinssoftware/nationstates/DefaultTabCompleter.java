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

            if (Permission.CLAIM.check(sender))
                help.add("claim");
            if (Permission.CONFIRM.check(sender))
                help.add("confirm");
            if (Permission.CREATE.check(sender))
                help.add("create");
            if (Permission.DEMOTE.check(sender))
                help.add("demote");
            if (Permission.DISBAND.check(sender))
                help.add("disband");
            if (Permission.HELP.check(sender))
                help.add("help");
            if (Permission.INFO.check(sender))
                help.add("info");
            if (Permission.JOIN.check(sender))
                help.add("join");
            if (Permission.LEAVE.check(sender))
                help.add("leave");
            if (Permission.MEMBERS.check(sender))
                help.add("members");
            if (Permission.NAME.check(sender))
                help.add("name");
            if (Permission.PROMOTE.check(sender))
                help.add("promote");
            if (Permission.UNCLAIM.check(sender))
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
                if (Permission.ADMIN.check(sender))
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
