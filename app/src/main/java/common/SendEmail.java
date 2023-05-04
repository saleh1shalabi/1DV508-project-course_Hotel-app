package common;

import java.util.Properties;
import javafx.concurrent.Task;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail extends Task<String> {
  private String[] receivers;
  private String subject;
  private String body;
  private Session session = null;
  private String sender = "siliconehotel@gmail.com";
  private String password = "majsbajs";
  private String host = "smtp.gmail.com";
  private Properties properties = System.getProperties();
  
  /**
   * Constructor.
   */
  public SendEmail(String[] receivers, String subject, String body) {
    this.receivers = receivers.clone();
    this.subject = subject;
    this.body = body;

    properties.put("mail.smtp.port", "587");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    session = Session.getDefaultInstance(properties, null);
  }

  @Override
  protected String call() {
    sendEmail(receivers, subject, body);
    return "Email sent";
  }

  /**
   * Sends email to as many emails as you want.
   */
  public void sendEmail(String[] receivers, String subject, String body) {
    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(sender));

      for (String i : receivers) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(i));
      }

      message.setSubject(subject);
      message.setText(body);
      Transport transport = session.getTransport("smtp");
      transport.connect(host, sender, password);
      transport.sendMessage(message, message.getAllRecipients());
    } catch (MessagingException e) {
      Util.displayAlert("Error", "Error", "E-mail not sent.");
    }
  } 
}
