package net.acampadas21.forumwhitelist;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import lib.PatPeter.SQLibrary.MySQL;

public class ForumWhitelist extends JavaPlugin {

    final ForumWhitelist plugin = this;
    public static final Logger logger = Logger.getLogger("Minecraft");
    public final ForumPlayerListener playerListener = new ForumPlayerListener(this);
    public static FileConfiguration config;
    public static MySQL mysqlcon;    
    
    @Override
    public void onDisable() {
        ForumWhitelist.logger.log(Level.INFO, plugin.getDescription().getName() + " disabled");

    }

    @Override
    public void onEnable() {
        config = this.getConfig();
        ForumWhitelist.mysqlcon = new MySQL(logger, config.getString("mysql.prefix"), config.getString("mysql.hostname"), String.valueOf(config.getInt("mysql.port")), config.getString("mysql.database"), config.getString("mysql.user"), String.valueOf(config.getInt("mysql.password")));
        if (dbCheck()) {
            ForumWhitelist.logger.log(Level.INFO, plugin.getDescription().getName() + " enabled");
        } else {
            ForumWhitelist.logger.log(Level.WARNING, "Whitelist file not found.");
            this.getPluginLoader().disablePlugin(this);
        }
    }
    
    public boolean dbCheck() {
    	ForumWhitelist.mysqlcon.open();
    	if(ForumWhitelist.mysqlcon.checkConnection()){
    		ForumWhitelist.mysqlcon.close();
    		return true;
    	} else {
    		ForumWhitelist.mysqlcon.close();
    		return false;
    	}
    }
}
