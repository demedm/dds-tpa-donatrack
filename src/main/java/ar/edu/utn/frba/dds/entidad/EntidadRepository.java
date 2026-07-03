package ar.edu.utn.frba.dds.entidad;

public interface EntidadRepository {
  EntidadBeneficiaria obtenerPorId(String mailId);
}
