package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CamionTest {

  @Test
  public void noDebeActualizarUbicacionSiNoEstaRealizandoEntregas() {
    // Arrange
    Camion camion = new Camion("AA123BB", 1000, 5000, 7);
    camion.setEstado(EstadoCamion.DISPONIBLE);

    // Act
    camion.actualizarUbicacion(-34.6037, -58.3816);

    // Assert
    assertNull(camion.getUbicacionActual(), "La ubicación debe ser nula porque no está en ruta.");
  }

  @Test
  public void debeActualizarUbicacionSiEstaRealizandoEntregas() {
    // Arrange
    Camion camion = new Camion("AA123BB", 1000, 5000, 7);
    camion.setEstado(EstadoCamion.REALIZANDO_ENTREGAS);

    // Act
    camion.actualizarUbicacion(-34.6037, -58.3816);

    // Assert
    assertNotNull(camion.getUbicacionActual(), "La ubicación no debe ser nula.");
    assertEquals(-34.6037, camion.getUbicacionActual().getLatitud());
  }
}