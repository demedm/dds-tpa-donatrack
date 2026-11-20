package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.session.Rol;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

public class HomeController {

  public void landing(Context ctx) {
    Map<String, Object> modelo = new HashMap<>();
    UsuarioLogueado usuario = ctx.attribute("usuarioLogueado");
    System.out.println("Usuario autenticado: " + usuario);
    modelo.put("logueado", usuario != null);
    modelo.put("esBeneficiario", usuario != null && Rol.BENEFICIARIO.equals(usuario.getRol()));
    modelo.put("esDonante", usuario != null && Rol.DONANTE.equals(usuario.getRol()));

    try {
      if (usuario.getRol().equals(Rol.ADMIN)) {
        ctx.redirect("/admin/panel");
      }
      ctx.render("landing-page.hbs", modelo);
    } catch (Exception e) {
      ctx.render("landing-page.hbs", modelo);
    }
  }

  public void mostrarLegal(Context ctx) {
    ctx.render("public/legal.hbs");
  }

}
