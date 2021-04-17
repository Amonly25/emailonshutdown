package org.askgaming.emailonshutdown.utils;

import java.util.ArrayList;
import java.util.List;

import org.askgaming.emailonshutdown.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabComplet implements TabCompleter {

	static Main plugin;
	public TabComplet(Main main) {
		plugin = main;
	}
	
	public List<String> arg = new ArrayList<String>();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if (arg.isEmpty()) {
			arg.add("sendtest");
			arg.add("showtps");
			arg.add("reload");
		}
		
		List<String> result = new ArrayList<String>();
		
		if (args.length == 1) {
			for (String s : arg) {
				if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(s);
				}
			}
			return result;
		}		
		return null;
	}
}
