package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import java.util.Arrays;
import java.util.List;

public class Bootstrap {
  public static void init() {
    List<Camion> camiones = camiones();
    camiones.forEach(camion ->
        CamionRepositorio.Instance.registrarCamion(camion));
    List<Ruta> rutas = rutas();
    Camion camion1 = camiones.get(0);
    rutas.stream().limit(camiones.size()).forEach(camion1::asignarRuta);

    RutaRepositorio.Instance.setAllRutas(rutas());
    //esto es para probar el dashboard
    // Forzamos a que el camión que ya tiene la ruta asignada arranque a trabajar
    camion1.iniciarRuta();

    // Le marcamos la primera entrega de SU propia ruta como completada
    camion1.getRutaActual().getEntregas().get(0).marcarComoEntregada();
  }

  private static List<Ruta> rutas() {
    var listaEntregas = Arrays.asList(new Entrega("Av Libertad 123", 13),
        new Entrega("Helguera 516", 57),
        new Entrega("Mandioca 67", 4551));
    var listaEntregas2 = Arrays.asList(new Entrega("Luis Maria 777", 12),
        new Entrega("Medrano 1512", 61));

    return Arrays.asList(new Ruta("aaaaAAAA", listaEntregas),
        new Ruta("bbbbBBBB", listaEntregas2),
        new Ruta("ccccCCCC", listaEntregas));
  }

  private static List<Camion> camiones() {
    return Arrays.asList(new Camion("aaaaAAAA", 1000.0,
            5000.0, 7.0), new Camion("bbbbBBBB",
            300.0, 6000.0, 5.0),
        new Camion("ccccCCCC", 3400.0,
            6700.0, 10.0));
  }

}