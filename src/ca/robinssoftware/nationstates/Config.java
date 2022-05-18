package ca.robinssoftware.nationstates;

import static ca.robinssoftware.nationstates.NationstatesPlugin.PLUGIN;

import java.io.File;

import org.json.JSONObject;

public class Config extends JSONFile {

    private static final JSONObject DEFAULTS;
    
    static {
        JSONObject defaults = new JSONObject();
        
        defaults.put("new_nation_chunks", 120);
        
        DEFAULTS = defaults;
    }
    
    Config() {
        super(new File(PLUGIN.getDataFolder() + "/config.json"), true, DEFAULTS);
    }
    
    public int getNewNationChunks() {
        return getInt("new_nation_chunks");
    }
}
