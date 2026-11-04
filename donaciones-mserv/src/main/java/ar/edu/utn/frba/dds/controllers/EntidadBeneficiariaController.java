package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.util.List;

public class EntidadBeneficiariaController {

  private EntidadRepository entidadRepository = EntidadRepository.Instance;

  public void crearEntidad(Context ctx) {
    EntidadDTO dto = ctx.bodyAsClass(EntidadDTO.class);
    EntidadBeneficiaria entidadBeneficiaria = dto.Entidad();

    entidadRepository.registrar(entidadBeneficiaria);
    ctx.status(201).json(EntidadDTO.desde(entidadBeneficiaria));
  }

  public EntidadDTO obtenerEntidad(Context ctx) {
    Long id = ctx.pathParamAsClass("id",Long.class).get();
    EntidadBeneficiaria entidad = entidadRepository.obtenerPorId(id);
    if (entidad == null) {
      throw new NotFoundResponse("No existe una entidad con id " + id);
    }
    return EntidadDTO.desde(entidad);
  }

  public EntidadDTO actualizarEntidad(Context ctx) {
    Long id = ctx.pathParamAsClass("id",Long.class).get();
    EntidadBeneficiaria entidadExistente = entidadRepository.obtenerPorId(id);
    if (entidadExistente == null) {
      throw new NotFoundResponse("No existe una entidad con id " + id);
    }

    EntidadDTO dto = ctx.bodyAsClass(EntidadDTO.class);
    EntidadBeneficiaria entidadBeneficiaria = dto.Entidad();
    return EntidadDTO.desde(entidadRepository.actualizarEntidad(entidadExistente, entidadBeneficiaria));

  }

  public void deleteEntidad(Context ctx) {
    Long id = ctx.pathParamAsClass("id",Long.class).get();
    EntidadBeneficiaria entidadExistente = entidadRepository.obtenerPorId(id);
    if (entidadExistente == null) {
      throw new NotFoundResponse("No existe una entidad con ese id " + id);
    }
    boolean eliminado = entidadRepository.eliminarPorId(id);
    ctx.status(eliminado ? 204 : 404);
  }

  public List<EntidadDTO> obtenerEntidades(){
    return entidadRepository.obtenerEntidades().stream().map(EntidadDTO::desde).toList();
  }
}

