package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;
import static ca.robinssoftware.nationstates.LocationUtils.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.json.JSONObject;

public class WorldRegion extends JSONFile {

    private static final HashMap<String, WorldRegion> loaded = new HashMap<>();
    
    private final String name;
    private final int x;

    WorldRegion(Location location, boolean cache) {
        super(new File(PLUGIN.getDataFolder() + "/region/" + stringify(location) + ".json"), true);
        this.name = stringify(location);

        x = (int) Math.floor(location.getChunk().getX() / 32);

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
    
    public static WorldRegion getAndUseOnce(Location location) {
        return new WorldRegion(location, false);
    }
    
    public static WorldRegion getAndUseOnce(String location) {
        String[] split = location.split("/");

        return get(new Location(Bukkit.getWorld(split[0]), (double) (Integer.parseInt(split[1].split("_")[0]) << 4), 0,
                (double) (Integer.parseInt(split[1].split("_")[1]) << 4)));
    }

    public static WorldRegion get(Location location) {
        if (loaded.get(stringify(location)) != null)
            return loaded.get(stringify(location));
        return new WorldRegion(location, true);
    }

    public Nation getOwner(Location location) {
        Chunk chunk = location.getChunk();

        if (getJSONObject(chunk.getX() + "") == null)
            return null;

        if (!getJSONObject(chunk.getX() + "").has(chunk.getZ() + ""))
            return null;

        if (Nation.get(getJSONObject(chunk.getX() + "").getString(chunk.getZ() + "")) == null) {
            getJSONObject(chunk.getX() + "").remove(chunk.getZ() + "");
            return null;
        }

        return Nation.get(getJSONObject(chunk.getX() + "").getString(chunk.getZ() + ""));
    }

    public void removeOwner(Location location) {
        Chunk chunk = location.getChunk();

        if (getOwner(location) != null && getClaims(getOwner(location)) == 0) {
            getOwner(location).removeRegion(stringify(location));
            getOwner(location).addChunks(1);
        }

        if (getJSONObject(chunk.getX() + "") != null)
            getJSONObject(chunk.getX() + "").remove(chunk.getZ() + "");

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

        if (getOwner(location) != null) {
            getOwner(location).removeRegion(stringify(location));
            getOwner(location).addChunks(1);
        }

        nation.addRegion(stringify(location));

        if (getJSONObject(chunk.getX() + "") == null)
            getOptions().put(chunk.getX() + "", new JSONObject().put(chunk.getZ() + "", nation.getName()));
        else
            getJSONObject(chunk.getX() + "").put(chunk.getZ() + "", nation.getName());

        nation.removeChunks(1);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getClaims(Nation nation) {
        int found = 0;
        int incrementX = x << 5;

        for (int i = 0; i < 16; i++) {
            if (getJSONObject((incrementX + i) + "") == null)
                break;

            for (String key : getJSONObject((incrementX + i) + "").keySet())
                if (getJSONObject((x + i) + "").getString(key) == nation.getName())
                    found++;
        }

        return found;
    }

    public int removeAllClaims(Nation nation) {
        int found = 0;
        int minChunkX = x << 5;

        for (int i = 0; i < 16; i++) {
            if (getJSONObject((minChunkX + i) + "") == null)
                continue;

            for (String key : getJSONObject((minChunkX + i) + "").keySet())
                if (getJSONObject((minChunkX + i) + "").getString(key) == nation.getName()) {
                    found++;
                    getJSONObject((minChunkX + i) + "").remove(key);
                }
        }
        
        nation.removeRegion(name);
        
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }
}
