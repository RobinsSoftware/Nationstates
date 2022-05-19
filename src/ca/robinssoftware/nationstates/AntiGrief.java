package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AntiGrief implements Listener {

    private HashSet<UUID> admins = new HashSet<>();
    private HashMap<UUID, AntiGriefValidation> loaded = new HashMap<>();

    AntiGrief() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PLUGIN, new Runnable() {

            @Override
            public void run() {
                loaded.clear();
            }

        }, 50, 50);
    }

    private static class AntiGriefValidation {

        AntiGriefValidation(int x, int z, String owner) {
            this.x = x;
            this.z = z;
            this.owner = owner;
        }

        int x, z;
        String owner;
        boolean allowed;
        
    }

    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        AntiGriefValidation saved = loaded.get(e.getPlayer().getUniqueId());
        Location location = e.getClickedBlock().getLocation();

        if (saved != null && saved.x == location.getChunk().getX() && saved.z == location.getChunk().getZ()) {
            if (!saved.allowed) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(PLUGIN.getLanguageData().getField("CLAIM_OWNED", saved.owner));
            }

            return;
        }

        Nation owner = WorldRegion.get(location).getOwner(location);
        AntiGriefValidation validation = new AntiGriefValidation(location.getChunk().getX(), location.getChunk().getZ(),
                owner.getDisplayName());

        if (owner != null && owner != new OfflinePlayerWrapper(e.getPlayer()).getNation()
                && !admins.contains(e.getPlayer().getUniqueId()))
            validation.allowed = false;
        else
            validation.allowed = true;

        loaded.put(e.getPlayer().getUniqueId(), validation);

        if (!validation.allowed) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(PLUGIN.getLanguageData().getField("CLAIM_OWNED", validation.owner));
        }
    }

}
