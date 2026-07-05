package ar.edu.utn.frba.dds.modelos.medioscontacto;

import ar.edu.utn.frba.dds.Notificacion;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class Mail implements MedioContacto {
  private String direccionMail;

  public Mail(String direccionCasilla) {
    if (direccionCasilla == null || !direccionCasilla.contains("@")) {
      throw new IllegalArgumentException("Dirección de correo electrónico inválida o nula.");
    }
    this.direccionMail = direccionCasilla;
  }

  @Override
  public String getMedioContacto() {
    return direccionMail;
  }

  @Override
  public void contactar(Notificacion notificacion) {
    notificacion.setDestinatario(this.getMedioContacto());
    try {
      enviarEmail(notificacion.getDestinatario(), notificacion.getMensaje());
      notificacion.marcarComoCompletada();
    } catch (MessagingException e) {
      notificacion.marcarComoFallida();
      throw new RuntimeException("Error al enviar email a " + direccionMail, e);
    }
    notificacion.marcarComoCompletada();
  }

  private void enviarEmail(String destinatario, String cuerpo) throws MessagingException {
    String host     = System.getenv("MAIL_HOST");
    String port     = System.getenv("MAIL_PORT");
    String username = System.getenv("MAIL_USERNAME");
    String password = System.getenv("MAIL_PASSWORD");
    String from     = System.getenv("MAIL_FROM");

    Properties props = new Properties();
    props.put("mail.smtp.auth",            "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host",            host);
    props.put("mail.smtp.port",            port);

    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(from));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
    message.setSubject("Notificación DonaTrack");
    message.setText(cuerpo);

    Transport.send(message);
  }

  }

