package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.io.File;

import org.json.JSONObject;

import net.md_5.bungee.api.ChatColor;

public class Language extends JSONFile {

    static final JSONObject DEFAULTS;

    static {
        JSONObject defaults = new JSONObject();

        defaults.put("PREFIX", "&dNationstates &f> ");
        
        defaults.put("DEFAULT_FOOTER", "&7>>> &dPage &b%s &dof &b%s &7<<<");
        defaults.put("DEFAULT_DESCRIPTION", "No description set.");
        
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
        return get("PREFIX") + get(string, replacements);
    }
}
