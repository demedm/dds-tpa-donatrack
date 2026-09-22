package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Bootstrap {
  public static void init() {
    List<Camion> camiones = camiones();
    camiones.forEach(CamionRepositorio.Instance::registrar);
    List<Ruta> rutas = rutas();
    rutas.forEach(RutaRepositorio.Instance::registrar);

    rutas.get(0).asignarCamion(camiones.get(0));
    rutas.get(1).asignarCamion(camiones.get(1));
    rutas.get(2).asignarCamion(camiones.get(2));

    Ruta ruta1 = rutas.get(0);

    // Para probar el dashboard:
    ruta1.iniciarRuta();

    // Marcamos la primera entrega como entregada
    ruta1.getEntregas().get(0).marcarComoEntregada(camiones.get(0), LocalDateTime.now());
  }

  private static List<Ruta> rutas() {
    var listaEntregas = Arrays.asList(new Entrega("Av Libertad 123", (long) 13),
        new Entrega("Helguera 516", (long) 57),
        new Entrega("Mandioca 67", (long) 4551));
    var listaEntregas2 = Arrays.asList(new Entrega("Luis Maria 777", (long) 12),
        new Entrega("Medrano 1512", (long) 61));

    Chofer chofer = new Chofer("matias@gmail.com", "123123123", "Matias", "Moreno");
    Chofer chofer2 = new Chofer("carloslop@gmail.com", "123calleviva","Carlos", "Lop");
    Chofer chofer3 = new Chofer("mariamm@gmail.com", "hsodfi1290","Maria", "Molas");
    return Arrays.asList(new Ruta(chofer, listaEntregas),
        new Ruta(chofer2, listaEntregas2),
        new Ruta(chofer3, listaEntregas));
  }

  private static List<Camion> camiones() {
    return Arrays.asList(new Camion("aaaaAAAA", 1000.0,
            5000.0, 7.0), new Camion("bbbbBBBB",
            300.0, 6000.0, 5.0),
        new Camion("ccccCCCC", 3400.0,
            6700.0, 10.0));
  }

}