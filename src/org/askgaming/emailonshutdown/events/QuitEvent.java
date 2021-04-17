package org.askgaming.emailonshutdown.events;

import org.askgaming.emailonshutdown.Main;
import org.askgaming.emailonshutdown.mail.SSLEmail;
import org.askgaming.emailonshutdown.mail.TLSEmail;
import org.askgaming.emailonshutdown.utils.Logs;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {
	
	static Main plugin;
	public QuitEvent(Main main) {
		plugin = main;
	}
	
	private boolean cooldown = false;
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		
		int players = Bukkit.getServer().getOnlinePlayers().size();
		
		if (plugin.getConfig().getBoolean("reasons.0players.enable") == true) {
			
			if (cooldown == false) {
				
				cooldown = true;
			
				if (players == 1) {
					
					String subject = plugin.getConfig().getString("reasons.0players.subject");
					String text = plugin.getConfig().getString("reasons.0players.text");
					
					Logs.logToFile("Smtp: 0players email sent");
					
					if (plugin.getConfig().getString("smtp_config.mode").toLowerCase().equals("ssl")) {
						SSLEmail.email(subject, text);
					}
					if (plugin.getConfig().getString("smtp_config.mode").toLowerCase().equals("tls")) {
						TLSEmail.email(subject, text);
					}
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						
						cooldown = false;
						
					},24000);
				}
			}
		}
	}
}
