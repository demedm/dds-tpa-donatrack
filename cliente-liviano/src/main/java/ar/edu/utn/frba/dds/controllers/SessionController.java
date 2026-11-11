package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.DonacionesHttpClient;
import ar.edu.utn.frba.dds.dto.RegistroRequest;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.repositorios.JwtRepositorio;
import ar.edu.utn.frba.dds.session.Rol;
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
    Map<String, Object> modelo = new HashMap<>();
    if ("exitoso".equals(ctx.queryParam("registro"))) {
      modelo.put("exito", "Tu cuenta fue creada correctamente. Ya podés iniciar sesión.");
    }

    if (ctx.sessionAttribute("user_id") != null) {
      ctx.redirect("/");
      return;
    }

    String nombreRecordado = ctx.cookie("recordarNombre");
    if (nombreRecordado != null) {
      modelo.put("recordarNombre", nombreRecordado);
    }

    modelo.put("sesionIniciada", ctx.sessionAttribute("user_id") != null);
    ctx.render("public/login.hbs", modelo);
  }

  public void logout(Context ctx) {
    ctx.header("Set-Cookie", "session=; Path=/; HttpOnly; SameSite=Lax; Secure");
    ctx.redirect("/");
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
        ctx.header("Set-Cookie", "session=" + token + "; Path=/; HttpOnly; SameSite=Lax");

        UsuarioLogueado usuario = JwtRepositorio.Instance.obtenerUsuario(token);
        ctx.attribute("usuarioLogueado", usuario);
        ctx.status(200);

      } else if (response.statusCode() == 401) {
        // CAMBIAR ctx.render POR:
        ctx.redirect("/login?error=true");
        return;
      } else {
        // CAMBIAR ctx.render POR:
        ctx.redirect("/login?error=true");
        return;
      }
      ctx.redirect("/");
    } catch (Exception e) {
      System.err.println("--- ERROR EN LOGIN ---");
      e.printStackTrace(); // Esto te mostrará en la consola de IntelliJ el error exacto
      ctx.redirect("/login?error=true");
    }
  }

  public void mostrarRegistro(Context ctx) {
    ctx.render("/public/registro-usuario.hbs");
  }

  public void mostrarRegistroAdmin(Context ctx) {
    Map<String, Object> modelo = new HashMap<>();

    modelo.put("esAdmin", true);

    ctx.render("public/registro-donacion-persona.hbs", modelo);
  }

  public void registrarUsuario(Context ctx) {
    String mail = ctx.formParam("email");
    String password = ctx.formParam("password");
    String rol = ctx.formParam("rol");
    String nombre = ctx.formParam("nombre");

    Map<String, Object> modelo = new HashMap<>();
    String token = ctx.cookie("session");
    UsuarioLogueado usuario = new JwtRepositorio().obtenerUsuario(token);
    if (usuario.getRol().equals(Rol.ADMIN)) {
      modelo.put("esAdmin", true);
    }

    RegistroRequest request = new RegistroRequest(nombre, mail, password, rol);

    // Datos adicionales según el rol
    if ("DONANTE".equals(rol)) {

      request.setTipoPersona(ctx.formParam("tipoPersona"));
      request.setNombreIdentificador(ctx.formParam("nombreIdentificador"));
      request.setMailDonante(ctx.formParam("mailDonante"));
      request.setTelefonoDonante(ctx.formParam("telefonoDonante"));
      request.setMedioPreferido(ctx.formParam("medioPreferido"));

    } else if ("BENEFICIARIO".equals(rol)) {

      request.setRazonSocial(ctx.formParam("razonSocial"));
      request.setDireccion(ctx.formParam("direccion"));
      request.setMailContacto(ctx.formParam("mailContacto"));
      request.setTelefonoBeneficiario(ctx.formParam("telefonoBeneficiario"));
    }

    try {
      HttpResponse<String> response = new DonacionesHttpClient()
          .registrarUsuario(request);

      if (response.statusCode() == 201) {

        // Si lo creó un administrador, vuelve al panel
        if ("ADMIN".equals(rol)) {
          ctx.redirect("/admin/panel?registro=exitoso");
        } else {
          ctx.redirect("/login?registro=exitoso");
        }
        return;

      } else if (response.statusCode() == 409) {
        modelo.put("error", "Ya existe un usuario registrado con ese email.");
      } else {
        modelo.put("error", "No se pudo completar el registro.");
      }

    } catch (Exception e) {
      e.printStackTrace();
      modelo.put("error", "No fue posible comunicarse con el servidor.");
    }

    ctx.render("public/registro-donacion-persona.hbs", modelo);
  }

  public void validarAutenticacion(@NotNull Context ctx) {
    String token = ctx.cookie("session");
    if (token == null || token.isBlank()) {
      ctx.redirect("/login");
      return;
    }

    try {
      UsuarioLogueado usuario = JwtRepositorio.Instance.obtenerUsuario(token);
      ctx.attribute("usuarioLogueado", usuario);
    } catch (Exception e) { // si es invalido o expiro, elimina la cookie y redirige al login
      ctx.header(
          "Set-Cookie",
          "session=; Path=/; Max-Age=0; HttpOnly; SameSite=Lax"
      );

      ctx.redirect("/login");
    }
  }

  public void mostrarUsuarios(@NotNull Context ctx) {
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
