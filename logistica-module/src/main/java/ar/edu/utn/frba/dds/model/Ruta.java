package ar.edu.utn.frba.dds.model;

import java.time.LocalDateTime;
import java.util.List;

public class Ruta {
  private List<Entrega> entregas;
  private String patenteAsignada;

  public Ruta(String patenteCamion, List<Entrega> entregas) {
    this.entregas = entregas;
    this.patenteAsignada = patenteCamion;
  }

  public String getPatenteAsignada() {
    return this.patenteAsignada;
  }

  public List<Entrega> getEntregas() {
    return this.entregas;
  }

  /*
  public void iniciarRuta() {
    entregas.forEach(destino -> {
      destino.getDonacion().iniciarTraslado();
    });
  }

  public void visitarParada(String direccion) {
    entregas.stream().filter(destino -> destino.getDireccion().equals(direccion))
        .forEach(parada -> {
              parada.getDonacion().confirmarEntrega();
              parada.setVisitado(true);
              // new Notificador().enviarNotificacionA(, "Su donación ha sido entregada");
            }
        );
  }

  public void finalizarRuta() {
    entregas.stream().filter(destino -> !destino.getVisitado())
        .forEach(destino -> { destino
              .getDonacion().fallarEntrega("Regreso a depósito");
          // new Notificador().enviarNotificacionA(,
          // "Su entrega no ha podido realizarse con exito");
        });
  }

  public String getPatenteAsignada() {
    return this.patenteAsignada;
  }

  public static class Ubicacion {
    private Double latitud;
    private Double longitud;
    private LocalDateTime timestamp;

    public Ubicacion(Double latitud, Double longitud, LocalDateTime timestamp) {
      this.latitud = latitud;
      this.longitud = longitud;
      this.timestamp = timestamp;
    }

    public Double getLatitud() { return latitud; }
    public Double getLongitud() { return longitud; }
    public LocalDateTime getTimestamp() { return timestamp; }

  }
   */
}
