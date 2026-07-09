package ar.edu.utn.frba.dds.donaciones.necesidad.NecesidadDominio;

import ar.edu.utn.frba.dds.donaciones.entidad.EntidadBeneficiaria;
import java.util.ArrayList;
import java.util.List;


public class Necesidad {
  private String id;
  private String estado = "en_preparacion";
  private String descripcion;
  private String entidadId;
  public List<Peticion> peticiones = new ArrayList<>();
  private List<String> idDonantesParticipantes = new ArrayList<>();


  public Necesidad(String entidadId, String descripcion){
    this.entidadId=entidadId;
    this.descripcion =descripcion;
  }

  public void agregarPeticion(Peticion peticion){
    this.peticiones.add(peticion);
  }

  public String getId() { return id; }
  
  public void setId(String id) { this.id = id; }

  public List<Peticion> getPeticiones() {return this.peticiones;}

  public void setPeticiones(List <Peticion> peticiones){
    this.peticiones = peticiones;
  }

  public List<String> obtenerDonantesUnicos() {
    return this.peticiones.stream()
        .flatMap(p -> p.getDonacionesAsignadas().stream())
        .distinct()
        .toList();
  }

  public List<String> getIdDonantesParticipantes() {
    return this.idDonantesParticipantes;
  }

  public void actualizarEstado() {
    if (this.peticiones.isEmpty()) {
      this.estado = "en_preparacion";
      return;
    }
    
    boolean todasCubiertas = this.peticiones.stream()
        .allMatch(p -> p.estaCubierta());
    
    if (todasCubiertas) {
      this.estado = "cubierta";  
    } else {
      this.estado = "parcialmente_cubierta";
    }
  }

  public String getEstado() { return this.estado; }
  public void setEstado(String estado) { this.estado = estado; }

  // public boolean estaCubierta() {
  //       if (this.peticiones.isEmpty()) return false;
  //       return this.peticiones.stream()
  //           .allMatch(p -> p.estaCubierta());
  //   }

  // public List<Peticion> getPeticiones() {
  //   return peticiones;
  // }

  // public String getEstado() {
  //   return estado;
  // }

  // public void pedidoListo() {
  //   estado = "listo";
  // }

  // public void pedidoEnEntrega() {
  //   estado = "enviado";
  // }

  // public void pedidoRecibido() {
  //   estado = "recibido";
  // }


  // public void cumplirNecesidades(GestorDonaciones gestorDonaciones) {
  //   for (Peticion peticion : peticiones) {
  //     ResultadoBusqueda resultado = gestorDonaciones.buscarProducto(
  //         peticion.getSubclase(),peticion.getCantidad()
  //     );
  //     peticion.setCantidad(resultado.getRestante());
  //     peticion.agregarBienesAsignados(resultado.getBienesAsignados());
  //   }
  //   boolean todasCubiertas = peticiones.stream().allMatch(p->p.getCantidad()==0);
  //   if (todasCubiertas) this.pedidoListo();
  // }
  // public void marcarListaParaEntregar() {
  //   peticiones.forEach (p ->
  //       p.getBienesAsignados().forEach(b ->
  //           b.setEstado(EstadoDonacion.LISTA_PARA_ENTREGAR)
  //       )
  //   );
  // }
  // public void marcarEnTraslado() {
  //   peticiones.forEach(p ->
  //       p.getBienesAsignados().forEach(b -> b.setEstado(EstadoDonacion.EN_TRASLADO)
  //       )
  //   );
  // }

  // public void marcarEntregaFallida() {
  //   peticiones.forEach(p ->
  //       p.getBienesAsignados().forEach(b ->
  //           b.setEstado(EstadoDonacion.ENTREGA_FALLIDA)
  //       )
  //   );
  // }
}
