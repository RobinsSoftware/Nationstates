package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

import net.md_5.bungee.api.ChatColor;

public class Language extends JSONFile {

    static final JSONObject DEFAULTS;

    static {
        JSONObject defaults = new JSONObject();

        defaults.put("TIME_FORMAT", "yyyy-MM-dd HH:mm:ssZ");

        defaults.put("DEFAULT_PREFIX", "&dNationstates &f> ");

        defaults.put("DEFAULT_FOOTER", "&7>>> &dPage &b%s &dof &b%s &7<<<");
        defaults.put("DEFAULT_DESCRIPTION", "No description set.");
        defaults.put("DEFAULT_REQUIRE_ADMIN", "&cOnly administrators can make changes other nations and players.");
        defaults.put("DEFAULT_NO_PERMISSION", "&cYou do not have permission to use this command.");
        defaults.put("DEFAULT_USAGE", "&cInvalid arguments. '/%s'.");
        defaults.put("DEFAULT_CONSOLE_SENDER", "&cOnly players can use this command.");
        defaults.put("DEFAULT_PLAYER_OFFLINE", "&cPlayer '%s' is not online.");
        defaults.put("DEFAULT_COMMAND_NOT_FOUND", "&cCommand '%s' not found.");
        defaults.put("DEFAULT_NATION_DOES_NOT_EXIST", "&cNation '%s' doesn't exist.");
        defaults.put("DEFAULT_NOT_IN_NATION", "&cYou are not in a nation.");
        defaults.put("DEFAULT_ALREADY_IN_NATION", "&cYou are already a member of %s");
        defaults.put("DEFAULT_REQUIRE_RANK", "&cYou must be at least the rank of %s to do use this command.");

        defaults.put("JOIN_NOTIFY", "&bYou have joined %s.");
        defaults.put("JOIN_NOT_INVITED", "&cYou have not been invited to %s.");

        defaults.put("HELP_TITLE", "&7>>> &dNationstates Commands &7<<<");
        defaults.put("HELP_ENTRY_HELP", "&dhelp &7> &bView available commands.");
        defaults.put("HELP_ENTRY_JOIN", "&djoin &7> &bJoin a nation.");
        defaults.put("HELP_ENTRY_LEAVE", "&dleave &7> &bLeave a nation.");
        defaults.put("HELP_ENTRY_MEMBERS", "&dmembers &7> &bView members within a nation.");
        defaults.put("HELP_ENTRY_INFO", "&dinfo &7> &bView the information and stats of a nation.");
        defaults.put("HELP_ENTRY_CREATE", "&dcreate &7> &bCreate a nation.");
        defaults.put("HELP_ENTRY_DISBAND", "&ddisband&7 &7> &bDisband a nation.");
        defaults.put("HELP_ENTRY_CLAIM", "&dclaim &7> &bClaim a chunk of land.");
        defaults.put("HELP_ENTRY_UNCLAIM", "&dunclaim &7> &bUnclaim a chunk of land.");
        defaults.put("HELP_ENTRY_INVITE", "&dinvite &7> &bInvite a player to a nation.");
        defaults.put("HELP_ENTRY_UNINVITE", "&duninvite &7> &bUninvite a player from a nation.");
        defaults.put("HELP_ENTRY_PROMOTE", "&dpromote &7> &bPromote a player within a nation.");
        defaults.put("HELP_ENTRY_DEMOTE", "&ddemote &7> &bDemote a player within a nation.");
        defaults.put("HELP_ENTRY_CONFIRM", "&dconfirm &7> &bConfirm an action.");
        defaults.put("HELP_ENTRY_NAME", "&dname &7> &bChange your nation's display name.");

        defaults.put("INFO_TITLE", "&7>>> &d%s &7<<<");
        defaults.put("INFO_ENTRY_ID", "&dID &7> &b%s");
        defaults.put("INFO_ENTRY_CREATED", "&dCreated &7> &b%s");
        defaults.put("INFO_ENTRY_MEMBERS", "&dMembers &7> &b%s");
        defaults.put("INFO_ENTRY_LEADER", "&dLeader &7> &b%s");
        defaults.put("INFO_ENTRY_CHUNKS", "&dChunks &7> &b%s");
        defaults.put("INFO_ENTRY_POLITICS", "&dPolitical Ideology &7> &b%s");
        defaults.put("INFO_ENTRY_SOCIAL", "&dSocial Ranking &7> &b%s");
        defaults.put("INFO_ENTRY_ECONOMIC", "&dEconomic Ranking &7> &b%s");
        defaults.put("INFO_ENTRY_AUTHORITARIANISM", "&dAuthoritarianism Ranking &7> &b%s");

        defaults.put("CREATE_ALPHABETICAL", "&cNation name must be alphabetical and at least 3 characters in length.");
        defaults.put("CREATE_EXISTS", "&cNation %s already exists.");
        defaults.put("CREATE_SUCCESS", "&bNation %s has been created.");

        defaults.put("LEAVE_LEADER", "&cYou must disband your nation or set a new leader before leaving it.");

        defaults.put("CONFIRM_NONE", "&cNothing left to confirm.");
        
        defaults.put("DISBAND_CONFIRM", "&bTo confirm the deletion of %s, use the command 'nation confirm'.");
        defaults.put("DISBAND_NOTIFY", "&cYour nation has been disbanded");
        
        defaults.put("CLAIM_SUCCESS", "&bClaimed %s chunk(s) for %s.");
        defaults.put("CLAIM_ALREADY_OWNED", "&bChunk is already owned by %s.");
        defaults.put("CLAIM_NOT_ENOUGH_CHUNKS", "&cSpecified nation does not have enough chunks to claim land.");
        defaults.put("CLAIM_OWNED", "&cYou cannot interact with the land of %s.");
        
        defaults.put("UNCLAIM_SUCCESS", "&bUnclaimed %s chunk(s) for %s.");
        defaults.put("UNCLAIM_CANT_UNCLAIM", "&bYou cannot unclaim this chunk.");

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

    public String getField(String string, String... replacements) {
        if (!getOptions().has(string)) {
            getOptions().put(string, DEFAULTS.get(string));
            
            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        string = getOptions().getString(string);

        for (String r : replacements)
            string = string.replaceFirst("%s", r);

        return translateColorCodes(string);
    }

    public String getFieldWithPrefix(String string, String... replacements) {
        return getField("DEFAULT_PREFIX") + getField(string, replacements);
    }
}
