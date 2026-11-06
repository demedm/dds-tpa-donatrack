package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class HomeController {

  public void landing(Context ctx) {
    Map<String, Object> modelo = new HashMap<>();
    UsuarioLogueado usuario = ctx.attribute("usuarioLogueado");
    System.out.println("Usuario autenticado: " + usuario);
    modelo.put("logueado", usuario != null);

    ctx.render("landing-page.hbs", modelo);
  }

  public void mostrarLegal(Context ctx) {
    ctx.render("public/legal.hbs");
  }

}
