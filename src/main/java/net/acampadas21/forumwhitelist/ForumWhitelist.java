package net.acampadas21.forumwhitelist;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import lib.PatPeter.SQLibrary.MySQL;

public class ForumWhitelist extends JavaPlugin {

    final ForumWhitelist plugin = this;
    public static final Logger logger = Logger.getLogger("Minecraft");
    public ForumPlayerListener playerListener;
    public static FileConfiguration config;
    public static MySQL mysqlcon;
    public static File f;
    
    @Override
    public void onDisable() {
        ForumWhitelist.logger.log(Level.INFO, plugin.getDescription().getName() + " disabled");

    }

    @Override
    public void onEnable() {
    	f = new File(config.getString("user_denied.file"));
    	if (f.exists()){
    		ForumWhitelist.logger.log(Level.INFO, plugin.getDescription().getName() + ": Backend file loaded.");
    	}
        config = this.getConfig();
        ForumWhitelist.mysqlcon = new MySQL(logger, config.getString("mysql.prefix"), config.getString("mysql.hostname"), String.valueOf(config.getInt("mysql.port")), config.getString("mysql.database"), config.getString("mysql.user"), String.valueOf(config.getInt("mysql.password")));
        if (dbCheck()) {
            ForumWhitelist.logger.log(Level.INFO, plugin.getDescription().getName() + " enabled");
            playerListener = new ForumPlayerListener(this);
        } else {
            ForumWhitelist.logger.log(Level.WARNING, "Error connecting to mysql. All users allowed.");
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
