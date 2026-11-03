package ar.edu.utn.frba.dds.controllers;
import io.javalin.http.Context;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeController {
  public void index(Context ctx) {
    Map<String, Object> model = new HashMap<>();
    model.put("titulo", "Página de Inicio");
    ctx.render("home.hbs", model);
  }
}
