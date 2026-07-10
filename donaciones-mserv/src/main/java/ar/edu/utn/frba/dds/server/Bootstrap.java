package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.BienPerecedero;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.donantes.Identificacion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.model.donantes.PersonaJuridica;
import ar.edu.utn.frba.dds.model.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;

import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bootstrap {
	public static void init() {
		List<Persona> donadores = donadores();
		donadores.forEach((usuario) -> {
			try {
				DonanteRepository.Instance.registrarDonante(usuario);
			} catch (Exception e) {
				System.out.println("No se pudo notificar a " + usuario.getNombreIdentificador());
			}
		});

		List<Donacion> donaciones = donaciones();
		donaciones();
		Persona gaston = donadores.get(0);
		// donaciones.stream().limit(4).forEach((donacion) -> gaston.capturar(pokemon));

	}

	private static List<Donacion> donaciones() {
    Subcategoria subcat = new Subcategoria(null, "Arroz");

    List<Bien> bienes = new ArrayList<>(List.of(
        new BienPerecedero(subcat, "foto.jpg", "Arroz", new Date()),
        new BienPerecedero(subcat, "foto.jpg", "Arroz", new Date()),
        new BienPerecedero(subcat, "foto.jpg", "Arroz", new Date()),
        new BienPerecedero(subcat, "foto.jpg", "Arroz", new Date()),
        new BienPerecedero(subcat, "foto.jpg", "Arroz", new Date())
    ));

    Donacion donacion = new Donacion("Donación de alimentos", bienes, null);
    donacion.setId(1);
    DonacionesRepository.Instance.agregarDonacion(donacion);

    return List.of(donacion);
}

	private static List<Persona> donadores() {
		PersonaFisica maria = new PersonaFisica();
		maria.setNombreIdentificador("Maria A");
		maria.setMail(new Mail("mariaa@gmail.com"));
		maria.setMedioPreferido(maria.getMail());
		maria.setEdad(46);
		maria.setIdentificacion(new Identificacion(TipoDocumento.DNI, "30111222"));
		PersonaJuridica org = new PersonaJuridica();
		org.setNombreIdentificador("Patitas");
		org.setMail(new Mail("patitas@org.com"));
		org.setMedioPreferido(org.getMail());
		org.setRubro("alimenticio");
		org.setIdentificacion(new Identificacion(TipoDocumento.CUIT, "30712345671"));
		List<MedioContacto> mails = new ArrayList<>();
		mails.add(new Mail("marior@gmail.com"));
		mails.add(new Mail("comidas@outlook.com"));
		org.setListaContactos(mails);
		return Arrays.asList(maria, org);
	}
}