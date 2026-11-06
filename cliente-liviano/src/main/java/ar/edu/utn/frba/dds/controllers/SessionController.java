package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.DonacionesHttpClient;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class SessionController {
  public void show(Context context) {
    if (context.sessionAttribute("user_id") != null) {
      context.redirect("/");
    }

    Map<String, Object> modelo = new HashMap<>();
    if ("true".equals(context.queryParam("error"))) {
      modelo.put("error", "usuario o contraseña invalidas");
    }

    String nombreRecordado = context.cookie("recordar_nombre");
    if (nombreRecordado != null) {
      modelo.put("recordar_nombre", nombreRecordado);
    }

    context.render("login.hbs", modelo);
  }

  public void login(Context ctx) {
    if (ctx.sessionAttribute("user_id") != null) {
      ctx.redirect("/");
      return;
    }
    Map<String, Object> modelo = new HashMap<>();

    String nombreRecordado = ctx.cookie("recordarNombre");
    if (nombreRecordado != null) {
      modelo.put("recordarNombre", nombreRecordado);
    }

    modelo.put("sesionIniciada", ctx.sessionAttribute("user_id") != null);
    ctx.render("login.hbs");
  }

  public void create(Context ctx) {
    String mail = ctx.formParam("email");
    String password = ctx.formParam("password");
    try {
      // segun lo que responda el modulo de donaciones se rechaza el inicio de sesion
      HttpResponse<String> response = new DonacionesHttpClient()
          .autenticarUsuario(mail, password);

      if (response.statusCode() == 200) {
        // para poder leer el token del json de respuesta
        JsonNode json = new ObjectMapper().readTree(response.body());

        // el token contiene: sub (identificador), rol, fecha emision y expiracion del token
        String token = json.get("token").asText();

        // setteo del token jwt para la sesion
        ctx.header("Set-Cookie", "session=" + token +
            "; Path=/; HttpOnly; SameSite=Lax; Secure");
        ctx.status(200);

      } else if (response.statusCode() == 401) {
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("error", "Email o contraseña incorrectos");
        ctx.render("/login", modelo);
        ctx.status(401);
        return;
      } else {
        Map<String, Object> modelo = new HashMap<>();
        modelo.put("error", "Ocurrió un error al iniciar sesión.");
        ctx.render("/login?error=true", modelo);
        return;
      }
      ctx.redirect("/");
    } catch (Exception e) {
      ctx.redirect("/login?error=true");
    }
  }

  public void mostrarDonaciones(Context ctx) {

  }

  public void mostrarRegistro(Context ctx) {
    ctx.render("public/registro.hbs");
  }

  public void registrarUsuario(@NotNull Context context) {

  }
}

// JSESSIONID=node01rra7at67x33zkdgq3rqze4ck0.node0
// JSESSIONID=node01rra7at67x33zkdgq3rqze4ck0.node0
// JSESSIONID=node01xqpogtx1ksio68qwagj8tpx0.node0
// JSESSIONID=node01xqpogtx1ksio68qwagj8tpx0.node0
// JSESSIONID=node01xqpogtx1ksio68qwagj8tpx0.node0

// client => COOKIE (puede mandar una cookie)
//
// <=  server (mantener la cookie como está)
// <=  server SET-COOKIE (cambia la cookie por otra)

// pero qué es la cookie?
// - podría ser un identificar de esa información de sesión (java)

// mecansimos para gestión de sesión
// - cookies
// - tokens al portador (bearer) (como una cabecera, tipicamente Authorization, como un query
// param, o como quieras)
//      - JWT => tipo de dato
