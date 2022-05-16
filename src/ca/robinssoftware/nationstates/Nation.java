package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.json.JSONArray;
import org.json.JSONObject;

public class Nation extends JSONFile {

    String name, description, displayName;
    long created;

    OfflinePlayer leader;
    ArrayList<OfflinePlayer> council, officers, citizens, residents, invites;

    static JSONObject getDefaults(String name, OfflinePlayer leader) {
        JSONObject obj = new JSONObject();

        obj.put("name", name);
        obj.put("created", System.currentTimeMillis());
        obj.put("leader", leader.getUniqueId().toString());
        obj.put("council", new JSONArray());
        obj.put("officers", new JSONArray());
        obj.put("citizens", new JSONArray());
        obj.put("residents", new JSONArray());
        obj.put("invites", new JSONArray());

        return obj;
    }

    private Nation(String name) {
        super(new File(PLUGIN.getDataFolder() + "/nation/" + name + ".json"));
        load();
    }

    private Nation(String name, JSONObject defaults) {
        super(new File(PLUGIN.getDataFolder() + "/nation/" + name + ".json"), true, defaults);
        load();
    }

    private void load() {
        name = getString("name");
        displayName = getString("display_name");
        description = getString("description");
        leader = Bukkit.getOfflinePlayer(UUID.fromString(getString("leader")));
        created = getLong("created");
        
        council = new ArrayList<>();
        for (Object o : getList("council"))
            council.add(Bukkit.getOfflinePlayer(UUID.fromString((String) o)));
        
        officers = new ArrayList<>();
        for (Object o : getList("officers"))
            officers.add(Bukkit.getOfflinePlayer(UUID.fromString((String) o)));
        
        citizens = new ArrayList<>();
        for (Object o : getList("citizens"))
            citizens.add(Bukkit.getOfflinePlayer(UUID.fromString((String) o)));
        
        residents = new ArrayList<>();
        for (Object o : getList("residents"))
            residents.add(Bukkit.getOfflinePlayer(UUID.fromString((String) o)));
        
        invites = new ArrayList<>();
        for (Object o : getList("invites"))
            invites.add(Bukkit.getOfflinePlayer(UUID.fromString((String) o)));
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        if(displayName == null)
            return name;
        else
            return displayName;
    }

    public String getDescription() {
        if (description == null)
            return PLUGIN.getLanguageData().get("DEFAULT_DESCRIPTION");
        else
            return description;
    }
    
    public void invite(OfflinePlayer player) {
        invites.add(player);
        writeMembers();
    }
    
    public void uninvite(OfflinePlayer player) {
        invites.remove(player);
        writeMembers();
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
            council.add(leader);
        }
        
        // remove
        switch (getRank(player)) {
        case CITIZEN:
            citizens.remove(player);
            break;
        case COUNCIL:
            council.remove(player);
            break;
        case OFFICER:
            officers.remove(player);
            break;
        case RESIDENT:
            residents.remove(player);
            break;
        case LEADER:
            leader = null;
            council.add(leader);
            break;
        }
        
        // add
        switch (rank) {
        case CITIZEN:
            citizens.add(player);
            break;
        case COUNCIL:
            council.add(player);
            break;
        case OFFICER:
            officers.add(player);
            break;
        case RESIDENT:
            residents.add(player);
            break;
        case LEADER:
            leader = player;
            council.remove(player); // if added
            break;
        }
        
        writeMembers();
    }
    
    public void join(OfflinePlayer player) {
        new OfflinePlayerWrapper(player).setNation(this);
        
        residents.add(player);
        
        writeMembers();
    }
    
    public void kick(OfflinePlayer player) {
        switch (getRank(player)) {
        case CITIZEN:
            citizens.remove(player);
            break;
        case COUNCIL:
            council.remove(player);
            break;
        case OFFICER:
            officers.remove(player);
            break;
        case RESIDENT:
            residents.remove(player);
            break;
        case LEADER:
            return;
        default:
            return;
        }
        
        new OfflinePlayerWrapper(player).setNation(null);
        
        writeMembers();
    }

    private void writeMembers() {
        getOptions().put("leader", leader.getUniqueId().toString());
        getOptions().put("council", stringUUIDs(council));
        getOptions().put("officers", stringUUIDs(officers));
        getOptions().put("citizens", stringUUIDs(citizens));
        getOptions().put("residents", stringUUIDs(residents));
        getOptions().put("invites", stringUUIDs(invites));
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean containsPlayer(OfflinePlayer player) {
        return getRank(player) == null;
    }
    
    public OfflinePlayer getLeader() {
        return leader;
    }
    
    public ArrayList<OfflinePlayer> getCouncil() {
        return council;
    }
    
    public ArrayList<OfflinePlayer> getOfficers() {
        return officers;
    }
    
    public ArrayList<OfflinePlayer> getCitizens() {
        return citizens;
    }
    
    public ArrayList<OfflinePlayer> getResidents() {
        return residents;
    }
    
    public ArrayList<OfflinePlayer> getInvites() {
        return invites;
    }
    
    public ArrayList<OfflinePlayer> getAllMembers() {
        ArrayList<OfflinePlayer> members = new ArrayList<>();
        
        members.add(leader);
        members.addAll(council);
        members.addAll(officers);
        members.addAll(citizens);
        members.addAll(residents);
        
        return members;
    }
    
    public ArrayList<String> stringUUIDs(ArrayList<OfflinePlayer> players) {
        ArrayList<String> uuids = new ArrayList<>();
        
        for (OfflinePlayer p : players)
            uuids.add(p.getUniqueId().toString());
        
        return uuids;
    }
    
    public NationRank getRank(OfflinePlayer player) {
        if (leader == player)
            return NationRank.LEADER;
        if (council.contains(player))
            return NationRank.COUNCIL;
        if (officers.contains(player))
            return NationRank.OFFICER;
        if (citizens.contains(player))
            return NationRank.CITIZEN;
        if (citizens.contains(player))
            return NationRank.RESIDENT;
        return null;
    }

    public long getTimeCreated() {
        return created;
    }

    public void setDescription(String description) {
        this.description = description;
        getOptions().put("description", description);

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Nation create(String name, OfflinePlayer leader) throws NationExistsException {
        Nation n = new Nation(name, getDefaults(name, leader));

        if (!n.isNew())
            throw new NationExistsException();

        return n;
    }

    public static Nation get(String name) {
        Nation n = new Nation(name, null);

        if (n.isNew())
            return null;

        return n;
    }

    @Override
    public String toString() {
        return name;
    }
    
    public static class NationExistsException extends Exception {

        private static final long serialVersionUID = 8490784938196191551L;

    }

}