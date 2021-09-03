package me.suchtpotenzial.altdetector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Config {
    private AltDetector plugin;

    public Config(AltDetector plugin) {
        this.plugin = plugin;
    }

    public long getExpirationTime() {
        return this.plugin.getConfig().getLong("expiration-time");
    }

    public long getSaveInterval() {
        return this.plugin.getConfig().getLong("save-interval");
    }

    public String getJoinPlayerPrefix() {
        return this.plugin.getConfig().getString("join-player-prefix");
    }

    public String getJoinPlayer() {
        return this.plugin.getConfig().getString("join-player");
    }

    public String getJoinPlayerList() {
        return this.plugin.getConfig().getString("join-player-list");
    }

    public String getJoinPlayerSeparator() {
        return this.plugin.getConfig().getString("join-player-separator");
    }

    public String getAltCmdPlayer() {
        return this.plugin.getConfig().getString("altcmd-player");
    }

    public String getAltCmdPlayerList() {
        return this.plugin.getConfig().getString("altcmd-player-list");
    }

    public String getAltCmdPlayerSeparator() {
        return this.plugin.getConfig().getString("altcmd-player-separator");
    }

    public String getAltCmdPlayerNoAlts() {
        return this.plugin.getConfig().getString("altcmd-playernoalts");
    }

    public String getAltCmdNoAlts() {
        return this.plugin.getConfig().getString("altcmd-noalts");
    }

    public String getAltCmdPlayerNotFound() {
        return this.plugin.getConfig().getString("altcmd-playernotfound");
    }

    public String getAltCmdParamError() {
        return this.plugin.getConfig().getString("altcmd-paramerror");
    }

    public String getAltCmdNoPerm() {
        return this.plugin.getConfig().getString("altcmd-noperm");
    }

    public String getDelCmdRemovedSingular() {
        return this.plugin.getConfig().getString("delcmd-removedsingular");
    }

    public String getDelCmdRemovedPlural() {
        return this.plugin.getConfig().getString("delcmd-removedplural");
    }

    private boolean contains(String path, boolean ignoreDefault) {
        return ((ignoreDefault ? this.plugin.getConfig().get(path, null) : this.plugin.getConfig().get(path)) != null);
    }

    public void updateConfig() {
        if (!contains("expiration-time", true))
            if (contains("expirationtime", true)) {
                this.plugin.getConfig().set("expiration-time", Long.valueOf(this.plugin.getConfig().getLong("expirationtime")));
            } else {
                this.plugin.getConfig().set("expiration-time", Long.valueOf(60L));
            }
        if (!contains("save-interval", true))
            if (contains("saveinterval", true)) {
                this.plugin.getConfig().set("save-interval", Long.valueOf(this.plugin.getConfig().getLong("saveinterval")));
            } else {
                this.plugin.getConfig().set("save-interval", Long.valueOf(1L));
            }
        if (!contains("join-player-prefix", true))
            this.plugin.getConfig().set("join-player-prefix", "&b[AltDetector] ");
        if (!contains("join-player", true))
            this.plugin.getConfig().set("join-player", "{0} may be an alt of ");
        if (!contains("join-player-list", true))
            this.plugin.getConfig().set("join-player-list", "{0}");
        if (!contains("join-player-separator", true))
            this.plugin.getConfig().set("join-player-separator", ", ");
        if (!contains("altcmd-player", true))
            this.plugin.getConfig().set("altcmd-player", "&c{0}&6 may be an alt of ");
        if (!contains("altcmd-player-list", true))
            this.plugin.getConfig().set("altcmd-player-list", "&c{0}");
        if (!contains("altcmd-player-separator", true))
            this.plugin.getConfig().set("altcmd-player-separator", "&6, ");
        if (!contains("altcmd-playernoalts", true))
            this.plugin.getConfig().set("altcmd-playernoalts", "&c{0}&6 has no known alts");
        if (!contains("altcmd-noalts", true))
            this.plugin.getConfig().set("altcmd-noalts", "&6No alts found");
        if (!contains("altcmd-playernotfound", true))
            this.plugin.getConfig().set("altcmd-playernotfound", "&4{0} not found");
        if (!contains("altcmd-paramerror", true))
            this.plugin.getConfig().set("altcmd-paramerror", "&4Must specify at most one player");
        if (!contains("altcmd-noperm", true))
            this.plugin.getConfig().set("altcmd-noperm", "&4You do not have permission for this command");
        if (!contains("delcmd-removedsingular", true))
            this.plugin.getConfig().set("delcmd-removedsingular", "&6{0} record removed");
        if (!contains("delcmd-removedplural", true))
            this.plugin.getConfig().set("delcmd-removedplural", "&6{0} records removed");
        saveConfig();
    }

    public void saveConfig() {
        try {
            File outFile = new File(this.plugin.getDataFolder(), "config.yml");
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile.getAbsolutePath()));
            writer.write("# Data expiration time in days\n");
            writer.write("expiration-time: " + this.plugin.getConfig().getLong("expiration-time") + "\n");
            writer.write("\n");
            writer.write("# Save interval in minutes (0 for immediate)\n");
            writer.write("save-interval: " + this.plugin.getConfig().getLong("save-interval") + "\n");
            writer.write("\n");
            writer.write("# Messages when player joins the server\n");
            writer.write("join-player-prefix: \"" + this.plugin.getConfig().getString("join-player-prefix").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("join-player: \"" + this.plugin.getConfig().getString("join-player").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("join-player-list: \"" + this.plugin.getConfig().getString("join-player-list").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("join-player-separator: \"" + this.plugin.getConfig().getString("join-player-separator").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("\n");
            writer.write("# Messages for alt command\n");
            writer.write("altcmd-player: \"" + this.plugin.getConfig().getString("altcmd-player").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("altcmd-player-list: \"" + this.plugin.getConfig().getString("altcmd-player-list").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("altcmd-player-separator: \"" + this.plugin.getConfig().getString("altcmd-player-separator").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("\n");
            writer.write("altcmd-playernoalts: \"" + this.plugin.getConfig().getString("altcmd-playernoalts").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("altcmd-noalts: \"" + this.plugin.getConfig().getString("altcmd-noalts").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("altcmd-playernotfound: \"" + this.plugin.getConfig().getString("altcmd-playernotfound").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("altcmd-paramerror: \"" + this.plugin.getConfig().getString("altcmd-paramerror").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("altcmd-noperm: \"" + this.plugin.getConfig().getString("altcmd-noperm").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("\n");
            writer.write("#Messages for alt delete command\n");
            writer.write("delcmd-removedsingular: \"" + this.plugin.getConfig().getString("delcmd-removedsingular").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.write("delcmd-removedplural: \"" + this.plugin.getConfig().getString("delcmd-removedplural").replaceAll("\n", "\\\\n") + "\"" + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.plugin.reloadConfig();
    }
}
