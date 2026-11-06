package ar.edu.utn.frba.dds.model.Asignacion;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "ejecuciones_algoritmo")

public class EjecucionAlgoritmo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String algoritmo;

  @ElementCollection
  @CollectionTable(name = "ejecucion_ranking",
      joinColumns = @JoinColumn(name = "ejecucion_id"))
  @OrderColumn(name = "posicion")
  @Column(name = "entidad_id")
  private List<Long> idsEntidades = new ArrayList<>();

  protected  EjecucionAlgoritmo() {
  }

  public EjecucionAlgoritmo(String algoritmo, List<Long> idsEntidades) {
    this.algoritmo = algoritmo;
    this.idsEntidades = new ArrayList<>(idsEntidades);
  }

  public String getAlgoritmo() {
    return algoritmo;
  }

  public List<Long> getIdsEntidades() {
    return Collections.unmodifiableList(idsEntidades);
  }

  public boolean propone(Long entidadId) {
    return idsEntidades.contains(entidadId);
  }

}
