package me.suchtpotenzial.altdetector;

import org.bukkit.plugin.java.JavaPlugin;

public class AltDetector extends JavaPlugin {
    long expirationTime = 60L;

    long saveInterval = 1L;

    Config config;

    DataStore dataStore;

    Listeners listeners;

    public void onEnable() {
        this.config = new Config(this);
        saveDefaultConfig();
        this.config.updateConfig();
        this.expirationTime = this.config.getExpirationTime();
        this.saveInterval = this.config.getSaveInterval();
        if (this.saveInterval > 0L) {
            getLogger().info("Database save interval " + this.saveInterval + " minute" + ((this.saveInterval == 1L) ? "." : "s."));
        } else {
            this.saveInterval = 0L;
            getLogger().info("Saving to database as each player connects.");
        }
        this.dataStore = new DataStore(this);
        this.dataStore.saveDefaultConfig();
        this.dataStore.reloadIpDataConfig();
        int entriesRemoved = this.dataStore.purge("");
        this.dataStore.saveIpDataConfig();
        this.dataStore.generatePlayerList();
        getLogger().info(String.valueOf(entriesRemoved) + " record" + ((entriesRemoved == 1) ? "" : "s") + " removed, expiration time " + this.expirationTime + " days.");
        this.listeners = new Listeners(this);
        getCommand("alt").setExecutor(new Commands(this));
        getCommand("alt").setTabCompleter(new TabComplete(this));
        int pluginId = 4862;
    }

    public void onDisable() {
        if (this.saveInterval > 0L)
            this.dataStore.saveIpDataConfig();
    }
}
