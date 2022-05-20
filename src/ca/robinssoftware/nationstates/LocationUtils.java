package ca.robinssoftware.nationstates;

import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtils {

    public static Location getChunkLocation(World world, int x, int z) {
        return new Location(world, (double) (x << 4), 0, (double) (z << 4));
    }

    public static String stringify(World world, int x, int z) {
        return world.getName() + "/" + (int) Math.floor(x / 32) + "_" + (int) Math.floor(z / 32);
    }

    public static String stringify(Location location) {
        return stringify(location.getWorld(), (int) Math.floor(location.getChunk().getX() / 32),
                (int) Math.floor(location.getChunk().getZ() / 32));
    }
    
    public HashSet<ChunkCoordinate> getChunksInSquareRadius(int radius, ChunkCoordinate chunk) {
        HashSet<ChunkCoordinate> chunks = new HashSet<>();
        
        for (int x = -radius + chunk.getX(); x <= radius + chunk.getX(); x++)
            for (int z = -radius + chunk.getZ(); z <= radius + chunk.getZ(); x++)
                chunks.add(new ChunkCoordinate(x, z));
        
        return chunks;
    }

}
