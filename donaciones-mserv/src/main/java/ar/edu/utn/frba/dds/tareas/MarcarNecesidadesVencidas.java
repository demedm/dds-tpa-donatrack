package ar.edu.utn.frba.dds.tareas;

import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.repositories.NecesidadRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;

public class MarcarNecesidadesVencidas implements WithSimplePersistenceUnit {


  public static void main(String args[]) {
    Tarea.correr("Necesidades vencidas",
        () -> "marcadas como vencidas: " + ejecutar());
  }

  public static int ejecutar() {
    return new MarcarNecesidadesVencidas().withTransaction(() -> {

      NecesidadRepository repo = NecesidadRepository.getInstance();
      List<Necesidad> vencidas = repo.findAllRecurrentesActivasVencidas();

      //efecto de lado
      for (Necesidad necesidad : vencidas) {
        necesidad.setEstado("vencida");
        repo.actualizar(necesidad);
        repo.guardar(necesidad.crearSiguienteRecurrencia());
      }

      return vencidas.size();

    });

  }


}
