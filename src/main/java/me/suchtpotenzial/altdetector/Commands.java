package me.suchtpotenzial.altdetector;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    private AltDetector plugin;

    public Commands(AltDetector plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("alt")) {
            Player player;
            if (sender instanceof Player && !sender.hasPermission("altdetector.alt")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.config.getAltCmdNoPerm()));
                return true;
            }
            List<String> playerList = new ArrayList<>();
            switch (args.length) {
                case 0:
                    for (Player player1 : this.plugin.getServer().getOnlinePlayers()) {
                        if (!player1.hasPermission("altdetector.exempt"))
                            playerList.add(player1.getName());
                    }
                    Collections.sort(playerList, String.CASE_INSENSITIVE_ORDER);
                    break;
                case 1:
                    player = Bukkit.getServer().getPlayerExact(args[0]);
                    if (player == null) {
                        handleOfflinePlayer(sender, args[0]);
                        return true;
                    }
                    if (!player.hasPermission("altdetector.exempt"))
                        playerList.add(player.getName());
                    break;
                case 2:
                    if (args[0].equalsIgnoreCase("delete")) {
                        if (sender instanceof Player && !sender.hasPermission("altdetector.alt.delete")) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.config.getAltCmdNoPerm()));
                            return true;
                        }
                        int entriesRemoved = this.plugin.dataStore.purge(args[1]);
                        if (entriesRemoved == 0) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(this.plugin.config.getAltCmdPlayerNotFound(), new Object[] { args[1] })));
                        } else if (entriesRemoved == 1) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(this.plugin.config.getDelCmdRemovedSingular(), new Object[] { Integer.valueOf(entriesRemoved) })));
                        } else {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(this.plugin.config.getDelCmdRemovedPlural(), new Object[] { Integer.valueOf(entriesRemoved) })));
                        }
                    } else {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.config.getAltCmdParamError()));
                    }
                    return true;
                default:
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.config.getAltCmdParamError()));
                    return true;
            }
            boolean altsFound = false;
            for (String name : playerList) {
                Player player1 = Bukkit.getServer().getPlayerExact(name);
                String ip = player1.getAddress().getAddress().getHostAddress();
                String uuid = player1.getUniqueId().toString();
                altsFound |= outputAlts(sender, name, ip, uuid);
            }
            if (!altsFound)
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.plugin.config.getAltCmdNoAlts()));
                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(this.plugin.config.getAltCmdPlayerNoAlts(), new Object[] { args[0] })));
                }
            return true;
        }
        return false;
    }

    private void handleOfflinePlayer(CommandSender sender, String playerName) {
        DataStore.PlayerDataType playerData = this.plugin.dataStore.lookupOfflinePlayer(playerName);
        if (playerData == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(this.plugin.config.getAltCmdPlayerNotFound(), new Object[] { playerName })));
        } else {
            boolean altsFound = outputAlts(sender, playerData.name, playerData.ip, playerData.uuid);
            if (!altsFound)
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', MessageFormat.format(this.plugin.config.getAltCmdPlayerNoAlts(), new Object[] { playerData.name })));
        }
    }

    private boolean outputAlts(CommandSender sender, String name, String ip, String uuid) {
        String altString = this.plugin.dataStore.getFormattedAltString(name,
                ip,
                uuid,
                this.plugin.config.getAltCmdPlayer(),
                this.plugin.config.getAltCmdPlayerList(),
                this.plugin.config.getAltCmdPlayerSeparator());
        if (altString != null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', altString));
            return true;
        }
        return false;
    }
}
