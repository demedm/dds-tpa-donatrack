package ar.edu.utn.frba.dds.model.medioscontacto;

import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Mail implements MedioContacto {

  private final String direccionMail;

  public Mail(String direccionCasilla) {
    if (direccionCasilla == null || !direccionCasilla.contains("@")) {
      throw new IllegalArgumentException("Direccion de correo electronico invalida o nula.");
    }
    this.direccionMail = direccionCasilla;
  }

  @Override
  public String getMedioContacto() {
    return direccionMail;
  }

  @Override
  public void contactar(Notificacion notificacion) {
    notificacion.setDestinatario(direccionMail);
    try {
      enviarEmail(direccionMail, notificacion.getMensaje());
      notificacion.marcarComoCompletada();
    } catch (MessagingException e) {
      notificacion.marcarComoFallida();
      throw new RuntimeException(
          "Error al enviar email a " + direccionMail + ": " + e.getMessage(), e);
    }
  }

  private void enviarEmail(String destinatario, String cuerpo) throws MessagingException {
    Configuracion config = Configuracion.desdeElEntorno();

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", config.host);
    props.put("mail.smtp.port", config.port);

    Session session = Session.getInstance(props, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(config.username, config.password);
      }
    });

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress(config.from));
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
    message.setSubject("Notificacion DonaTrack");
    message.setText(cuerpo);

    Transport.send(message);
  }

  /**
   * Agrupa la configuracion SMTP y valida que este completa ANTES de armar las
   * Properties.
   * <p>
   * Sin esto, una variable de entorno faltante termina en un
   * NullPointerException adentro de java.util.Properties (que no acepta valores
   * null), tres capas abajo y sin ninguna pista de que falto configurar.
   */
  private static class Configuracion {

    final String host;
    final String port;
    final String username;
    final String password;
    final String from;

    private Configuracion(String host, String port, String username,
                          String password, String from) {
      this.host = host;
      this.port = port;
      this.username = username;
      this.password = password;
      this.from = from;
    }

    static Configuracion desdeElEntorno() {
      String host = System.getenv("MAIL_HOST");
      String port = System.getenv("MAIL_PORT");
      String username = System.getenv("MAIL_USERNAME");
      String password = System.getenv("MAIL_PASSWORD");
      String from = System.getenv("MAIL_FROM");

      List<String> faltantes = new ArrayList<>();
      if (esVacio(host)) faltantes.add("MAIL_HOST");
      if (esVacio(port)) faltantes.add("MAIL_PORT");
      if (esVacio(username)) faltantes.add("MAIL_USERNAME");
      if (esVacio(password)) faltantes.add("MAIL_PASSWORD");
      if (esVacio(from)) faltantes.add("MAIL_FROM");

      if (!faltantes.isEmpty()) {
        throw new IllegalStateException(
            "No se puede enviar mail: faltan las variables de entorno "
                + String.join(", ", faltantes)
                + ". Configurarlas en la Run Configuration del IDE o en el servidor.");
      }

      return new Configuracion(host, port, username, password, from);
    }

    private static boolean esVacio(String valor) {
      return valor == null || valor.isBlank();
    }
  }
}