package ar.edu.utn.frba.dds.main;

import java.io.IOException;

public class App_Donaciones {

  public static void main(String[] args) throws IOException, InterruptedException {
    ConfiguracionBaseDeDatos.aplicar();
    Bootstrap.init();
    new Server().start();
  }
}