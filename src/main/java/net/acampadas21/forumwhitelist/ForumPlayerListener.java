package net.acampadas21.forumwhitelist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.ResultSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.logging.Level;
import java.sql.SQLException;

public class ForumPlayerListener implements Listener {

    public static ForumWhitelist plugin;

    public ForumPlayerListener(ForumWhitelist instance) {
        plugin = instance;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!playerRegistered(event.getPlayer())) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ForumWhitelist.config.getString("user_denied.message"));
        }
    }

    public boolean playerRegistered(Player p) {
    	boolean reg = false;
        try {
            ForumWhitelist.mysqlcon.open();
            ResultSet rs = ForumWhitelist.mysqlcon.query("SELECT `real_name` FROM `"+ForumWhitelist.config.getString("mysql.table")+"` WHERE `real_name` LIKE '"+p.getName()+"' AND `id_group` <> '9'");
            ForumWhitelist.mysqlcon.close();
            rs.next();
            if(rs.getString("real_name").equalsIgnoreCase(p.getName())) { 
            	reg = true;
            }
        } catch (SQLException ex) {
        	ForumWhitelist.logger.log(Level.SEVERE, "["+plugin.getDescription().getName()+"] MySQL Error! The error reported is "+ex.getMessage()+" . The cause seems to be: "+ex.getCause());
        }
        if(!reg){
        	if (ForumWhitelist.f.exists()){
        		BufferedReader b;
				try {
					b = new BufferedReader(new FileReader(ForumWhitelist.f));
					String linea;
                	while((linea = b.readLine())!=null){
                    	if(p.getName().equals(linea)) {
                    		reg = true;
                    		p.sendMessage("No estas verificado en el foro. El dia 19/02/12 no podras acceder a menos que verifiques");
                    	}
                	}
                	b.close();
				} catch (Exception e) {
					ForumWhitelist.logger.log(Level.SEVERE, "["+plugin.getDescription().getName()+"] Error on File Backend while reading.");
				}
        	}
        }
        return reg;
    }
}