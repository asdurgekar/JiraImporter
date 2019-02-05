package com.cigniti.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailOperations {
	
	public static void SendEmail()
	{
		try {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "<>");			
			props.put("mail.smtp.port", "25");
			
			
			Authenticator auth = new SMTPAuthenticator();
			Session session = Session.getInstance(props,auth);
					
			Message message = new MimeMessage(session);
			Multipart mp = new MimeMultipart("alternative");
			BodyPart textmessage = new MimeBodyPart();
			textmessage.setText("It is a text message n");
			BodyPart htmlmessage = new MimeBodyPart();
			htmlmessage.setContent("It is a html message.", "text/html");
			mp.addBodyPart(textmessage);
			mp.addBodyPart(htmlmessage);
			message.setFrom(new InternetAddress("<>"));
			message.setRecipients(Message.RecipientType.TO,
			InternetAddress.parse("<>"));
			message.setSubject("<>");
			message.setContent(mp); 
			Transport.send(message); 
			System.out.println("Done"); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	

}
