package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ca.robinssoftware.nationstates.Nation.NationExistsException;

public class DefaultCommand implements CommandExecutor {

    private static final HashMap<String, Runnable> confirmations = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            help(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
        case "help":
            help(sender, args);
            return true;
        case "join":
            join(sender, args);
            return true;
        case "leave":
            leave(sender, args);
            return true;
        case "members":
            members(sender, args);
            return true;
        case "info":
            info(sender, args);
            return true;
        case "create":
            create(sender, args);
            return true;
        case "unclaim":
        case "claim":
            claim(sender, args);
            return true;
        case "disband":
            disband(sender, args);
            return true;
        case "invite":
        case "uninvite":
        case "promote":
        case "demote":
        case "name":
        case "confirm":
            confirm(sender, args);
            return true;
        default:
            try {
                new PagedChatMessage(PLUGIN.getLanguageData().get("HELP_TITLE"), helpList(sender)).sendTo(sender,
                        Integer.parseInt(args[0]) - 1);
            } catch (NumberFormatException e) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_COMMAND_NOT_FOUND", args[0]));
            }
        }

        return true;
    }

    void addConfirmation(String owner, Runnable confirmation) {
        confirmations.put(owner, confirmation);

        Bukkit.getScheduler().scheduleSyncDelayedTask(PLUGIN, new Runnable() {

            @Override
            public void run() {
                confirmations.remove(owner);
            }

        }, 60 * 20);
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

        new PagedChatMessage(PLUGIN.getLanguageData().get("HELP_TITLE"), helpList(sender)).sendTo(sender, page);
    }

    List<String> helpList(CommandSender sender) {
        List<String> help = new ArrayList<>();

        if (Permission.CLAIM.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_CLAIM"));
        if (Permission.CONFIRM.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_CONFIRM"));
        if (Permission.CREATE.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_CREATE"));
        if (Permission.DEMOTE.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_DEMOTE"));
        if (Permission.DISBAND.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_DISBAND"));
        if (Permission.HELP.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_HELP"));
        if (Permission.INFO.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_INFO"));
        if (Permission.JOIN.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_JOIN"));
        if (Permission.LEAVE.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_LEAVE"));
        if (Permission.MEMBERS.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_MEMBERS"));
        if (Permission.NAME.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_NAME"));
        if (Permission.PROMOTE.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_PROMOTE"));
        if (Permission.UNCLAIM.get(sender))
            help.add(PLUGIN.getLanguageData().get("HELP_ENTRY_UNCLAIM"));

        return help;
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
                                PLUGIN.getLanguageData().getWithPrefix("DEFAULT_ALREADY_IN_NATION", current.getName()));
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

    void info(CommandSender sender, String... args) {
        if (!Permission.INFO.get(sender)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NO_PERMISSION"));
            return;
        }

        if (args.length > 3) {
            sender.sendMessage(
                    PLUGIN.getLanguageData().getWithPrefix("DEFAULT_USAGE", "nation info [nation:page] [page]"));
            return;
        }

        Nation nation = null;
        if (args.length == 1)
            if (sender instanceof Player)
                nation = new OfflinePlayerWrapper((Player) sender).getNation();
            else {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_CONSOLE_SENDER"));
                return;
            }
        else
            nation = Nation.get(args[1].toLowerCase());

        if (nation == null) {
            if (args.length == 1)
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NOT_IN_NATION"));
            else
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NATION_DOES_NOT_EXIST", args[1].toLowerCase()));
            return;
        }

        sender.sendMessage(PLUGIN.getLanguageData().get("INFO_TITLE"),
                PLUGIN.getLanguageData().get("INFO_ENTRY_ID", nation.getName()),
                PLUGIN.getLanguageData().get("INFO_ENTRY_CREATED",
                        new SimpleDateFormat(PLUGIN.getLanguageData().get("TIME_FORMAT"))
                                .format(new Date(nation.getTimeCreated()))),
                PLUGIN.getLanguageData().get("INFO_ENTRY_MEMBERS", "" + nation.getAllPlayers().size()),
                PLUGIN.getLanguageData().get("INFO_ENTRY_LEADER", nation.getLeader().getName()),
                PLUGIN.getLanguageData().get("INFO_ENTRY_POLITICS"), PLUGIN.getLanguageData().get("INFO_ENTRY_SOCIAL"),
                PLUGIN.getLanguageData().get("INFO_ENTRY_ECONOMIC"),
                PLUGIN.getLanguageData().get("INFO_ENTRY_AUTHORITARIANISM"));
    }

    void members(CommandSender sender, String... args) {
        if (!Permission.MEMBERS.get(sender)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NO_PERMISSION"));
            return;
        }
    }

    void create(CommandSender sender, String... args) {
        if (!Permission.CREATE.get(sender)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NO_PERMISSION"));
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_USAGE", "nation create <name>"));
        } else if (args.length == 2) {
            if (new OfflinePlayerWrapper((Player) sender).getNation() == null) {
                if (Pattern.compile("^[a-zA-Z]*$").matcher(args[1]).matches()) {
                    try {
                        Nation.create(args[1].toLowerCase(), (Player) sender);
                        sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("CREATE_SUCCESS"));
                    } catch (NationExistsException e) {
                        sender.sendMessage(
                                PLUGIN.getLanguageData().getWithPrefix("CREATE_EXISTS", args[1].toLowerCase()));
                    }
                } else {
                    sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("CREATE_ALPHABETICAL"));
                }
            } else {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_ALREADY_IN_NATION",
                        new OfflinePlayerWrapper((Player) sender).getNation().getName()));
            }
        } else {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_USAGE", "nation create <name>"));
        }
    }

    void disband(CommandSender sender, String... args) {
        if (!Permission.MEMBERS.get(sender)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NO_PERMISSION"));
            return;
        }

        if (args.length > 2) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_USAGE", "nation disband [nation]"));
            return;
        }

        if (args.length == 1 & !(sender instanceof Player)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_CONSOLE_SENDER"));
            return;
        } else if (args.length == 2 & !(Permission.ADMIN.get(sender))) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_REQUIRE_ADMIN"));
            return;
        }

        Nation nation = args.length == 1 ? new OfflinePlayerWrapper((Player) sender).getNation() : Nation.get(args[1].toLowerCase());
        if (nation == null) {
            if (args.length == 1)
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NOT_IN_NATION"));
            else
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NATION_DOES_NOT_EXIST", args[1].toLowerCase()));
            return;
        }
        
        if (!Permission.ADMIN.get(sender) && !nation.getRank((Player) sender).inherits(NationRank.LEADER)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_REQUIRE_RANK"), "leader");
            return;
        }

        sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DISBAND_CONFIRM", nation.getName()));
        addConfirmation(sender.getName(), new Runnable() {

            @Override
            public void run() {
                nation.disband();
            }

        });
    }

    void claim(CommandSender sender, String... args) {
        if (!Permission.CLAIM.get(sender)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NO_PERMISSION"));
            return;
        }
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_CONSOLE_SENDER"));
            return;
        }
        
        Nation nation = null;
        
        if (args.length == 2) {
            if (!Permission.ADMIN.get(sender)) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_REQUIRE_ADMIN"));
                return;
            }
            
            nation = Nation.get(args[1].toLowerCase());
            
            if (nation == null) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_REQUIRE_ADMIN"));
                return;
            }
        
        } else {
            nation = new OfflinePlayerWrapper((Player) sender).getNation();
            
            if (nation == null) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NOT_IN_NATION"));
                return;
            }
            
            if (!Permission.ADMIN.get(sender) && !nation.getRank((Player) sender).inherits(NationRank.OFFICER)) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_REQUIRE_RANK"), "officer");
                return;
            }
        }
        
        if(nation.getChunks() == 0) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("CLAIM_NOT_ENOUGH_CHUNKS"));
            return;
        }
        
        Location location = ((Player) sender).getLocation();
        WorldRegion region = WorldRegion.getAndUseOnce(location);
        
        if (region.getOwner(location) != null && region.getOwner(location) != nation) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("CLAIM_ALREADY_OWNED", region.getOwner(location).getName()));
            return;
        }
        
        nation.chunks--;
        region.setOwner(location, nation);
        sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("CLAIM_SUCCESS", "1", nation.getName()));
    }

    void unclaim(CommandSender sender, String... args) {
        if (!Permission.CLAIM.get(sender)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NO_PERMISSION"));
            return;
        }
        
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_CONSOLE_SENDER"));
            return;
        }
        
        Nation nation = null;
        
        if (args.length == 2) {
            if (!Permission.ADMIN.get(sender)) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_REQUIRE_ADMIN"));
                return;
            }
            
            nation = Nation.get(args[1].toLowerCase());
            
            if (nation == null) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_REQUIRE_ADMIN"));
                return;
            }
        
        } else {
            nation = new OfflinePlayerWrapper((Player) sender).getNation();
            
            if (nation == null) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_NOT_IN_NATION"));
                return;
            }
            
            if (!Permission.ADMIN.get(sender) && !nation.getRank((Player) sender).inherits(NationRank.OFFICER)) {
                sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("DEFAULT_REQUIRE_RANK"), "officer");
                return;
            }
        }
        
        Location location = ((Player) sender).getLocation();
        WorldRegion region = WorldRegion.getAndUseOnce(location);
        
        if (region.getOwner(location) != null && region.getOwner(location) != nation) {
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("CLAIM_ALREADY_OWNED", region.getOwner(location).getName()));
            return;
        }
        
        nation.chunks++;
        region.removeOwner(location);
        sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("UNCLAIM_SUCCESS", "1", nation.getName()));
    }

    void invite() {

    }

    void confirm(CommandSender sender, String... args) {
        if (confirmations.get(sender.getName()) == null)
            sender.sendMessage(PLUGIN.getLanguageData().getWithPrefix("CONFIRM_NONE"));
        else {
            confirmations.get(sender.getName()).run();
            confirmations.remove(sender.getName());
        }
    }

}
