package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DefaultCommand implements CommandExecutor {

    private static final List<String> HELP = List.of(PLUGIN.getLanguageData().get("HELP_ENTRY_HELP"),
            PLUGIN.getLanguageData().get("HELP_ENTRY_MEMBERS"), PLUGIN.getLanguageData().get("HELP_ENTRY_INFO"),
            PLUGIN.getLanguageData().get("HELP_ENTRY_JOIN"), PLUGIN.getLanguageData().get("HELP_ENTRY_LEAVE"),
            PLUGIN.getLanguageData().get("HELP_ENTRY_CREATE"), PLUGIN.getLanguageData().get("HELP_ENTRY_DISBAND"),
            PLUGIN.getLanguageData().get("HELP_ENTRY_CLAIMS"), PLUGIN.getLanguageData().get("HELP_ENTRY_CLAIM"),
            PLUGIN.getLanguageData().get("HELP_ENTRY_UNCLAIM"));

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
            join(sender, args);
            return true;
        case "leave":
        case "l":
            leave(sender, args);
            return true;
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
            try {
                new PagedChatMessage(PLUGIN.getLanguageData().get("HELP_TITLE"), HELP).sendTo(sender,
                        Integer.parseInt(args[0]) - 1);
            } catch (NumberFormatException e) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_COMMAND_NOT_FOUND", args[0]));
            }
        }

        return true;
    }

    void help(CommandSender sender, String... args) {
        if (!Permission.HELP.get(sender)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NO_PERMISSION"));
            return;
        }

        int page = 0;
        if (args.length >= 2) {
            page = Integer.parseInt(args[1]) - 1;
        }

        new PagedChatMessage(PLUGIN.getLanguageData().get("HELP_TITLE"), HELP).sendTo(sender, page);
    }

    void join(CommandSender sender, String... args) {
        if (!Permission.JOIN.get(sender)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NO_PERMISSION"));
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(
                    PLUGIN.getLanguageData().getWithPrefix("DEFAULT_USAGE", "nation join <nation> [player]"));
        } else if (args.length == 2) {
            if (!(sender instanceof OfflinePlayer)) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_CONSOLE_SENDER", args));
                return;
            }

            Nation target = Nation.get(args[1].toLowerCase());
            OfflinePlayer player = (OfflinePlayer) sender;
            if (target != null) {
                if (target.getInvites().contains(player)) {
                    Nation current = new OfflinePlayerWrapper(player).getNation();

                    if (current != null) {
                        sender.sendMessage(
                                PLUGIN.getLanguageData().getWithPrefix("JOIN_ALREADY_IN_NATION", current.getName()));
                    } else {
                        target.uninvite(player);
                        target.join(player);
                    }
                } else {
                    sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("JOIN_NOT_INVITED", target.getName()));
                }
            } else {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NATION_DOES_NOT_EXIST", args[1]));
            }
        } else if (args.length == 3) {
            if (!Permission.ADMIN.get(sender))
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_REQUIRE_ADMIN"));
            else {
                if (Nation.get(args[1].toLowerCase()) != null) {
                    if (Bukkit.getPlayer(args[2]) != null) {
                        Nation.get(args[1].toLowerCase()).uninvite(Bukkit.getPlayer(args[2]));
                        Nation.get(args[1].toLowerCase()).join(Bukkit.getPlayer(args[2]));
                    } else {
                        sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_PLAYER_OFFLINE", args[2]));
                    }
                } else {
                    sender.sendMessage(
                            PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NATION_DOES_NOT_EXIST", args[1]));
                }
            }
        } else
            sender.sendMessage(
                    PLUGIN.getLanguageData().getWithPrefix("DEFAULT_USAGE", "nation join <nation> [player]"));
    }

    void leave(CommandSender sender, String... args) {
        if (!Permission.LEAVE.get(sender)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NO_PERMISSION"));
            return;
        }

        if (args.length == 1) {
            if (!(sender instanceof OfflinePlayer)) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_CONSOLE_SENDER", args));
                return;
            }

            OfflinePlayer player = (OfflinePlayer) sender;
            Nation current = new OfflinePlayerWrapper(player).getNation();

            if (current != null) {
                if (current.getLeader() == player) {
                    sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("LEAVE_LEADER"));
                    current.kick(player);
                }
            } else {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NOT_IN_NATION"));
            }
        } else
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_USAGE", "nation leave"));
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
