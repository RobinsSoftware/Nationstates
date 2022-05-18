package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.json.JSONObject;

public class WorldRegion extends JSONFile {

    final static HashMap<String, WorldRegion> loaded = new HashMap<>();
    
    final String name;
    final int x, z;

    WorldRegion(Location location, boolean cache) {
        super(new File(PLUGIN.getDataFolder() + "/region/" + stringify(location)), true);
        this.name = stringify(location);

        x = (int) Math.floor(location.getChunk().getX() / 32);
        z = (int) Math.floor(location.getChunk().getZ() / 32);

        if (!cache)
            return;

        loaded.put(name, this);

        Bukkit.getScheduler().scheduleSyncDelayedTask(PLUGIN, new Runnable() {

            @Override
            public void run() {
                loaded.remove(name);
            }

        }, 300 * 20);
    }

    static String stringify(Location location) {
        return location.getWorld().getName() + "/" + Math.floor(location.getChunk().getX() / 32) + "."
                + Math.floor(location.getChunk().getZ()) / 32;
    }
    
    public static WorldRegion getAndUseOnce(Location location) {
        return new WorldRegion(location, false);
    }
    
    public static WorldRegion getAndUseOnce(String location) {
        String[] split = location.split("/");

        return get(new Location(Bukkit.getWorld(split[0]), (double) Integer.parseInt(split[1].split(".")[0]) * 16, 0,
                (double) Integer.parseInt(split[1].split(".")[1]) * 16));
    }

    public static WorldRegion get(Location location) {
        if (loaded.get(stringify(location)) != null)
            return loaded.get(stringify(location));
        return new WorldRegion(location, true);
    }

    public Nation getOwner(Location location) {
        Chunk chunk = location.getChunk();

        if (getObject(chunk.getX() + "") == null)
            return null;

        if (!getObject(chunk.getX() + "").has(chunk.getZ() + ""))
            return null;

        if (Nation.get(getObject(chunk.getX() + "").getString(chunk.getZ() + "")) == null) {
            getObject(chunk.getX() + "").remove(chunk.getZ() + "");
            return null;
        }

        return Nation.get(getObject(chunk.getX() + "").getString(chunk.getZ() + ""));
    }

    public void removeOwner(Location location) {
        Chunk chunk = location.getChunk();

        if (getOwner(location) != null && getClaims(getOwner(location)) == 0)
            getOwner(location).removeRegion(stringify(location));

        if (getObject(chunk.getX() + "") == null)
            getOptions().put(chunk.getX() + "", new JSONObject()).remove(chunk.getZ() + "");
        else
            getObject(chunk.getX() + "").remove(chunk.getZ() + "");

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOwner(Location location, Nation nation) {
        if (nation == null) {
            removeOwner(location);
            return;
        }

        Chunk chunk = location.getChunk();

        if (getOwner(location) != null && getClaims(getOwner(location)) == 0)
            getOwner(location).removeRegion(stringify(location));

        nation.addRegion(stringify(location));

        if (getObject(chunk.getX() + "") == null)
            getOptions().put(chunk.getX() + "", new JSONObject()).put(chunk.getZ() + "", nation.getName());
        else
            getObject(chunk.getX() + "").put(chunk.getZ() + "", nation.getName());

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getClaims(Nation nation) {
        int found = 0;
        int incrementX = x * 32;

        for (int i = 0; i < 16; i++) {
            if (getObject((incrementX + i) + "") == null)
                break;

            for (String key : getObject((incrementX + i) + "").keySet())
                if (getObject((x + i) + "").getString(key) == nation.getName())
                    found++;
        }

        return found;
    }

    public int removeAllClaims(Nation nation) {
        int found = 0;
        int incrementX = x * 32;

        for (int i = 0; i < 16; i++) {
            if (getObject((incrementX + i) + "") == null)
                break;

            for (String key : getObject((incrementX + i) + "").keySet())
                if (getObject((x + i) + "").getString(key) == nation.getName()) {
                    found++;
                    getObject((x + i) + "").remove(key);
                }
        }
        
        nation.removeRegion(name);

        return found;
    }
}
