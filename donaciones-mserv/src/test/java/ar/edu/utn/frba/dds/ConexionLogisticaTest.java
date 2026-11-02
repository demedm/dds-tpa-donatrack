package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.main.ClientLogistica;
import ar.edu.utn.frba.dds.dto.DonacionSegmentadaDTO;
import org.junit.jupiter.api.Test;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

public class ConexionLogisticaTest {

  @Test
  public void testConexionLotesConLogistica() {
    HttpClient httpClient = HttpClient.newHttpClient();
    ClientLogistica cliente = new ClientLogistica(httpClient);

    // Creamos 105 donaciones falsas para forzar que el cliente arme 2 lotes (uno de 100 y uno de 5)
    List<DonacionSegmentadaDTO> donacionesDummy = new ArrayList<>();
    for (int i = 1; i <= 105; i++) {
      DonacionSegmentadaDTO dto = new DonacionSegmentadaDTO();
      dto.setIdDonacion(i); // <-- Es vital que el DTO tenga el ID
      dto.setIdEntidadAsignada(10);
      dto.setDireccionEntidad("Medrano 951, CABA");
      donacionesDummy.add(dto);
    }

    System.out.println("Enviando 105 donaciones a Logística...");
    cliente.enviarDonacionesEnLotes(donacionesDummy);
  }
}