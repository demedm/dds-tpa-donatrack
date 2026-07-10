package ar.edu.utn.frba.dds.server;

import io.javalin.Javalin;

import java.io.IOException;

public class App {

  public static void main(String[] args) throws IOException, InterruptedException {
    // Bootstrap.init();
    new Server().start();
  }
}