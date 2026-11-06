package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dto.EntidadDTO;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.util.List;

public class EntidadBeneficiariaController implements WithSimplePersistenceUnit {

  private EntidadRepository entidadRepository = EntidadRepository.Instance;

  public void crearEntidad(Context ctx) {
    EntidadDTO dto = ctx.bodyAsClass(EntidadDTO.class);
    EntidadBeneficiaria entidadBeneficiaria = dto.Entidad();

    withTransaction(() ->
      entidadRepository.registrar(entidadBeneficiaria)
    );

    ctx.status(201).json(EntidadDTO.desde(entidadBeneficiaria));
  }

  public EntidadDTO obtenerEntidad(Context ctx) {
    Long id = ctx.pathParamAsClass("id", Long.class).get();

    return EntidadDTO.desde(buscarOFalla(id));
  }

  private EntidadBeneficiaria buscarOFalla(Long id) {
    EntidadBeneficiaria entidad = entidadRepository.obtenerPorId(id);
    if (entidad == null) {
      throw new NotFoundResponse("No existe una entidad con id " + id);
    }
    return entidad;
  }

  public EntidadDTO actualizarEntidad(Context ctx) {
    Long id = ctx.pathParamAsClass("id",Long.class).get();
    EntidadBeneficiaria entidadExistente = buscarOFalla(id);

    EntidadBeneficiaria camposNuevos = ctx.bodyAsClass(EntidadDTO.class).Entidad();

    EntidadBeneficiaria actualizada = withTransaction(() ->
      entidadRepository.actualizarEntidad(entidadExistente, camposNuevos)
    );

    return EntidadDTO.desde(actualizada);

  }

  public void deleteEntidad(Context ctx) {
    Long id = ctx.pathParamAsClass("id",Long.class).get();
    buscarOFalla(id);

    withTransaction(() -> entidadRepository.eliminarPorId(id));
    ctx.status(204);
  }

  public List<EntidadDTO> obtenerEntidades(){
    return entidadRepository.obtenerEntidades().stream().map(EntidadDTO::desde).toList();
  }
}

