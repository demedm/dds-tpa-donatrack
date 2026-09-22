package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.model.Ubicacion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class CamionTest {

  @Test
  public void noDebeActualizarUbicacionSiNoEstaRealizandoEntregas() {
    // Arrange
    Camion camion = new Camion("AA123BB", 1000.0, 5000.0, 7.0);

    // Act
    Ubicacion ubicacion = new Ubicacion(camion);

    // Assert
    assertNull(ubicacion.getLatitud(), "La ubicación debe ser nula porque no está en ruta.");
    assertNull(ubicacion.getLongitud(), "La ubicación debe ser nula porque no está en ruta.");
  }

  @Test
  public void debeActualizarUbicacionSiEstaRealizandoEntregas() {
    // Arrange
    Camion camion = new Camion("AA123BB", 1000.0, 5000.0, 7.0);
    camion.iniciarRuta();

    // Act
    Ubicacion ubicacion = new Ubicacion(camion);
    ubicacion.actualizarUbicacion(-34.6037, -58.3816);

    // Assert
    assertNotNull(ubicacion.getTimestamp(), "La ubicación no debe ser nula.");
    assertEquals(-34.6037, ubicacion.getLatitud());
    assertEquals(-58.3816, ubicacion.getLongitud());
  }
}