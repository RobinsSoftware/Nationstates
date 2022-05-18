package ca.robinssoftware.nationstates;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class NationstatesPlugin extends JavaPlugin {
    
    static NationstatesPlugin PLUGIN = null;
    
    private Language language;
    private Config config;
    
    public NationstatesPlugin() {
        PLUGIN = this;
        language = new Language();
        config = new Config();
    }
    
    public Language getLanguageData() {
        return language;
    }
    
    @Override
    public void onEnable() {
        getCommand("nation").setExecutor(new DefaultCommand());
        getCommand("nation").setTabCompleter(new DefaultTabCompleter());
        getCommand("nation").setAliases(List.of("n"));
        
        getCommand("nationstates").setExecutor(new PluginCommand());
        getCommand("nationstates").setTabCompleter(new PluginTabCompleter());
        getCommand("nationstates").setAliases(List.of("ns"));
    }
    
    public Config getPluginConfig() {
        return config;
    }
    
}
