// ar/edu/utn/frba/dds/controllers/EstadoEntregaController.java   [NUEVO]
package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.casosDeUso.CambioEstadoDonacion;
import ar.edu.utn.frba.dds.dto.CambioEstadoDTO;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.notificaciones.Destinatario;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import ar.edu.utn.frba.dds.exceptions.DonacionInexistenteException;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

  public class EstadoEntregaController {

    private final CambioEstadoDonacion cambioEstado;

    public EstadoEntregaController() {
      this(CambioEstadoDonacion.Instance);
    }

    public EstadoEntregaController(CambioEstadoDonacion cambioEstado) {
      this.cambioEstado = cambioEstado;
    }

    public void cambiarEstado(Context context) {
      Long donacionId = context.pathParamAsClass("id", Long.class).get();
      CambioEstadoDTO cambio = context.bodyAsClass(CambioEstadoDTO.class);

      try {
        cambioEstado.aplicar(donacionId, cambio);
        context.status(200).json(cambio);
      } catch (DonacionInexistenteException e) {
        context.status(404).result(e.getMessage());
      }
    }
  }