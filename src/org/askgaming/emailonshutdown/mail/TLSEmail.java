package org.askgaming.emailonshutdown.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.askgaming.emailonshutdown.Main;
import org.askgaming.emailonshutdown.utils.Logs;

public class TLSEmail {
	
	static Main plugin;
	public TLSEmail(Main main) {
		plugin = main;
	}
	
	public static void email(String Subject, String body) {
		
		final String fromEmail = plugin.getConfig().getString("smtp_config.user");
		final String password = plugin.getConfig().getString("smtp_config.password");
		final String toEmail = plugin.getConfig().getString("email_config.to");
		
		Logs.logToFile("Smtp: TLSEmail Start");
		System.out.println("Smtp: TLSEmail Start");
		
		Properties props = new Properties();
		props.put("mail.smtp.host", plugin.getConfig().getString("smtp_config.host"));
		props.put("mail.smtp.port", plugin.getConfig().getString("smtp_config.port")); 
		props.put("mail.smtp.auth", "true"); 
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
		      
		Authenticator auth = new Authenticator() {			
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		
		Session session = Session.getInstance(props, auth);
		
		System.out.println("Smtp: Session created");
		Logs.logToFile("Smtp: Session created");
		
		SendEmail.send(session, toEmail, Subject, body);
			
	}
}
