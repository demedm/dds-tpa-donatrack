package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.notificaciones.medioscontacto.Mail;
import ar.edu.utn.frba.dds.notificaciones.medioscontacto.MedioContacto;
import org.junit.jupiter.api.Test;

public class NotificacionTest {

  @Test
  public void importaCorrectamentedelCSV() {
    assert(Notificacion.notificar(notificacion("maríaaguirre4360@outlook.com"), "Hola Maria"));
  }

  private MedioContacto notificacion(String direccionMail) {
    return new Mail(direccionMail);
  }
}
