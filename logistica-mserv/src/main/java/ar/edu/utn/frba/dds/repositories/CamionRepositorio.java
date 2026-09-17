package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.model.EstadoRuta;
import ar.edu.utn.frba.dds.model.Ruta;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.List;

public class CamionRepositorio implements WithSimplePersistenceUnit {
  public static final CamionRepositorio Instance = new CamionRepositorio();

  public void reportarImprevisto(Long id) {
    Camion camion = buscarPorId(id);
    camion.improvistoLogistico();
    var rutaEnCurso = RutaRepositorio.Instance
        .buscarRutasDeCamionConEstado(id, EstadoRuta.EN_CURSO);
    rutaEnCurso.indicarImprovistoLogistico();
  }

  public void registrar(Camion camion) {
    entityManager().persist(camion);
  }

  @SuppressWarnings("unchecked")
  public List<Camion> mostrarTodos() {
    return entityManager()
        .createQuery("from Camion")
        .getResultList();
  }

  public Camion buscarPorId(Long id) {
    return entityManager()
        .createQuery("from Camion c where c.id = :id", Camion.class)
        .setParameter("id", id)
        .getSingleResult();
  }

  public Camion buscarPorPatente(String patente) {
    return entityManager()
        .createQuery("from Camion c where c.patente = :patente", Camion.class)
        .setParameter("patente", patente)
        .getSingleResult();
  }

  @SuppressWarnings("unchecked")
  public List<Camion> mostrarCamionesDisponibles() {
    return entityManager()
        .createQuery("from Camion c where c.estado = :estado")
        .setParameter("estado", EstadoCamion.DISPONIBLE)
        .getResultList();
  }

  /*
  // devuelve false si no se pudo asignar
  public boolean asignarRutaACamion(Ruta ruta) {
    var patenteCamion = ruta.getPatenteAsignada();
    var camionAsignado = allCamiones.stream()
        .filter(camion -> camion.getPatente() == patenteCamion)
        .findFirst().orElse(null);

    if(camionAsignado != null && camionAsignado.asignarRuta(ruta)) {
      // notificar entidades que se inicio la ruta: pendiente
      return true;
    }
    return false;
  }
  public Camion buscarCamionPorPatente(String patente) {
    return this.allCamiones.stream()
        .filter(camion -> camion.getPatente().equals(patente))
        .findFirst()
        .orElse(null);
  }

  public List<Ruta> asignarRutasACamiones(List<RutaComponenteExterno> rutasAsignadas) {
    List<Ruta> rutas = rutasAsignadas.stream().map(ruta ->
        new RutaAdapter().rutaExternaToRuta(ruta)).toList();
    List<Ruta> rutasNoAsignadas = new ArrayList<>();

    rutas.forEach(ruta -> {
      Camion camion = allCamiones.stream()
          .filter(c -> c.getPatente().equals(ruta.getPatenteAsignada()))
          .findFirst()
          .orElse(null);

      if(camion == null || !camion.asignarRuta(ruta)) {
        rutasNoAsignadas.add(ruta);
      }
    });
    return rutasNoAsignadas;
  }
  // Asumiendo que tu lista se llama "camiones". Si se llama distinto
  // (ej: "listaCamiones"), cambialo en el return.
  public List<Camion> getCamiones() {
    return this.allCamiones;
  }
  */

}