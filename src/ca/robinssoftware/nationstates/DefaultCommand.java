package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DefaultCommand implements CommandExecutor {
    
    private static final List<String> HELP = 
            List.of(PLUGIN.getLanguageData().get("HELP_ENTRY_HELP"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_JOIN"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_LEAVE"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_MEMBERS"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_INFO"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_CREATE"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_DISBAND"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_CLAIMS"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_CLAIM"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_UNCLAIM"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_INVITE"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_UNINVITE"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_PROMOTE"),
                    PLUGIN.getLanguageData().get("HELP_ENTRY_DEMOTE"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            help(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
        case "help":
        case "h":
            help(sender, args);
            return true;
        case "join":
        case "j":
        case "leave":
        case "l":
        case "members":
        case "m":
        case "info":
        case "i":
        case "claims":
        case "cs":
        case "create":
        case "c":
        case "unclaim":
        case "uc":
        case "claim":
        case "cl":
        case "disband":
        case "d":
        case "invite":
        case "inv":
        case "uninvite":
        case "uinv":
        case "promote":
        case "p":
        case "demote":
        case "de":
        default:
        }

        return true;
    }

    void help(CommandSender sender, String... args) {
        int page = 0;
        if (args.length >= 2) {
            page = Integer.parseInt(args[1]) - 1;
        }
        
        new PagedChatMessage(PLUGIN.getLanguageData().get("HELP_TITLE"), HELP).sendTo(sender, page);
    }

    void join() {

    }

    void members() {

    }

    void info() {

    }

    void unclaim() {

    }

    void claim() {

    }

    void disband() {

    }

    void invite() {

    }

}
