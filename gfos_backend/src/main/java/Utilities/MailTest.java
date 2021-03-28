package Utilities;


import EJB.NutzerEJB;
import Entity.Nutzer;
import java.io.IOException;
import java.util.Properties;
import javax.ejb.EJB;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailTest {
    
    @EJB
    private NutzerEJB nutzerEJB;
 
 public void send(String mailFrom, String pw, String benutzername, String mailTo, int pin) throws IOException, AddressException, MessagingException, InterruptedException {
        System.out.println("hello");

     
     
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        
        Session session = Session.getInstance(prop, new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(mailFrom, pw);
        }
    });
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailTo));
        message.setRecipients(
          Message.RecipientType.TO, InternetAddress.parse(mailTo));
        message.setSubject("Desputatio-Registrierung");

        String msg = "<h2>Hallo " + benutzername +",</h2><p>vielen Dank für deine Registrierung bei Desputatio. Wir freuen uns darüber, dass du dich bei uns anmelden willst. Um deine Registrierung abzuschließen, brauchst du lediglich noch den folgenden Verifizierungscode einzugeben:</p>"
                + "</br>" + "<h2>"+ pin +"</h2>";
        

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
     
 }
}