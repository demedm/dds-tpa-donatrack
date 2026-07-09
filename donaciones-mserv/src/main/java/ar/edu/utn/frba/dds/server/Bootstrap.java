package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.Bienes.Donacion;
import ar.edu.utn.frba.dds.model.donantes.Identificacion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.model.donantes.TipoPersona;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Bootstrap {
	public static void init() {
		List<Persona> donadores = donadores();
		donadores.forEach((usuario) -> DonanteRepository.Instance.registrar(usuario));

		List<Donacion> donaciones = donaciones();

		Persona gaston = donadores.get(0);
		// donaciones.stream().limit(4).forEach((donacion) -> gaston.capturar(pokemon));

	}

	private static List<Donacion> donaciones() {
		return null;
	}

	private static List<Persona> donadores() {
		return null;
	}
}