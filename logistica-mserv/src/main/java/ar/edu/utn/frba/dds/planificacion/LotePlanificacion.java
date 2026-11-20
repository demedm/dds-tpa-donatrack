package ar.edu.utn.frba.dds.planificacion;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class LotePlanificacion {
  @Id
  @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  private EstadoLote estado;

  @ElementCollection
  private List<Long> idsCamiones = new ArrayList<>();

  private LocalDateTime recibidoEn;

  public LotePlanificacion() {}

  public List<Long> getIdsCamiones() {
    return idsCamiones;
  }

  public void setIdsCamiones(List<Long> idsCamiones) {
    this.idsCamiones = idsCamiones;
  }

  public EstadoLote getEstado() {
    return estado;
  }

  public void setEstado(EstadoLote estado) {
    this.estado = estado;
  }

  public Long getId() {
    return id;
  }

  public LocalDateTime getRecibidoEn() {
    return recibidoEn;
  }

  public void setRecibidoEn(LocalDateTime recibidoEn) {
    this.recibidoEn = recibidoEn;
  }
}
