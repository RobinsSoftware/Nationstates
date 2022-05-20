package ca.robinssoftware.nationstates;

import org.bukkit.Location;

public class ChunkCoordinate {
    
    private int x, z;
    
    public ChunkCoordinate(int x, int z) {
        this.x = x;
        this.z = z;
    }
    
    public ChunkCoordinate(Location location) {
        this.x = (int) Math.floor(location.getChunk().getX() / 32);
        this.z = (int) Math.floor(location.getChunk().getZ() / 32);
    }
    
    public int getX() {
        return x;
    }
    
    public int getZ() {
        return z;
    }
    
}
