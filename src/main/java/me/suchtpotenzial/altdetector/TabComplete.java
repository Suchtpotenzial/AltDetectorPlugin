package me.suchtpotenzial.altdetector;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabComplete implements TabCompleter {
    private AltDetector plugin;

    public TabComplete(AltDetector plugin) {
        this.plugin = plugin;
    }

    private boolean hasPermission(CommandSender sender, String permission) {
        if (!(sender instanceof org.bukkit.entity.Player))
            return true;
        return sender.hasPermission(permission);
    }

    private List<String> filterList(List<String> list, String str) {
        List<String> al = new ArrayList<>();
        for (String name : list) {
            if (str.length() == 0 || (name.length() >= str.length() && name.substring(0, str.length()).equalsIgnoreCase(str)))
                al.add(name);
        }
        return al;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("alt")) {
            List<String> argList = new ArrayList<>();
            if (args.length == 1) {
                if (hasPermission(sender, "altdetector.alt.delete"))
                    argList.add("delete");
                if (hasPermission(sender, "altdetector.alt"))
                    argList.addAll(this.plugin.dataStore.getPlayerList());
                return filterList(argList, args[0]);
            }
            if (args.length == 2 && args[0].equals("delete") && hasPermission(sender, "altdetector.alt.delete")) {
                argList.addAll(this.plugin.dataStore.getPlayerList());
                return filterList(argList, args[1]);
            }
            return argList;
        }
        return null;
    }
}
