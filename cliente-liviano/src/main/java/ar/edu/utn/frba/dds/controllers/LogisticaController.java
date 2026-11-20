package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClientCamionImplementation;
import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClientLogisticaImplementation;
import ar.edu.utn.frba.dds.clients.GrpcClientImplementation.GrpcClientRutaImplementation;
import ar.edu.utn.frba.dds.clients.HttpClient.LogisticaHttpClient;
import ar.edu.utn.frba.dds.dto.CamionDto;
import ar.edu.utn.frba.dds.dto.CamionRequest;
import ar.edu.utn.frba.dds.dto.EntregaDto;
import ar.edu.utn.frba.dds.dto.UsuarioLogueado;
import com.camionesServiceGrpc.grpc.CamionResponse;
import com.camionesServiceGrpc.grpc.CamionesResponse;
import com.rutasServiceGrpc.grpc.RutasResponse;
import io.grpc.StatusRuntimeException;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogisticaController {
  private final GrpcClientCamionImplementation clientCamiones;
  private final GrpcClientRutaImplementation clientRutas;

  public LogisticaController() {
    this.clientCamiones = new GrpcClientCamionImplementation();
    this.clientRutas = new GrpcClientRutaImplementation();
  }

  public void mostrarCamiones(Context ctx) {
    String token = ctx.cookie("session");
    if (token == null || token.isBlank()) {
      ctx.redirect("/login");
      return;
    }

    Map<String, Object> modelo = new HashMap<>();
    modelo.put("logueado", true);

    try {
      CamionesResponse response = clientCamiones.mostrarCamiones(token);

      if (!response.getExito()) {
        modelo.put("camiones", List.of());
        modelo.put("error", response.getMensaje());
      } else {
        List<CamionDto> camiones = response.getCamiones().getCamionesList().stream()
            .map(CamionDto::fromProto)
            .toList();

        String pedido = ctx.queryParam("camion");
        CamionDto seleccionado = camiones.stream()
            .filter(c -> c.getId().toString().equals(pedido))
            .findFirst()
            .orElse(camiones.isEmpty() ? null : camiones.get(0));

        if (seleccionado != null) {
          seleccionado.setSeleccionado(true);
          modelo.put("entregas", cargarEntregas(token, seleccionado, modelo));
        }

        modelo.put("camiones", camiones);
        modelo.put("camionSeleccionado", seleccionado);
      }
      ctx.render("admin/logistica.hbs", modelo);

    } catch (StatusRuntimeException e) {
      switch (e.getStatus().getCode()) {
        case UNAUTHENTICATED, PERMISSION_DENIED -> ctx.redirect("/login");
        case UNAVAILABLE, DEADLINE_EXCEEDED -> {
          modelo.put("camiones", List.of());
          modelo.put("error", "No se pudo conectar con el servicio de logística");
          ctx.render("admin/logistica.hbs", modelo);
        }
        default -> {
          e.printStackTrace();
          ctx.redirect("/");
        }
      }
    }
  }

  public void mostrarPanelCamion(Context ctx) {
    System.out.println(">>> panel camion id=" + ctx.pathParam("id"));
    String token = ctx.cookie("session");
    if (token == null || token.isBlank()) {
      ctx.status(401);
      System.err.println(">>> panel: sin cookie de sesión");
      return;
    }
    long id = Long.parseLong(ctx.pathParam("id"));
    try {
      CamionResponse response = clientCamiones.mostrarCamionXid(id);

      CamionDto camion = new CamionDto(response.getCamion());

      Map<String, Object> modelo = new HashMap<>();
      modelo.put("camionSeleccionado", camion);
      modelo.put("entregas", cargarEntregas(token, camion, modelo));

      ctx.render("admin/logistica-panel.hbs", modelo); // solo el fragmento, sin layout

    } catch (StatusRuntimeException e) {
      switch (e.getStatus().getCode()) {
        case UNAUTHENTICATED, PERMISSION_DENIED -> ctx.status(401);
        case UNAVAILABLE, DEADLINE_EXCEEDED -> ctx.status(503);
        case INTERNAL -> ctx.status(500);
        default -> {
          e.printStackTrace();
          ctx.status(500);
        }
      }
      System.err.println("Error en comunicacion: " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      System.err.println(e.getMessage());
      ctx.status(500);
    }
  }

  private List<EntregaDto> cargarEntregas(String token, CamionDto camion, Map<String, Object> modelo) {
    try {
      RutasResponse rr = clientRutas.mostrarRutasXcamion(token, camion.getPatente());
      if (!rr.getExito()) {
        modelo.put("errorRutas", rr.getMensaje());
        return List.of();
      }

      List<EntregaDto> entregas = rr.getRuta().getRutasList().stream()
          .filter(r -> r.getCamionId() == camion.getId())
          .flatMap(r -> r.getEntregas().getEntregaList().stream())
          .map(EntregaDto::fromProto)
          .toList();
      entregas.forEach(e -> System.out.println(
          "DONACION id:" + e.getDonacionId() +
          " BENEFICIARIO id: " + e.getBeneficiarioId()));
      return entregas;
      /*
      return rr.getRuta().getRutasList().stream()
          .filter(r -> r.getCamionId() == camion.getId())
          .flatMap(r -> r.getEntregas().getEntregaList().stream())
          .map(EntregaDto::fromProto)
          .toList();

       */
    } catch (StatusRuntimeException e) {
      System.err.println(">>> mostrarRutas falló: " + e.getStatus());
      modelo.put("errorRutas", "No se pudieron cargar las paradas");
      return List.of();
    }
  }

  public void registrarCamion(@NotNull Context ctx) {
    String patente = ctx.formParam("patente");
    String capacidadVolumen = ctx.formParam("capacidadVolumen");
    String altura = ctx.formParam("altura");
    String capacidadCarga = ctx.formParam("capacidadCarga");
    String estado = ctx.formParam("estado");

    Map<String, Object> modelo = new HashMap<>();

    try {
      CamionRequest request = new CamionRequest();

      request.setPatente(patente);
      request.setCapacidadVolumen(Double.parseDouble(capacidadVolumen));
      request.setAltura(Double.parseDouble(altura));
      request.setCapacidadCarga(Double.parseDouble(capacidadCarga));
      request.setEstado(estado);

      HttpResponse<String> response = new LogisticaHttpClient()
          .registrarCamion(request);

      if (response.statusCode() == 201) {
        ctx.redirect("/admin/camiones?registro=exitoso");
        return;

      } else if (response.statusCode() == 409) {
        modelo.put("error", "Ya existe un camión registrado con esa patente.");

      } else if (response.statusCode() == 400) {
        modelo.put("error", "Los datos ingresados para el camión no son válidos.");

      } else {
        modelo.put("error", "No se pudo completar el registro del camión.");
      }

    } catch (NumberFormatException e) {
      modelo.put("error", "La capacidad, altura y volumen deben ser valores numéricos.");

    } catch (Exception e) {
      modelo.put("error", "No fue posible comunicarse con el servidor.");
    }

    ctx.render("admin/nuevo-camion.hbs", modelo);
  }
  private final GrpcClientLogisticaImplementation grpcLogisticaClient = new GrpcClientLogisticaImplementation();

  public void entregasEnCurso(Context ctx) {
    Long idUsuario = Long.parseLong(ctx.pathParam("idUsuario"));
    List enCurso = grpcLogisticaClient.obtenerEntregasEnCurso(idUsuario);

    String accept = ctx.header("Accept");
    if (accept != null && accept.contains("application/json")) {
      ctx.json(enCurso);
      return;
    }

    UsuarioLogueado usuarioLogueado = ctx.attribute("usuarioLogueado");
    Map<String, Object> model = new HashMap<>();
    model.put("logueado", usuarioLogueado != null);
    model.put("esBeneficiario", usuarioLogueado != null && "BENEFICIARIO".equalsIgnoreCase(String.valueOf(usuarioLogueado.getRol())));
    model.put("idUsuario", idUsuario);
    model.put("enCurso", enCurso);

    ctx.render("entregas-en-curso.hbs", model);
  }

}
