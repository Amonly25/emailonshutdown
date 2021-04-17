package org.askgaming.emailonshutdown.mail;

import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.askgaming.emailonshutdown.Main;
import org.askgaming.emailonshutdown.utils.Logs;

public class SendEmail {
	
	static Main plugin;
	public SendEmail(Main main) {
		plugin = main;
	}

	public static void send(Session session, String toEmail, String subject, String body){
		
		try {
			
	      MimeMessage msg = new MimeMessage(session);

	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
	      msg.addHeader("format", "flowed");
	      msg.addHeader("Content-Transfer-Encoding", "8bit");

	      msg.setFrom(new InternetAddress(plugin.getConfig().getString("email_config.from"), plugin.getConfig().getString("email_config.from")));

	      msg.setReplyTo(InternetAddress.parse(plugin.getConfig().getString("email_config.from"), false));

	      msg.setSubject(subject, "UTF-8");

	      msg.setText(body, "UTF-8");

	      msg.setSentDate(new Date());

	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
	      
	      System.out.println("Smtp: Message is ready");
	      Logs.logToFile("Smtp: Message is ready");
	      
    	  Transport.send(msg);  

	      System.out.println("Smtp: EMail Sent Successfully!!");
	      Logs.logToFile("Smtp: EMail Sent Successfully!!");
	    }
		
	    catch (Exception e) {
	      e.printStackTrace();
	      Logs.logToFile("Error " + e.toString());
	    }
	}
}