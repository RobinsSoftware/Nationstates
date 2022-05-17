package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.io.File;

import org.json.JSONObject;

import net.md_5.bungee.api.ChatColor;

public class Language extends JSONFile {

    static final JSONObject DEFAULTS;

    static {
        JSONObject defaults = new JSONObject();

        defaults.put("DEFAULT_PREFIX", "&dNationstates &f> ");
        
        defaults.put("DEFAULT_FOOTER", "&7>>> &dPage &b%s &dof &b%s &7<<<");
        defaults.put("DEFAULT_DESCRIPTION", "No description set.");
        defaults.put("DEFAULT_REQUIRE_ADMIN", "&cOnly administrators can specify other nations and players to modify.");
        defaults.put("DEFAULT_NO_PERMISSION", "&cYou do not have permission to use this command.");
        defaults.put("DEFAULT_USAGE", "&cInvalid arguments. '/%s'.");
        defaults.put("DEFAULT_CONSOLE_SENDER", "&cOnly players can use this command.");
        defaults.put("DEFAULT_PLAYER_OFFLINE", "&cPlayer '%s' is offline.");
        defaults.put("DEFAULT_COMMAND_NOT_FOUND", "&cCommand '%s' not found.");
        defaults.put("DEFAULT_NATION_DOES_NOT_EXIST", "&cNation '%s' doesn't exist.");
        defaults.put("DEFAULT_NOT_IN_NATION", "&cYou are not in a nation.");
        
        defaults.put("JOIN_NOTIFY", "&bYou have joined %s.");
        defaults.put("JOIN_NOT_INVITED", "&cYou have not been invited to %s.");
        defaults.put("JOIN_ALREADY_IN_NATION", "&cYou are already a member of %s");
        
        defaults.put("HELP_TITLE", "&7>>> &dNationstates Commands &7<<<");
        defaults.put("HELP_ENTRY_HELP", "&dhelp&7:&dh &7> &bView available commands.");
        defaults.put("HELP_ENTRY_JOIN", "&djoin&7:&dj &7> &bJoin a nation.");
        defaults.put("HELP_ENTRY_LEAVE", "&dleave&7:&dl &7> &bLeave a nation.");
        defaults.put("HELP_ENTRY_MEMBERS", "&dmembers&7:&dm &7> &bView members within a nation.");
        defaults.put("HELP_ENTRY_INFO", "&dinfo&7:&di &7> &bView the information and stats of a nation.");
        defaults.put("HELP_ENTRY_CREATE", "&dcreate&7:&dc &7> &bCreate a nation.");
        defaults.put("HELP_ENTRY_DISBAND", "&ddisband&7:&dd &7> &bDisband a nation.");
        defaults.put("HELP_ENTRY_CLAIMS", "&dclaims&7:&dcs &7> &bView claims owned by a nation.");
        defaults.put("HELP_ENTRY_CLAIM", "&dclaim&7:&dcl &7> &bClaim a chunk of land.");
        defaults.put("HELP_ENTRY_UNCLAIM", "&dunclaim&7:&duc &7> &bUnclaim a chunk of land.");
        defaults.put("HELP_ENTRY_INVITE", "&dinvite&7:&dinv &7> &bInvite a player to a nation.");
        defaults.put("HELP_ENTRY_UNINVITE", "&duninvite&7:&duinv &7> &bUninvite a player from a nation.");
        defaults.put("HELP_ENTRY_PROMOTE", "&dpromote&7:&dp &7> &bPromote a player within a nation.");
        defaults.put("HELP_ENTRY_DEMOTE", "&ddemote&7:&dde &7> &bDemote a player within a nation.");
        
        defaults.put("LEAVE_LEADER", "&cYou must disband your nation or set a new leader before leaving it.");
        
        DEFAULTS = defaults;
    }

    Language() {
        super(new File(PLUGIN.getDataFolder().getPath() + "/language.json"), true, DEFAULTS);
    }

    public String translateColorCodes(String string) {
        string = ChatColor.translateAlternateColorCodes('&', string);
        // add hex later
        return string;
    }
    
    public String get(String string, String... replacements) {
        string = getOptions().getString(string);
        
        for (String r : replacements)
            string = string.replaceFirst("%s", r);
        
        return translateColorCodes(string);
    }

    public String getWithPrefix(String string, String... replacements) {
        return get("DEFAULT_PREFIX") + get(string, replacements);
    }
}
