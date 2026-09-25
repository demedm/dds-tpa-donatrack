package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class Bootstrap {
  public static void init() {
    List<Camion> camiones = camiones();
    List<Chofer> choferes = choferes();
    List<Entrega> entregas = entregas();
    List<EntidadBeneficiaria> beneficiarias = beneficiarias();

    entregas.get(0).setEntidadBeneficiaria(beneficiarias.get(0));
    entregas.get(1).setEntidadBeneficiaria(beneficiarias.get(1));
    entregas.get(2).setEntidadBeneficiaria(beneficiarias.get(0));
    entregas.get(3).setEntidadBeneficiaria(beneficiarias.get(1));
    entregas.get(4).setEntidadBeneficiaria(beneficiarias.get(1));

    var listaEntregas = Arrays.asList(entregas.get(0), entregas.get(1), entregas.get(2));
    var listaEntregas2 = Arrays.asList(entregas.get(3), entregas.get(4));
    Ruta ruta1 = new Ruta(listaEntregas);
    Ruta ruta2 = new Ruta(listaEntregas2);
    ruta1.asignarCamion(camiones.get(0));
    ruta2.asignarCamion(camiones.get(1));
    ruta1.asignarChofer(choferes.get(0));
    ruta2.asignarChofer(choferes.get(1));

    beneficiarias.forEach(UsuarioRepositorio.Instance::registrar);
    choferes.forEach(UsuarioRepositorio.Instance::registrar);
    camiones.forEach(CamionRepositorio.Instance::registrar);
    entregas.forEach(EntregaRepositorio.Instance::registrar);
    RutaRepositorio.Instance.registrar(ruta1);
    RutaRepositorio.Instance.registrar(ruta2);

    // Para probar el dashboard:
    ruta1.iniciarRuta();
    // Marcamos la primera entrega como entregada
    ruta1.getEntregas().get(0).marcarComoEntregada(camiones.get(0), LocalDateTime.now());
  }

  private static List<Entrega> entregas() {
    return Arrays.asList(new Entrega("Av Libertad 123", (long) 13),
        new Entrega("Helguera 516", (long) 57),
        new Entrega("Mandioca 67", (long) 4551),
        new Entrega("Luis Maria 777", (long) 12),
        new Entrega("Medrano 1512", (long) 61));
  }

  private static List<EntidadBeneficiaria> beneficiarias() {
    return Arrays.asList(new EntidadBeneficiaria("patitasfelices@outlook.com",
            "Av Siempreviva 123", "Patitas Felices ORG"),
        new EntidadBeneficiaria("extiendomismanos@gmail.com",
            "Calle Armando 748", "Extiendo Mis Manos Coop."));
  }

  private static List<Chofer> choferes() {
    return List.of(new Chofer("carlosmendez@gmail.com", "Carlos", "Mendez"),
        new Chofer("mariamonica@outlook.com", "Maria", "Monica"));
  }

  private static List<Camion> camiones() {
    return Arrays.asList(new Camion("aaaaAAAA", 1000.0,
            5000.0, 7.0), new Camion("bbbbBBBB",
            300.0, 6000.0, 5.0));
  }

}