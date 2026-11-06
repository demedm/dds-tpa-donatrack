package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;

public class HomeController {

  public void landing(Context ctx) {
    ctx.render("landing-page.hbs");
  }

  public void mostrarLegal(Context ctx) {
    ctx.render("public/legal.hbs");
  }

}
