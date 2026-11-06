package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.dto.DonacionSegmentadaDTO;

import java.net.http.HttpClient;
import java.util.List;

public class ClientLogistica extends Client {

  public ClientLogistica(HttpClient client) {
    super(client);
  }

  public void enviarDonacionesEnLotes(List<DonacionSegmentadaDTO> donacionesListas) {
    int maximoPorLote = 100; // Restricción del proveedor logístico

    // Particionamos la lista en sublistas de máximo 100 elementos
    for (int i = 0; i < donacionesListas.size(); i += maximoPorLote) {
      int fin = Math.min(donacionesListas.size(), i + maximoPorLote);
      List<DonacionSegmentadaDTO> lote = donacionesListas.subList(i, fin);

      try {
        // Enviamos la lista completa en el body a un nuevo endpoint masivo
        post("planificaciones/lotes", lote, Object.class);

        System.out.println("✅ Lote de " + lote.size() + " donaciones enviado a Logística.");
      } catch (Exception e) {
        System.err.println("❌ Error al enviar lote a logística: " + e.getMessage());
      }
    }
  }
}