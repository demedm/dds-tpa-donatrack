package ar.edu.utn.frba.dds.medioscontacto;

import ar.edu.utn.frba.dds.Notificacion;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * Envío real de SMS mediante Twilio.
 *
 * Variables de entorno requeridas:
 *   TWILIO_ACCOUNT_SID
 *   TWILIO_AUTH_TOKEN
 *   TWILIO_FROM_PHONE  (ej: +15551234567)
 */
public class Telefono implements MedioContacto {

  public final String nroTelefono;

  public Telefono(String telefono) {
    this.nroTelefono = telefono;
  }

  @Override
  public String getMedioContacto() {
    return nroTelefono;
  }

  @Override
  public void contactar(Notificacion notificacion) {
    notificacion.setDestinatario(this.getMedioContacto());
    try {
      enviarSms(notificacion.getDestinatario(), notificacion.getMensaje());
      notificacion.marcarComoCompletada();
    } catch (Exception e) {
      notificacion.marcarComoFallida();
      throw new RuntimeException("Error al enviar SMS a " + nroTelefono, e);
    }
  }

  private void enviarSms(String destinatario, String cuerpo) {
    String accountSid = System.getenv("TWILIO_ACCOUNT_SID");
    String authToken  = System.getenv("TWILIO_AUTH_TOKEN");
    String fromPhone  = System.getenv("TWILIO_FROM_PHONE");

    Twilio.init(accountSid, authToken);

    Message.creator(
        new PhoneNumber(destinatario),
        new PhoneNumber(fromPhone),
        cuerpo
    ).create();
  }
}

//creeria que con la clase mensaje ya estaria