package ar.edu.utn.frba.dds.necesidad;

import ar.edu.utn.frba.dds.Bienes.EstadoDonacion;
import ar.edu.utn.frba.dds.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.EntidadBeneficiaria;

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

  public void agregarPeticiones(Subcategoria subclase, int cantidad) {
    Peticion p = new Peticion(subclase, cantidad);
    peticiones.add(p);
    //Voy aniadiendo a la lista de la necesidad las distintas peticiones de cosas que tengo
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
          peticion.getSubclase(),peticion.getCantidad()
      );
      peticion.setCantidad(resultado.getRestante());
      peticion.agregarBienesAsignados(resultado.getBienesAsignados());
    }
    boolean todasCubiertas = peticiones.stream().allMatch(p->p.getCantidad()==0);
    if (todasCubiertas) this.pedidoListo();
  }
  public void marcarListaParaEntregar() {
    peticiones.forEach (p ->
        p.getBienesAsignados().forEach(b ->
            b.setEstado(EstadoDonacion.LISTA_PARA_ENTREGAR)
        )
    );
  }
  public void marcarEnTraslado() {
    peticiones.forEach(p ->
        p.getBienesAsignados().forEach(b -> b.setEstado(EstadoDonacion.EN_TRASLADO)
        )
    );
  }

  public void marcarEntregaFallida() {
    peticiones.forEach(p ->
        p.getBienesAsignados().forEach(b ->
            b.setEstado(EstadoDonacion.ENTREGA_FALLIDA)
        )
    );
  }
}
