package ar.edu.utn.frba.dds.necesidad;
import ar.edu.utn.frba.dds.Estado.ListaParaEntregar;
import ar.edu.utn.frba.dds.Estado.EnTraslado;
import ar.edu.utn.frba.dds.Estado.EntregaFallida;
import ar.edu.utn.frba.dds.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.necesidad.Necesidad;

import java.util.ArrayList;
import java.util.List;

public class Necesidad {
  private String estado = "preparacion";
  private EntidadBeneficiaria entidad;
  public List<Peticion> peticiones = new ArrayList<>();

  public Necesidad(EntidadBeneficiaria entidad){
    this.entidad=entidad;
  }

  public boolean estaPreparado() {
    return estado.equals("listo");
  }

  public void agregarPeticiones(String subclase, int cantidad) {
    Peticion p = new Peticion(subclase, cantidad);
    peticiones.add(p);
  }

  public List<Peticion> getPeticiones() {
    return peticiones;
  }

  public String getEstado() {
    return estado;
  }

  public void pedidoListo() {
    estado = "listo";
  }

  public void pedidoEnEntrega() {
    estado = "enviado";
  }

  public void pedidoRecibido() {
    estado = "recibido";
  }

  public void cumplirNecesidades(GestorDonaciones gestorDonaciones) {
    for (Peticion peticion : peticiones) {
      ResultadoBusqueda resultado = gestorDonaciones.buscarProducto(
          peticion.getSubclase(), peticion.getCantidad()
      );
      peticion.setCantidad(resultado.getRestante());

      // Acá guardamos la donación que resolvió el gestor, no el bien suelto
      peticion.agregarDonacionesAsignadas(resultado.getDonacionesAsignadas());
    }
    for (Peticion peticion : peticiones) {
      ResultadoBusqueda resultado = gestorDonaciones.buscarProducto(
          peticion.getSubclase(),peticion.getCantidad()
      );
      peticion.setCantidad(resultado.getRestante());
      peticion.agregarDonacionesAsignadas(resultado.getDonacionesAsignadas());
    }
    boolean todasCubiertas = peticiones.stream().allMatch(p->p.getCantidad()==0);
    if (todasCubiertas) this.pedidoListo();
  }
  public void marcarListaParaEntregar() {
    peticiones.forEach(p ->
        p.getDonacionesAsignadas().forEach(d -> // Ahora iteramos sobre las donaciones
            d.setEstado(new ListaParaEntregar())
        )
    );
  }

  public void marcarEnTraslado() {
    peticiones.forEach(p ->
        p.getDonacionesAsignadas().forEach(d ->
            d.setEstado(new EnTraslado())
        )
    );
  }

  public void marcarEntregaFallida() {
    peticiones.forEach(p ->
        p.getDonacionesAsignadas().forEach(d ->
            d.setEstado(new EntregaFallida())
        )
    );
  }
}