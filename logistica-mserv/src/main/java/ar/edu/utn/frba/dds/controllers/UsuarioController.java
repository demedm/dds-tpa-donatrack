package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.GrpcClientDonaciones.GrpcClientDonacionesImplementation;
import ar.edu.utn.frba.dds.model.*;
import ar.edu.utn.frba.dds.model.fallaentrega.MotivoFallo;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.ConfirmacionEntregaDto;
import ar.edu.utn.frba.dds.scripts.dto.EntregaEnCursoDto;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class UsuarioController {
  private final GrpcClientDonacionesImplementation clienteDonaciones =
          new GrpcClientDonacionesImplementation();

  public void showUsuario(Context ctx) {
    var idUsuario = Long.parseLong(ctx.pathParam("id"));
    if (idUsuario != 0) {
      ctx.status(400);
      return;
    }
    var usuario = UsuarioRepositorio.Instance.buscarPorId(idUsuario);
    if (usuario == null) {
      ctx.status(404);
      return;
    }
    ctx.status(200);
    ctx.json(usuario);
  }

  public void confirmarEntrega(Context ctx) {
    var idUsuario = Long.parseLong(ctx.pathParam("idUsuario"));
    var idEntrega = Long.parseLong(ctx.pathParam("id"));
    var confirmacion = ctx.bodyAsClass(ConfirmacionEntregaDto.class);

    var entidad = UsuarioRepositorio.Instance.buscarEntidadBeneficiariaPorId(idUsuario);
    var entrega = UsuarioRepositorio.Instance.confirmarEntrega(entidad, idEntrega, confirmacion);


    if (entrega.getEstado() == EstadoEntrega.ENTREGADA) {
      clienteDonaciones.notificarEntregaExitosa(entrega);
    }

    ctx.status(200);
    ctx.json(entrega);
  }

  public void marcarEntregaComoNoRecepcionada(Context ctx) {
    var idUsuario = Long.parseLong(ctx.pathParam("idUsuario"));
    var idEntrega = Long.parseLong(ctx.pathParam("id"));
    var entidad = UsuarioRepositorio.Instance.buscarEntidadBeneficiariaPorId(idUsuario);
    var entrega = UsuarioRepositorio.Instance.noRecepcionaEntrega(entidad, idEntrega);

    if (entrega.getEstado() == EstadoEntrega.NO_RECIBIDA) {
      MotivoFallo motivo = MotivoFallo.NO_RECEPCIONADA;
      clienteDonaciones.notificarFallaDeEntrega(
              entrega.getDonacionId(),
              motivo.getDescripcionMotivo(),
              motivo.esReplanificable()
      );
    }

    ctx.status(200);
    ctx.json(entrega);
  }

  public void iniciarRuta(Context ctx) {
    var idUsuario = Long.parseLong(ctx.pathParam("idUsuario"));
    var idRuta = Long.parseLong(ctx.pathParam("id"));
    var chofer = UsuarioRepositorio.Instance.buscarChoferPorId(idUsuario);
    var ruta = RutaRepositorio.Instance.iniciarRuta(chofer, idRuta);

    if (ruta == null) {
      ctx.status(400);
      return;
    }

    if (ruta.getEntregas() != null) {
      for (Entrega entrega : ruta.getEntregas()) {
        clienteDonaciones.notificarInicioDeRuta(entrega, ruta);
      }
    }

    ctx.status(200);
    ctx.json(ruta);
  }

  public void guardarUsuario(@NotNull Context ctx) {

  }

  public void entregasEnCurso(Context ctx) {
    var idUsuario = Long.parseLong(ctx.pathParam("idUsuario"));

    var usuario = UsuarioRepositorio.Instance.buscarPorId(idUsuario);
    if (usuario == null) {
      ctx.status(404).result("Usuario o Entidad Beneficiaria no encontrada");
      return;
    }

    var entregas = EntregaRepositorio.Instance.buscarEnTrasladoDeEntidad(idUsuario);

    List<EntregaEnCursoDto> enCurso = entregas.stream().map(entrega -> {
      Ruta ruta = RutaRepositorio.Instance.buscarRutaDeEntrega(entrega.getId());
      Camion camion = (ruta != null) ? ruta.getCamion() : null;

      Ubicacion ubicacion = null;
      if (camion != null) {
        ubicacion = CamionRepositorio.Instance.verUbicacionDeCamion(camion);
      }

      return new EntregaEnCursoDto(entrega, ubicacion);
    }).toList();

    // 4. Negociación de contenido con Accept header
    String accept = ctx.header("Accept");

    if (accept != null && accept.contains("text/html")) {
      ctx.render("entregas-en-curso.hbs", Map.of("idUsuario", idUsuario, "enCurso", enCurso));
    } else {
      ctx.json(enCurso);
    }
  }
}
