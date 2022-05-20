package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class Nation extends JSONFile {

    static JSONObject getDefaults(String name, OfflinePlayer leader) {
        JSONObject obj = new JSONObject();

        obj.put("name", name.toLowerCase());
        obj.put("created", System.currentTimeMillis());
        obj.put("leader", leader.getUniqueId().toString());
        obj.put("chunks", PLUGIN.getPluginConfig().getNewNationChunks());

        return obj;
    }

    private Nation(String name, boolean create, JSONObject defaults) {
        super(new File(PLUGIN.getDataFolder() + "/nation/" + name.toLowerCase() + ".json"), create, defaults);
    }

    public boolean containsPlayer(OfflinePlayer player) {
        return getRank(player) != null;
    }

    public OfflinePlayer getLeader() {
        return Bukkit.getOfflinePlayer(UUID.fromString(getString("leader")));
    }

    public ArrayList<OfflinePlayer> getCouncil() {
        ArrayList<OfflinePlayer> council = new ArrayList<>();
        for (Object o : getList("council"))
            council.add(Bukkit.getOfflinePlayer(UUID.fromString((String) o)));

        return council;
    }

    public ArrayList<OfflinePlayer> getOfficers() {
        ArrayList<OfflinePlayer> officers = new ArrayList<>();
        for (Object o : getList("officers"))
            officers.add(Bukkit.getOfflinePlayer(UUID.fromString((String) o)));

        return officers;
    }

    public ArrayList<OfflinePlayer> getCitizens() {
        ArrayList<OfflinePlayer> citizens = new ArrayList<>();
        for (Object o : getList("citizens"))
            citizens.add(Bukkit.getOfflinePlayer(UUID.fromString((String) o)));

        return citizens;
    }

    public ArrayList<OfflinePlayer> getResidents() {
        ArrayList<OfflinePlayer> residents = new ArrayList<>();
        for (Object o : getList("residents"))
            residents.add(Bukkit.getOfflinePlayer(UUID.fromString((String) o)));

        return residents;
    }

    public ArrayList<OfflinePlayer> getInvites() {
        ArrayList<OfflinePlayer> invites = new ArrayList<>();
        for (Object o : getList("invites"))
            invites.add(Bukkit.getOfflinePlayer(UUID.fromString((String) o)));

        return invites;
    }

    public ArrayList<Nation> getAllies() {
        ArrayList<Nation> allies = new ArrayList<>();
        for (Object o : getList("allies")) {
            Nation nation = Nation.get((String) o);
            if (nation == null)
                removeAlly(nation);
            else
                allies.add(nation);
        }

        return allies;
    }
    
    public ArrayList<Nation> getEnemies() {
        ArrayList<Nation> enemies = new ArrayList<>();
        for (Object o : getList("enemies")) {
            Nation nation = Nation.get((String) o);
            if (nation == null)
                removeEnemy(nation);
            else
                enemies.add(nation);
        }

        return enemies;
    }

    public ArrayList<OfflinePlayer> getAllPlayers() {
        ArrayList<OfflinePlayer> members = new ArrayList<>();

        members.add(getLeader());
        members.addAll(getCouncil());
        members.addAll(getOfficers());
        members.addAll(getCitizens());
        members.addAll(getResidents());

        return members;
    }

    public NationRank getRank(OfflinePlayer player) {
        if (getLeader() == player)
            return NationRank.LEADER;
        if (getCouncil().contains(player))
            return NationRank.COUNCIL;
        if (getOfficers().contains(player))
            return NationRank.OFFICER;
        if (getCitizens().contains(player))
            return NationRank.CITIZEN;
        if (getResidents().contains(player))
            return NationRank.RESIDENT;
        return null;
    }

    public long getTimeCreated() {
        return getLong("created");
    }

    public List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();

        for (OfflinePlayer p : getAllPlayers())
            if (p.isOnline())
                players.add(p.getPlayer());

        return players;
    }

    public String getName() {
        return getString("name");
    }

    public String getDisplayName() {
        if (!getOptions().has("display_name"))
            return getName();
        else
            return getString("display_name");
    }

    public int getChunks() {
        return getInt("chunks");
    }

    public String getDescription() {
        if (!getOptions().has("description"))
            return PLUGIN.getLanguageData().getField("DEFAULT_DESCRIPTION");
        else
            return getString("description");
    }

    public void setDescription(String description) {
        getOptions().put("description", description);

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addAlly(Nation nation) {
        addToList("allies", nation.getName());
    }
    
    public void removeAlly(Nation nation) {
        removeFromList("allies", nation.getName());
    }
    
    public void addEnemy(Nation nation) {
        addToList("enemies", nation.getName());
    }
    
    public void removeEnemy(Nation nation) {
        removeFromList("enemies", nation.getName());
    }

    public void invite(OfflinePlayer player) {
        addToList("invites", player.getName());
    }

    public void uninvite(OfflinePlayer player) {
        removeFromList("invites", player.getName());
    }

    public void promote(OfflinePlayer player) {
        switch (getRank(player)) {
        case CITIZEN:
            setRank(player, NationRank.OFFICER);
            break;
        case COUNCIL:
            setRank(player, NationRank.LEADER);
            break;
        case LEADER:
            break;
        case OFFICER:
            setRank(player, NationRank.COUNCIL);
            break;
        case RESIDENT:
            setRank(player, NationRank.CITIZEN);
            break;
        }
    }

    public void demote(OfflinePlayer player) {
        switch (getRank(player)) {
        case CITIZEN:
            setRank(player, NationRank.RESIDENT);
            break;
        case COUNCIL:
            setRank(player, NationRank.OFFICER);
            break;
        case LEADER:
            break;
        case OFFICER:
            setRank(player, NationRank.CITIZEN);
            break;
        case RESIDENT:
            kick(player);
            break;
        }
    }

    public void setRank(OfflinePlayer player, NationRank rank) {
        if (rank == NationRank.LEADER) {
            addToList("council", player.getName());
        }

        // remove
        switch (getRank(player)) {
        case CITIZEN:
            removeFromList("citizen", player.getName());
            break;
        case COUNCIL:
            removeFromList("citizen", player.getName());
            break;
        case OFFICER:
            removeFromList("citizen", player.getName());
            break;
        case RESIDENT:
            removeFromList("citizen", player.getName());
            break;
        case LEADER:
            setLeader(null);
            addToList("council", player.getName());
            break;
        }

        // add
        switch (rank) {
        case CITIZEN:
            addToList("citizens", player.getName());
            break;
        case COUNCIL:
            addToList("council", player.getName());
            break;
        case OFFICER:
            addToList("officers", player.getName());
            break;
        case RESIDENT:
            addToList("residents", player.getName());
            break;
        case LEADER:
            setLeader(player);
            removeFromList("council", player.getName());
            break;
        }
    }

    public void setLeader(OfflinePlayer player) {
        set("leader", player.getName());
    }

    public void join(OfflinePlayer player) {
        new OfflinePlayerWrapper(player).setNation(this);

        addToList("residents", player.getName());
        
        if (player.isOnline())
            player.getPlayer().sendMessage(PLUGIN.getLanguageData().getFieldWithPrefix("JOIN_NOTIFY", getName()));

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kick(OfflinePlayer player) {
        switch (getRank(player)) {
        case CITIZEN:
            removeFromList("citizen", player.getName());
            break;
        case COUNCIL:
            removeFromList("citizen", player.getName());
            break;
        case OFFICER:
            removeFromList("citizen", player.getName());
            break;
        case RESIDENT:
            removeFromList("citizen", player.getName());
            break;
        case LEADER:
            setLeader(null);
            break;
        }

        if (player.isOnline())
            player.getPlayer().sendMessage(PLUGIN.getLanguageData().getFieldWithPrefix("KICK_NOTIFY", getName()));

        new OfflinePlayerWrapper(player).setNation(null);
    }

    public void disband() {
        notifyAll(PLUGIN.getLanguageData().getFieldWithPrefix("DISBAND_NOTIFY"));

        unclaimAll();

        for (OfflinePlayer p : getAllPlayers())
            new OfflinePlayerWrapper(p).setNation(null);

        getFile().delete();
    }

    public void addChunks(int chunks) {
        set("chunks", getInt("chunks") + chunks);
    }

    public void removeChunks(int chunks) {
        set("chunks", getInt("chunks") - chunks);
    }

    public void unclaimAll() {
        Set<WorldRegion> regions = new HashSet<>();

        // prevent concurrent modification as regions delete themselves from nation data
        for (Object region : getList("regions"))
            regions.add(WorldRegion.getAndUseOnce((String) region));

        for (WorldRegion region : regions)
            set("chunks", getInt("chunks") + region.removeAllClaims(this));
    }

    public void notifyAll(String message) {
        for (Player p : getOnlinePlayers())
            p.sendMessage(message);
    }

    void addRegion(String name) {
        addToList("regions", name);
    }

    void removeRegion(String name) {
        removeFromList("regions", name);
    }

    @Override
    public String toString() {
        return getName();
    }

    public static Nation create(String name, OfflinePlayer leader) throws NationExistsException {
        Nation nation = new Nation(name, true, getDefaults(name, leader));

        if (!nation.isNew())
            throw new NationExistsException();

        new OfflinePlayerWrapper(leader).setNation(nation);

        return nation;
    }

    public static Nation get(String name) {
        Nation nation = new Nation(name, false, null);

        if (nation.isNew())
            return null;

        return nation;
    }

    public static ArrayList<String> stringUUIDs(ArrayList<OfflinePlayer> players) {
        ArrayList<String> uuids = new ArrayList<>();

        for (OfflinePlayer p : players)
            uuids.add(p.getUniqueId().toString());

        return uuids;
    }

    public static class NationExistsException extends Exception {

        private static final long serialVersionUID = 8490784938196191551L;

    }

}
