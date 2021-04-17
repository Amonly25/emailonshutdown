package org.askgaming.emailonshutdown.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.askgaming.emailonshutdown.Main;


public class Logs {
	
	static Main plugin;
	public Logs(Main main) {
		plugin = main;
	}

	public static void logToFile(String message) {
		 
        try {
        	
            File data = new File(plugin.getDataFolder(), "logs");
            Path path = Paths.get(plugin.getDataFolder() + "/logs");
            
            if (!data.exists()) {
            	Files.createDirectory(path);
            }
            
            String month = java.time.LocalDate.now().getMonth().toString().toLowerCase();
            
            File saveTo = new File(plugin.getDataFolder(), "/logs/" + month + ".yml");
            
            if (!saveTo.exists()) {
                saveTo.createNewFile();
            }
 
            String date = java.time.LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
            
            FileWriter fw = new FileWriter(saveTo, true);
            PrintWriter pw = new PrintWriter(fw); 
            pw.println(date + " " + message);
            pw.flush();
            pw.close();
 
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
