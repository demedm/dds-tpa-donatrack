package ar.edu.utn.frba.dds.server;

import io.javalin.Javalin;


public class App {

  public static void main(String[] args) {
    new Server().start();
  }
}