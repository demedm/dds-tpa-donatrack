package ar.edu.utn.frba.dds.controllers;

import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class HomeController {

  public void landing(Context ctx) {
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("logueado", ctx.attribute("usuarioLogueado") != null);
    ctx.render("landing-page.hbs");
  }

  public void mostrarLegal(Context ctx) {
    ctx.render("public/legal.hbs");
  }

}
