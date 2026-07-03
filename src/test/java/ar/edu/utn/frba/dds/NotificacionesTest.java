package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.donantes.Identificacion;
import ar.edu.utn.frba.dds.donantes.Persona;
import ar.edu.utn.frba.dds.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.donantes.TipoPersona;
import ar.edu.utn.frba.dds.medioscontacto.Mail;
import ar.edu.utn.frba.dds.medioscontacto.MedioContacto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Tests del Notificador con Mockito.
 *
 * MedioContacto se mockea para que los tests corran sin credenciales reales
 * de Twilio ni servidor SMTP.
 */
@ExtendWith(MockitoExtension.class)
public class NotificacionesTest {

  @Mock
  private MedioContacto medioMock;

  @Mock
  private MedioContacto otroMedio;

  private Notificador notificador;
  private Persona persona;

  @BeforeEach
  void setUp() {
    notificador = new Notificador();

    Mail mail = new Mail("donante@ejemplo.com");
    Identificacion id = new Identificacion(TipoDocumento.DNI, "12345678");
    persona = new Persona("Juan Pérez", mail, id, TipoPersona.FISICA);
    persona.setMedioPreferido(medioMock);

    // El mock simula un envío exitoso marcando la notificación como completada
    doAnswer(inv -> {
      Notificacion n = inv.getArgument(0);
      n.setDestinatario("donante@ejemplo.com");
      n.marcarComoCompletada();
      return null;
    }).when(medioMock).contactar(any(Notificacion.class));
  }

  @Test
  @DisplayName("Debe usar el medio preferido de la persona al notificar")
  void debeUsarMedioPreferido() {
    notificador.enviarNotificacionA(persona, "Mensaje de prueba");

    verify(medioMock).contactar(any(Notificacion.class));
    verifyNoInteractions(otroMedio);
  }

  @Test
  @DisplayName("La notificación debe contener el mensaje exacto enviado")
  void notificacionContieneElMensaje() {
    ArgumentCaptor<Notificacion> captor = ArgumentCaptor.forClass(Notificacion.class);

    notificador.enviarNotificacionA(persona, "Tu donación fue asignada");

    verify(medioMock).contactar(captor.capture());
    assertEquals("Tu donación fue asignada", captor.getValue().getMensaje());
  }

  @Test
  @DisplayName("La notificación queda COMPLETADA cuando el envío es exitoso")
  void notificacionCompletadaCuandoEnvioExitoso() {
    Notificacion resultado = notificador.enviarNotificacionA(persona, "Hola!");

    assertNotNull(resultado);
    assertEquals(EstadoNotificacion.COMPLETADA, resultado.getEstado());
  }

  @Test
  @DisplayName("Lanza excepción si la persona no tiene medio preferido configurado")
  void lanzaExcepcionSinMedioPreferido() {
    persona.setMedioPreferido(null);

    assertThrows(IllegalStateException.class,
        () -> notificador.enviarNotificacionA(persona, "Mensaje"));
  }
}