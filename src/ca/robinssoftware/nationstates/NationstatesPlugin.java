package ca.robinssoftware.nationstates;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class NationstatesPlugin extends JavaPlugin {
    
    static NationstatesPlugin PLUGIN = null;
    
    private Language language;
    private Config config;
    private AntiGrief antiGrief;
    
    public Language getLanguageData() {
        return language;
    }
    
    @Override
    public void onEnable() {
        PLUGIN = this;
        language = new Language();
        config = new Config();
        antiGrief = new AntiGrief();
        
        getCommand("nation").setExecutor(new DefaultCommand());
        getCommand("nation").setTabCompleter(new DefaultTabCompleter());
        getCommand("nation").setAliases(List.of("n"));
        
        getCommand("nationstates").setExecutor(new PluginCommand());
        getCommand("nationstates").setTabCompleter(new PluginTabCompleter());
        getCommand("nationstates").setAliases(List.of("ns"));
        
        getServer().getPluginManager().registerEvents(antiGrief, PLUGIN);
    }
    
    @Override
    public void onDisable() {
        PLUGIN = null;
    }
    
    public Config getPluginConfig() {
        return config;
    }
    
    public AntiGrief getAntiGrief() {
        return antiGrief;
    }
    
}
