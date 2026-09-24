package ar.edu.utn.frba.dds.tareas;

import ar.edu.utn.frba.dds.main.Bootstrap;
import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoAsignacion;
import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoDeCompatibilidad;
import ar.edu.utn.frba.dds.model.Asignacion.AlgoritmoSubatendidos;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;

import java.util.ArrayList;
import java.util.List;

public class ProcesarDonaciones {

  private static final List<AlgoritmoAsignacion> ALGORITMOS =
    List.of(new AlgoritmoDeCompatibilidad(), new AlgoritmoSubatendidos());

  public static void main(String[] args) {
    Bootstrap.init();
    ejecutar();
  }

  public static void ejecutar() {

    procesar(DonacionesRepository.Instance.findSegmentadasEnDeposito(),
        EntidadRepository.Instance.obtenerEntidades(),
        ALGORITMOS);

  }

  public static void procesar(List<DonacionSegmentada> donaciones,
                              List<EntidadBeneficiaria> entidades,
                              List<AlgoritmoAsignacion> algoritmos) {

    List<String> fallidas = new ArrayList<>();

    for (DonacionSegmentada donacion : donaciones) {

      try {
        donacion.buscarCandidatas(entidades, algoritmos);
      } catch (RuntimeException e) {
        fallidas.add(donacion.getId() + ": " + e.getMessage());
      }
    }

    if(!fallidas.isEmpty()) {
      throw new RuntimeException("Fallo el matchmaking de : "+ fallidas.size()
          + " donacion(es):\n" + String.join("\n", fallidas));

    }

  }


}
