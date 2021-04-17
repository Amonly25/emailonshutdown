package org.askgaming.emailonshutdown;

import org.askgaming.emailonshutdown.mail.SSLEmail;
import org.askgaming.emailonshutdown.mail.TLSEmail;
import org.askgaming.emailonshutdown.utils.Logs;
import org.askgaming.emailonshutdown.utils.TpsChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	
	static Main plugin;
	public Commands(Main main) {
		plugin = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if (msg.equals("smtp")) {
			
			if (args.length == 1 ) {
			
				if (sender.hasPermission("smtp.admin") == true) {
				
					if (args[0].equalsIgnoreCase("sendtest")) {
						
						if (sender instanceof Player) {
							sender.sendMessage("§aPlease, see the console for the process.");
						}
					
						String subject = "It Works!", text = "This is an email sent by EmailOnShutdown.";
						Logs.logToFile("Smtp: Test email sent");
						
						if (plugin.getConfig().getString("smtp_config.mode").toLowerCase().equals("ssl")) {
							SSLEmail.email(subject, text);
						}
						if (plugin.getConfig().getString("smtp_config.mode").toLowerCase().equals("tls")) {
							TLSEmail.email(subject, text);
						}
												
						return true;
					}
					if (args[0].equalsIgnoreCase("showtps")) {
						
						sender.sendMessage("Tps: " + String.valueOf(TpsChecker.getTPS()));
						
						return true;
					}
					if (args[0].equalsIgnoreCase("reload")) {
						
						plugin.reloadConfig();
						sender.sendMessage("§aConfig reloaded.");
						return true;
					}
				}
			}
		}
		sender.sendMessage("§cCorrect Usage: /smtp §7{sendtest§c|§7showtps}");
		return false;
	}
}
