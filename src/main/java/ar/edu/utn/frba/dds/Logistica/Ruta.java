package ar.edu.utn.frba.dds.Logistica;

import java.util.ArrayList;
import java.util.List;

public class Ruta {
  private List<Destino> destinos;
  private String patenteAsignada;

  public Ruta(String patenteCamion, List<Destino> paradas) {
    this.destinos = paradas;
    this.patenteAsignada = patenteCamion;
  }

  public static Ruta rutaExternaToRuta(RutaComponenteExterno rutaExterna) {
    List<Destino> listaDestinos = new ArrayList<>();
    var listaDonaciones = rutaExterna.getEntregas();
    var listaDirecciones = rutaExterna.getDirecciones();

    for(int i = 0; i < listaDirecciones.size(); i++) {
     var destino = new Destino(listaDirecciones.get(i),
         listaDonaciones.get(i));
     listaDestinos.add(destino);
    }

    return new Ruta(rutaExterna.getPatenteCamion(), listaDestinos);
  }

  public void iniciarRuta() {
    destinos.forEach(destino -> { destino.getDonacion().iniciarTraslado(); });
  }

  public void visitarParada(String direccion) {
    destinos.stream().filter(destino -> destino.getDireccion().equals(direccion))
        .forEach(parada -> {
              parada.getDonacion().confirmarEntrega();
              parada.setVisitado(true);
            }
        );
  }

  public void finalizarRuta() {
    destinos.stream().filter(destino -> !destino.getVisitado())
        .forEach(destino -> destino
            .getDonacion().fallarEntrega("Regreso a depósito"));
  }

  public String getPatenteAsignada() { return this.patenteAsignada; }

}
