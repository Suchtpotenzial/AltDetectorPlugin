package me.suchtpotenzial.altdetector;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class Listeners implements Listener {
    private AltDetector plugin;

    public Listeners(AltDetector plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, (Plugin)plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("altdetector.exempt"))
            return;
        String ip = player.getAddress().getAddress().getHostAddress();
        String uuid = player.getUniqueId().toString();
        String name = player.getName();
        this.plugin.dataStore.addUpdateIp(ip, uuid, name);
        if (this.plugin.saveInterval == 0L)
            this.plugin.dataStore.saveIpDataConfig();
        String altString = this.plugin.dataStore.getFormattedAltString(name,
                ip,
                uuid,
                this.plugin.config.getJoinPlayer(),
                this.plugin.config.getJoinPlayerList(),
                this.plugin.config.getJoinPlayerSeparator());
        if (altString != null) {
            this.plugin.getLogger().info(altString.replaceAll("&[0123456789AaBbCcDdEeFfKkLlMmNnOoRr]", ""));
            String notifyString = ChatColor.translateAlternateColorCodes('&', String.valueOf(this.plugin.config.getJoinPlayerPrefix()) + altString);
            for (Player p : this.plugin.getServer().getOnlinePlayers()) {
                if (p.hasPermission("altdetector.notify"))
                    p.sendMessage(notifyString);
            }
        }
    }
}
