package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.model.notificaciones.EventoNotificacionService;
import ar.edu.utn.frba.dds.model.notificaciones.Notificacion;
import ar.edu.utn.frba.dds.model.notificaciones.Notificador;
import ar.edu.utn.frba.dds.repositories.AdministradorRepository;
import ar.edu.utn.frba.dds.repositories.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificacionTest {

  private EventoNotificacionService servicio;
  private NotificacionRepository historial;

  private MedioContacto mailDonante;
  private MedioContacto mailEntidad;
  private MedioContacto mailAdmin;

  @BeforeEach
  void init() {
    historial = new NotificacionRepository();
    servicio = new EventoNotificacionService(new Notificador(historial));

    mailDonante = medioMockeado("juan@mail.com");
    mailEntidad = medioMockeado("comedor@mail.com");
    mailAdmin = medioMockeado("admin@donatrack.com");

    //AdministradorRepository.Instance.limpiar();
  }

  private MedioContacto medioMockeado(String direccion) {
    MedioContacto medio = mock(MedioContacto.class);
    when(medio.medioDeContacto()).thenReturn(medio);
    when(medio.nombreParaMostrar()).thenReturn(direccion);
    when(medio.getMedioContacto()).thenReturn(direccion);
    return medio;
  }

  private String mensajeEnviadoA(MedioContacto medio) {
    ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);
    verify(medio).contactar(captor.capture());
    return captor.getValue().getMensaje();
  }

  @Test
  @DisplayName("Al donante inactivo se lo contacta por su medio")
  void seNotificaAlDonanteInactivo() {
    servicio.notificarInactividadDonante(mailDonante);

    verify(mailDonante).contactar(any(Notificacion.class));
  }

  @Test
  @DisplayName("Al donante se le avisa a que entidad fue su donacion")
  void alDonanteSeLeAvisaLaEntidad() {
    servicio.notificarDonacionAsignadaDonante(mailDonante, "D-1", "Comedor Los Pinos");
    assertTrue(mensajeEnviadoA(mailDonante).contains("Comedor Los Pinos"));
  }

  @Test
  @DisplayName("La entrega fallida llega al donante, a la entidad Y a los admins")
  void laEntregaFallidaLlegaTambienALosAdmins() {
    AdministradorRepository.Instance.agregar(mailAdmin);

    servicio.notificarEntregaFallida(
        List.of(mailDonante, mailEntidad), "D-1", "La entidad no pudo recibir", false);

    verify(mailDonante).contactar(any(Notificacion.class));
    verify(mailEntidad).contactar(any(Notificacion.class));
    verify(mailAdmin).contactar(any(Notificacion.class));
  }

  // TEST DE ENVIO DE MAIL
    @Test
    @DisplayName("Una direccion sin arroba se rechaza al construir")
    void unaDireccionInvalidaSeRechaza() {
      assertThrows(IllegalArgumentException.class, () -> new Mail("no-es-un-mail"));
    }

    @Test
    @DisplayName("Una direccion nula se rechaza al construir")
    void unaDireccionNulaSeRechaza() {
      assertThrows(IllegalArgumentException.class, () -> new Mail(null));
    }
}