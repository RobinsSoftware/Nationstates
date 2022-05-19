package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.io.File;
import java.io.IOException;

import org.bukkit.OfflinePlayer;

public class OfflinePlayerWrapper extends JSONFile {

    final OfflinePlayer player;
    Nation nation;

    public OfflinePlayerWrapper(OfflinePlayer player) {
        super(new File(PLUGIN.getDataFolder() + "/player/" + player.getUniqueId() + ".json"), true);
        this.player = player;
        load();
    }
    

    private void load() {
        if (getOptions().has("nation"))
            nation = Nation.get(getString("nation"));
        else
            nation = null;
        
        // verify nationality with nation
        if (nation != null && !nation.containsPlayer(player)) {
            getOptions().remove("nation");
            nation = null;

            try {
                save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Nation getNation() {
        return nation;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
        
        if (nation == null) {
            getOptions().remove("nation");
        } else
            getOptions().put("nation", nation.getName());

        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
