package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.io.File;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.json.JSONObject;

public class WorldRegion extends JSONFile {
    
    WorldRegion(Location location) {
        super(new File(PLUGIN.getDataFolder() + "/region/" + Math.floor(location.getChunk().getX() / 32) + "." + Math.floor(location.getChunk().getZ()) / 32), true);
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
