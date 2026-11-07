package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.DonacionesHttpClient;
import ar.edu.utn.frba.dds.dto.DonacionDto;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import ar.edu.utn.frba.dds.session.Rol;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class DonacionController {

  private static final List<String> CATEGORIAS = List.of("ALIMENTOS", "MOBILIARIO", "VESTIMENTA");
  private static final List<String> UNIDADES = List.of("UNIDADES", "KILOGRAMOS", "LITROS");
  private static final int FILAS_DE_BIENES = 3;

  private final DonacionesHttpClient donaciones = new DonacionesHttpClient();

  // GET /donaciones: cada rol ve su pantalla
  public void mostrarDonaciones(Context ctx) {
    UsuarioLogueado usuario = ctx.attribute("usuarioLogueado");   // lo carga Router.cargarSesion
    switch (usuario.getRol()) {
      case ADMIN -> mostrarPantallaDelAdmin(ctx, null, ctx.queryParam("registrada"));
      case DONANTE -> ctx.render("donantes/donaciones.hbs");
      case BENEFICIARIO -> ctx.render("beneficiarios/donaciones-asignadas.hbs");
    }
  }

  // POST /donaciones: el admin registra una donación recibida en el depósito
  public void registrarDonacion(@NotNull Context ctx) {
    UsuarioLogueado usuario = ctx.attribute("usuarioLogueado");
    if (usuario.getRol() != Rol.ADMIN) {
      ctx.status(403).result("Solo la persona administradora registra donaciones");
      return;
    }

    String donanteId = ctx.formParam("donanteId");
    List<Map<String, Object>> bienes = bienesDelFormulario(ctx);
    if (donanteId == null || donanteId.isBlank() || bienes.isEmpty()) {
      mostrarPantallaDelAdmin(ctx, "Elegí un donante y cargá al menos un bien con cantidad, "
          + "y con fecha de vencimiento (perecedero) o estado de uso (duradero).", null);
      return;
    }

    Map<String, Object> donacion = new HashMap<>();
    donacion.put("donanteId", Long.parseLong(donanteId));
    donacion.put("descripcionGeneral", ctx.formParam("descripcionGeneral"));
    donacion.put("bienes", bienes);

    DonacionDto creada = donaciones.crearDonacion(donacion, ctx.cookie("session"));
    if (creada == null) {
      mostrarPantallaDelAdmin(ctx, "No se pudo registrar la donación. Revisá los datos.", null);
      return;
    }
    // Se vuelve a la misma pantalla con un aviso (así recargar la página no la registra dos veces)
    ctx.redirect("/donaciones?registrada=" + creada.getId());
  }

  public void chequearAutorizacion(@NotNull Context context) {
  }

  private void mostrarPantallaDelAdmin(Context ctx, String error, String registrada) {
    String token = ctx.cookie("session");
    Map<String, Object> modelo = new HashMap<>();
    modelo.put("donaciones", donaciones.todasLasDonaciones(token));
    modelo.put("donantes", donaciones.listarDonantes(token));
    modelo.put("categorias", CATEGORIAS);
    modelo.put("unidades", UNIDADES);
    modelo.put("filas", IntStream.rangeClosed(1, FILAS_DE_BIENES).boxed().toList());
    modelo.put("error", error);
    modelo.put("registrada", registrada);
    ctx.render("admin/donaciones.hbs", modelo);
  }

  // Cada fila del formulario es un bien; las filas sin cantidad se ignoran
  private List<Map<String, Object>> bienesDelFormulario(Context ctx) {
    List<String> categorias = ctx.formParams("categoria");
    List<String> subcategorias = ctx.formParams("subcategoria");
    List<String> unidades = ctx.formParams("unidad");
    List<String> descripciones = ctx.formParams("descripcion");
    List<String> cantidades = ctx.formParams("cantidad");
    List<String> vencimientos = ctx.formParams("fechaVencimiento");
    List<String> estados = ctx.formParams("estadoUso");

    List<Map<String, Object>> bienes = new ArrayList<>();
    for (int i = 0; i < cantidades.size(); i++) {
      String cantidad = valor(cantidades, i);
      String vencimiento = valor(vencimientos, i);
      String estadoUso = valor(estados, i);
      if (cantidad.isBlank() || (vencimiento.isBlank() && estadoUso.isBlank())) {
        continue;
      }
      try {
        Map<String, Object> bien = new HashMap<>();
        bien.put("categoria", valor(categorias, i));
        bien.put("subcategoria", valor(subcategorias, i));
        bien.put("unidad", valor(unidades, i));
        bien.put("descripcion", valor(descripciones, i));
        bien.put("cantidad", new BigDecimal(cantidad.replace(',', '.')));
        if (!vencimiento.isBlank()) {
          bien.put("fechaVencimiento", vencimiento);   // viene del <input type="date">: 2027-01-01
        } else {
          bien.put("estadoUso", estadoUso);
        }
        bienes.add(bien);
      } catch (NumberFormatException e) {
        // cantidad que no es un número: esa fila se ignora
      }
    }
    return bienes;
  }

  private static String valor(List<String> lista, int i) {
    return i < lista.size() && lista.get(i) != null ? lista.get(i).trim() : "";
  }
}
