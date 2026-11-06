package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.GrpcClientDonaciones.GrpcClientDonacionesImplementation;import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.model.EstadoRuta;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.fallaentrega.MotivoFallo;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.RutaDto;
import io.javalin.http.Context;
import ar.edu.utn.frba.dds.model.Camion;
import java.time.LocalDateTime;
import java.util.List;

public class RutaController {
  private final GrpcClientDonacionesImplementation cliente =
      new GrpcClientDonacionesImplementation();

  public Ruta showRuta(Context ctx) {
    var id = Long.parseLong(ctx.pathParam("id"));
    return RutaRepositorio.Instance.buscarPorId(id);
  }

  public Entrega showEntrega(Context ctx) {
    var idEntrega = Long.parseLong(ctx.pathParam("idEntrega"));
    return EntregaRepositorio.Instance.buscarPorId(idEntrega);
  }

  public List<Entrega> showEntregas(Context ctx) {
    var idRuta = Long.parseLong(ctx.pathParam("id"));
    return EntregaRepositorio.Instance.buscarEntregasDeRuta(idRuta);
  }

  public List<Ruta> showAllRutas() {
    return RutaRepositorio.Instance.mostrarTodos();
  }

  public void postRuta(Context ctx) {
    Ruta ruta = validarRuta(ctx);
    Ruta rutaGuardada = RutaRepositorio.Instance.registrar(ruta);

    if (rutaGuardada.getEntregas() != null) {
      for (Entrega entrega : rutaGuardada.getEntregas()) {
        cliente.notificarCambioEstado(entrega.getDonacionId(), "PLANIFICADA");
      }
    }

    ctx.status(201).json(rutaGuardada);
  }

  public Ruta postEntrega(Context ctx) {
    Long idRuta = Long.parseLong(ctx.pathParam("idRuta"));
    Entrega entrega = ctx.bodyValidator(Entrega.class)
        .check(e -> e.getId() != null,
            "El id de la entrega es obligatorio")
        .check(e -> e.getDireccion() != null && !e.getDireccion().isBlank(),
            "La dirección es obligatoria")
        .check(e -> e.getDonacionId() != null,
            "El id de la donación debe ser mayor a 0")
        .get();
    Ruta ruta = RutaRepositorio.Instance.buscarPorId(idRuta);
    if (ruta == null) {
      ctx.status(404);
      return null;
    }
    EntregaRepositorio.Instance.registrar(entrega);
    ctx.status(201);
    ruta.agregarEntrega(entrega);
    return ruta;
  }

  public void patchRuta(Context ctx) {
    Long idRuta = Long.parseLong(ctx.pathParam("id"));
    Ruta ruta = RutaRepositorio.Instance.buscarPorId(idRuta);
    if (ruta == null) {
      ctx.status(404);
      return;
    }

    RutaDto rutaModificada = ctx.bodyAsClass(RutaDto.class);

    var rutaActualizada = RutaRepositorio.Instance.actualizarRuta(ruta, rutaModificada);
    if (rutaActualizada == null) {
      ctx.status(400);
      return;
    }

    if (rutaModificada.getEstado() != null) {
      switch (rutaModificada.getEstado()) {
        case EN_CURSO -> {
          rutaActualizada.getEntregas().forEach(e -> {
            e.marcarComoIniciada();
            EntregaRepositorio.Instance.actualizar(e);
            cliente.notificarInicioDeRuta(e, rutaActualizada);
          });
        }
        case CANCELADA -> {
          MotivoFallo imprevisto = MotivoFallo.IMPREVISTO_LOGISTICO;
          rutaActualizada.getEntregas().stream()
              .filter(e -> !e.getEstado().name().equals("ENTREGADA"))
              .forEach(e -> {
                e.marcarComoFallida(imprevisto);
                EntregaRepositorio.Instance.actualizar(e);
                cliente.notificarFallaDeEntrega(
                    e.getDonacionId(),
                    imprevisto.getDescripcionMotivo(),
                    imprevisto.esReplanificable()
                );
              });
        }
        case FINALIZADA -> {
          rutaActualizada.getEntregas().stream()
              .filter(e -> e.getEstado().name().equals("PENDIENTE"))
              .forEach(e -> cliente.notificarCambioEstado(e.getDonacionId(), "PENDIENTE"));
        }
        case NO_INICIADA -> {
        }
      }
    }

    ctx.status(200);
    ctx.json(rutaActualizada);
  }

  public void deleteRuta(Context ctx) {
    Long idRuta = Long.parseLong(ctx.pathParam("id"));
    Ruta ruta = RutaRepositorio.Instance.buscarPorId(idRuta);

    RutaRepositorio.Instance.eliminarRutaYEntregas(ruta);
    ctx.status(200);
  }

  private Ruta validarRuta(Context ctx) {
    return ctx.bodyValidator(Ruta.class)
        .check(r -> r.getCamion() != null,
            "El camión es obligatorio")
        .check(r -> r.getEntregas() != null && !r.getEntregas().isEmpty(),
            "La ruta debe contener al menos una entrega")
        .check(r -> r.getEntregas().stream().allMatch(e ->
                e.getDireccion() != null && !e.getDireccion().isBlank()),
            "Todas las entregas deben tener una dirección")
        .check(r -> r.getEntregas().stream().allMatch(e ->
                e.getDonacionId() != null),
            "Todas las entregas deben tener un id de donación válido")
        .check(r -> r.getChofer() != null,
            "Las rutas tienen que tener asignado un chofer")
        .get();
  }

  public void iniciarRuta(Context ctx) {
    Long idRuta = Long.parseLong(ctx.pathParam("id"));
    Ruta ruta = RutaRepositorio.Instance.buscarPorId(idRuta);

    if (ruta == null) {
      ctx.status(404).result("Ruta no encontrada");
      return;
    }
    ruta.iniciarRuta();
    if (ruta.getEntregas() != null) {
      for (Entrega entrega : ruta.getEntregas()) {
        cliente.notificarInicioDeRuta(entrega, ruta);
      }
    }

    ctx.status(200).result("Ruta iniciada. Notificaciones despachadas a Donaciones.");
  }

  public void finalizarRuta(Context ctx) {
    Long idRuta = Long.parseLong(ctx.pathParam("id"));
    Ruta ruta = RutaRepositorio.Instance.buscarPorId(idRuta);

    if (ruta == null) {
      ctx.status(404).result("Ruta no encontrada");
      return;
    }

    ruta.finalizarRuta();

    RutaDto dto = new RutaDto();
    dto.setEstado(EstadoRuta.FINALIZADA);
    RutaRepositorio.Instance.actualizarRuta(ruta, dto);

    if (ruta.getEntregas() != null) {
      for (Entrega entrega : ruta.getEntregas()) {
        if (entrega.getEstado().name().equals("PENDIENTE")) {
          cliente.notificarCambioEstado(entrega.getDonacionId(), "PENDIENTE");
        }
      }
    }

    ctx.status(200).result("Ruta finalizada. Pendientes devueltas al depósito.");
  }

  public void recepcionarEntrega(Context ctx) {
    Long idEntrega = Long.parseLong(ctx.pathParam("idEntrega"));
    Entrega entrega = EntregaRepositorio.Instance.buscarPorId(idEntrega);

    if (entrega == null) {
      ctx.status(404).result("Entrega no encontrada");
      return;
    }

    String urlFoto = ctx.formParam("fotoUrl");
    if (urlFoto != null && !urlFoto.isBlank()) {
      entrega.cargarFoto(urlFoto);
    }

    entrega.marcarComoEntregada(camionDe(entrega), LocalDateTime.now());

    EntregaRepositorio.Instance.actualizar(entrega);

    boolean notificacionExitosa = cliente.notificarEntregaExitosa(entrega);

    if (notificacionExitosa) {
      ctx.status(200).result("Entrega recepcionada con éxito y Donaciones notificada.");
    } else {
      ctx.status(502).result(
          "Advertencia: La entrega se guardó en Logística, pero Donaciones rechazó la actualización.");
    }
  }

  private Camion camionDe(Entrega entrega) {
    return entrega.getRuta() != null ? entrega.getRuta().getCamion() : null;
  }
  public void rechazarEntrega(Context ctx) {
    Long idEntrega = Long.parseLong(ctx.pathParam("idEntrega"));

    Entrega entrega = EntregaRepositorio.Instance.buscarPorId(idEntrega);
    if (entrega == null) {
      ctx.status(404).result("Entrega no encontrada.");
      return;
    }

    if (!entrega.getEstado().name().equals("EN_TRASLADO")) {
      ctx.status(400).result("Solo se puede rechazar una entrega que se encuentra EN_TRASLADO.");
      return;
    }

    entrega.marcarComoNoRecepcionada();
    MotivoFallo motivo = MotivoFallo.NO_RECEPCIONADA;

    String motivoExtra = ctx.formParam("motivo");
    String textoMotivo = (motivoExtra != null && !motivoExtra.isBlank())
        ? motivoExtra
        : motivo.getDescripcionMotivo();

    EntregaRepositorio.Instance.actualizar(entrega);

    boolean notificacionExitosa = cliente.notificarFallaDeEntrega(
        entrega.getDonacionId(),
        textoMotivo,
        motivo.esReplanificable()
    );

    if (notificacionExitosa) {
      ctx.status(200).result("Entrega marcada como NO_RECIBIDA y Donaciones notificada para su replanificación.");
    } else {
      ctx.status(502).result("Advertencia: La entrega se marcó como NO_RECIBIDA en Logística, pero Donaciones rechazó la actualización.");
    }
  }
}