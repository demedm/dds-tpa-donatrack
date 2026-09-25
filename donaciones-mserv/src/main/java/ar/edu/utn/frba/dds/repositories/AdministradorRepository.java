// ar/edu/utn/frba/dds/repositories/AdministradorRepository.java   [NUEVO]
package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.notificaciones.Destinatario;

import java.util.ArrayList;
import java.util.List;

/**
 * Personas administradoras a notificar ante una entrega fallida.
 *
 * Reemplaza el System.getenv("ADMIN_MAIL") que estaba dentro de una clase de
 * modelo: la configuracion no va en el dominio.
 *
 * Se siembra desde Bootstrap o desde la variable de entorno ADMIN_MAILS
 * (direcciones separadas por coma).
 */
public class AdministradorRepository {

  public static AdministradorRepository Instance = new AdministradorRepository();

  private final List<Destinatario> administradores = new ArrayList<>();

  private AdministradorRepository() {
    cargarDesdeEntorno();
  }

  private void cargarDesdeEntorno() {
    String mails = System.getenv("ADMIN_MAILS");
    if (mails == null || mails.isBlank()) {
      return;
    }
    for (String direccion : mails.split(",")) {
      String limpia = direccion.trim();
      if (!limpia.isEmpty()) {
        administradores.add(new Mail(limpia));
      }
    }
  }

  public void agregar(Destinatario administrador) {
    administradores.add(administrador);
  }

  public List<Destinatario> administradores() {
    return List.copyOf(administradores);
  }

  //para test
  public void limpiar() {
    administradores.clear();
  }
}