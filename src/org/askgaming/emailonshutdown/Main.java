package org.askgaming.emailonshutdown;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.askgaming.emailonshutdown.events.QuitEvent;
import org.askgaming.emailonshutdown.mail.SSLEmail;
import org.askgaming.emailonshutdown.mail.SendEmail;
import org.askgaming.emailonshutdown.mail.TLSEmail;
import org.askgaming.emailonshutdown.utils.JarUtils;
import org.askgaming.emailonshutdown.utils.Logs;
import org.askgaming.emailonshutdown.utils.TabComplet;
import org.askgaming.emailonshutdown.utils.TpsChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	public void onEnable() {
		
		this.saveDefaultConfig();
		this.getServer().getPluginCommand("smtp").setExecutor(new Commands(this));
		this.getServer().getPluginCommand("smtp").setTabCompleter(new TabComplet(this));
		this.getServer().getPluginManager().registerEvents(new QuitEvent(this), this);
		
		try {
			
			File lib = new File(getDataFolder(), "library");
			Path path = Paths.get(getDataFolder() + "/library");
			
			if (!lib.exists()) {
				Files.createDirectories(path);
			}
			
			final File mailLib = new File(getDataFolder(),  "/library/javax.mail.jar");
			
			if (!mailLib.exists()) {
				 JarUtils.extractFromJar(mailLib.getName(), mailLib.getAbsolutePath());
            }
            addClassPath(JarUtils.getJarUrl(mailLib));
         
		} catch (final Exception e) {
	    	e.printStackTrace();
		}	
		
		new SSLEmail(this);
		new TLSEmail(this);
		new SendEmail(this);
		new Logs(this);
		
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TpsChecker(), 100L, 1L);
				
		Bukkit.getServer().getScheduler().runTaskTimer(this, new Runnable(){

			@Override
			public void run() {
				
				String subject = getConfig().getString("reasons.low_tps.subject");
				String text = getConfig().getString("reasons.low_tps.text");
				
				if (TpsChecker.getTPS() < getConfig().getDouble("reasons.low_tps.at")) {
					
					Logs.logToFile("Smtp: Low tps email sent");
					
					if (getConfig().getString("smtp_config.mode").toLowerCase().equals("ssl")) {
						SSLEmail.email(subject, text);
					}
					if (getConfig().getString("smtp_config.mode").toLowerCase().equals("tls")) {
						TLSEmail.email(subject, text);
					}
				}				
			}			
        }, 12000, (getConfig().getLong("reasons.low_tps.how_often") * 20 * 60));				
    }	

	public void onDisable() {
		
		String subject = getConfig().getString("reasons.shutdown/restart.subject");
		String text = getConfig().getString("reasons.shutdown/restart.text");
		
		if (getConfig().getBoolean("reasons.shutdown/restart.enable") == true) {
			
			Logs.logToFile("Smtp: Shutdown/restart email sent");
			
			if (getConfig().getString("smtp_config.mode").toLowerCase().equals("ssl")) {
				SSLEmail.email(subject, text);
			}
			if (getConfig().getString("smtp_config.mode").toLowerCase().equals("tls")) {
				TLSEmail.email(subject, text);
			}
		}
	}
	
    private void addClassPath(final URL url) throws IOException {
    	
        final URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        final Class<URLClassLoader> sysclass = URLClassLoader.class;
        
        try {
        	
            final Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { url });
            
        } catch (final Throwable t) {
        	
            t.printStackTrace();
            throw new IOException("Error adding " + url + " to system classloader");
        }
    }	
}
