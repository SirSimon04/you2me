/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.util.*;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.*;

/**
* <h1>Die Java-Klasse zum Senden der Emails.</h1>
* <p>
* Diese Klasse versendet die Emails über den konfigurierbaren SMTP-Servers.
*/
@Stateless
@LocalBean
public class Emailservice {
    
    /**
    * Diese Methode setzt die einzelnen Daten 
    * @param args Übergibt die Daten vom Email-Webserver
    */
    public void setdaten() {  //args[] to,subjekt,body
        //setzte Daten
        String from = "simi@engelnetz.de";
        String pass = "Simoneng2S"; 
        String to = "simi@engelnetz.de";
        String subject = "Test";
        String body = "";
        String host = "smtp.ionos.de";
        String port = "587";
        sendFromGMail(host, port, from, pass, to, subject, body);
    }
    
    /**
    * Diese Methode sendet die Email.
    * @param host Der SMTP-Server.
    * @param port Der Port des SMTP-Servers.
    * @param from Der Sender der Email.
    * @param passwort Das Passwort des Senders.
    * @param to Der Empfänger der Nachricht.
    * @param subjekt Das Betreff der Nachricht.
    * @param body Der Inhalt der Nachricht.
    */
    private static void sendFromGMail(String host,String port, String from, String passwort, String to, String subject, String body) {
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", passwort);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, passwort);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
