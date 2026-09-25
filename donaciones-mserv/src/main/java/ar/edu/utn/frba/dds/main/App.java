package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.utils.AsignacionCronScheduler;
import io.javalin.Javalin;

import java.io.IOException;

import ar.edu.utn.frba.dds.utils.LimpiezaCronScheduler;

public class App {

  public static void main(String[] args) throws IOException, InterruptedException {
    Bootstrap.init();
    LimpiezaCronScheduler.iniciar();
    AsignacionCronScheduler.iniciar();
    new Server().start();
  }
}