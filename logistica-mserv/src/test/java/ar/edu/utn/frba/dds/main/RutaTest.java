package ar.edu.utn.frba.dds.main;

import java.util.Arrays;

import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RutaTest {

  @Test
  public void debeCalcularElPorcentajeDeAvanceCorrectamente() {
    // Arrange
    Entrega entrega1 = new Entrega("Calle Falsa 123", (long)1);
    Entrega entrega2 = new Entrega("Avenida Siempreviva 742", (long)2);

    // Simulamos que ya se entregó 1 de 2 paquetes
    entrega1.marcarComoEntregada();
    Chofer chofer = new Chofer("Carlos", "Martinez");
    Ruta ruta = new Ruta(chofer, Arrays.asList(entrega1, entrega2));

    // Act
    double avance = ruta.calcularPorcentajeAvance();

    // Assert
    assertEquals(50.0, avance, "El avance debería ser exactamente del 50%.");
  }
}
