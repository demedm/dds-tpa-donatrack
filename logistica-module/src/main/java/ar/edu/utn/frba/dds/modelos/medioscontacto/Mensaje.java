package ar.edu.utn.frba.dds.modelos.medioscontacto;

import ar.edu.utn.frba.dds.Notificacion;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * Envío real de mensajes de WhatsApp mediante Twilio.
 *
 * Variables de entorno requeridas:
 *   TWILIO_ACCOUNT_SID
 *   TWILIO_AUTH_TOKEN
 *   TWILIO_WA_FROM     (ej: whatsapp:+14155238886 — sandbox de Twilio)
 *
 * El número destino debe tener formato: whatsapp:+549XXXXXXXXXX
 * Si se pasa solo el número, la clase agrega el prefijo automáticamente.
 */
public class Mensaje implements MedioContacto {

  private final String nroWhatsApp;

  public Mensaje(String numeroWhatsApp) {
    this.nroWhatsApp = numeroWhatsApp;
  }

  @Override
  public String getMedioContacto() {
    return nroWhatsApp;
  }

  @Override
  public void contactar(Notificacion notificacion) {
    notificacion.setDestinatario(this.getMedioContacto());
    try {
      enviarWhatsApp(notificacion.getDestinatario(), notificacion.getMensaje());
      notificacion.marcarComoCompletada();
    } catch (Exception e) {
      notificacion.marcarComoFallida();
      throw new RuntimeException("Error al enviar WhatsApp a " + nroWhatsApp, e);
    }
  }

  private void enviarWhatsApp(String destinatario, String cuerpo) {
    String accountSid = System.getenv("TWILIO_ACCOUNT_SID");
    String authToken  = System.getenv("TWILIO_AUTH_TOKEN");
    String fromWa     = System.getenv("TWILIO_WA_FROM");

    String to = destinatario.startsWith("whatsapp:") ? destinatario : "whatsapp:" + destinatario;

    Twilio.init(accountSid, authToken);

    Message.creator(
        new PhoneNumber(to),
        new PhoneNumber(fromWa),
        cuerpo
    ).create();
  }
}

