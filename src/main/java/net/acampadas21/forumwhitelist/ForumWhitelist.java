package net.acampadas21.forumwhitelist;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ForumWhitelist extends JavaPlugin {

    final ForumWhitelist plugin = this;
    public static final Logger logger = Logger.getLogger("Minecraft");
    public final ForumPlayerListener playerListener = new ForumPlayerListener(this);
    public static FileConfiguration config;
    public static File f;
    
    
    @Override
    public void onDisable() {
        ForumWhitelist.logger.log(Level.INFO, "Whitelist2 disabled");

    }

    @Override
    public void onEnable() {
        config = this.getConfig();
        f = new File(config.getString("user_denied.file"));
        if (f.exists()) {
            PluginManager pm = getServer().getPluginManager();
            pm.registerEvent(Event.Type.PLAYER_LOGIN, this.playerListener, Event.Priority.Normal, this);
            pm.registerEvent(Event.Type.PLAYER_CHAT, this.playerListener, Event.Priority.Normal, this);
            ForumWhitelist.logger.log(Level.INFO, "Whitelist2 enabled");
        } else {
            ForumWhitelist.logger.log(Level.WARNING, "Whitelist file not found.");
            this.getPluginLoader().disablePlugin(this);
        }
    }
}
