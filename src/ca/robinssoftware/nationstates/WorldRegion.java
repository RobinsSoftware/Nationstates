package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.json.JSONObject;

public class WorldRegion extends JSONFile {

    static HashMap<String, HashMap<String, WorldRegion>> loaded;

    WorldRegion(Location location, boolean cache) {
        super(new File(PLUGIN.getDataFolder() + "/region/" + location.getWorld().getName()
                + "/" + stringify(location)), true);
        
        if(!cache)
            return;
        
        if (loaded.get(location.getWorld().getName()) == null)
            loaded.put(location.getWorld().getName(), new HashMap<>());
        
        loaded.get(location.getWorld().getName()).put(stringify(location), this);
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(PLUGIN, new Runnable() {

            @Override
            public void run() {
                loaded.get(location.getWorld().getName()).remove(stringify(location));
            }
            
        }, 300 * 20); 
    }
    
    static String stringify(Location location) {
        return Math.floor(location.getChunk().getX() / 32) + "." + Math.floor(location.getChunk().getZ()) / 32;
    }
    
    public WorldRegion getAndUseOnce(Location location) {
        return new WorldRegion(location, false);
    }

    public WorldRegion get(Location location) {
        if (loaded.get(stringify(location)) != null) 
            return loaded.get(location.getWorld().getName()).get(stringify(location) + "," + location.getChunk().getZ());
        return new WorldRegion(location, true);
    }

    public Nation getOwner(Chunk chunk) {
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

    public void setOwner(Chunk chunk, Nation nation) {
        if (getObject(chunk.getX() + "") == null)
            getOptions().put(chunk.getX() + "", new JSONObject()).put(chunk.getZ() + "", nation.getName());
        else
            getObject(chunk.getX() + "").put(chunk.getZ() + "", nation.getName());
    }
}
