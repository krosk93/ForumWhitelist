package net.acampadas21.forumwhitelist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class ForumPlayerListener extends PlayerListener {

    public static ForumWhitelist plugin;

    public ForumPlayerListener(ForumWhitelist instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (!playerRegistered(event.getPlayer())) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ForumWhitelist.config.getString("user_denied.message"));
        }
    }

    public boolean playerRegistered(Player p) {
        try {
            BufferedReader b = new BufferedReader(new FileReader(ForumWhitelist.f));
            boolean reg = false;
            String linea;
            while((linea = b.readLine())!=null){
                if(p.getName().equals(linea)) reg = true;
            }
            b.close();
            return reg;
        } catch (Exception ex) {
            Logger.getLogger(ForumPlayerListener.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}