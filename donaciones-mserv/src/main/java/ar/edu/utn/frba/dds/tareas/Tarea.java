package ar.edu.utn.frba.dds.tareas;

import ar.edu.utn.frba.dds.main.ConfiguracionBaseDeDatos;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.function.Supplier;

public class Tarea {

  public static void correr(String nombre, Supplier<String> tarea){
    int codigo= 0;

    try{
      ConfiguracionBaseDeDatos.aplicar();
      System.out.println("[" + nombre + "] " + tarea.get());
    } catch (Exception e) {
      System.err.println("[" + nombre + "] falló: " + e.getMessage());
      e.printStackTrace();
      codigo = 1;
    } finally {
      WithSimplePersistenceUnit.dispose();
    }

    System.exit(codigo);

  }

}
