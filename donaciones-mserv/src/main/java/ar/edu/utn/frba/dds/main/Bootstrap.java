package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.PorVencimiento;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Bienes.UnidadMedida;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.donantes.Identificacion;
import ar.edu.utn.frba.dds.model.donantes.MedioPreferido;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.model.donantes.PersonaJuridica;
import ar.edu.utn.frba.dds.model.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.model.usuarios.Rol;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import ar.edu.utn.frba.dds.repositories.usuarios.UsuariosRepositorio;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bootstrap {
	public static void init() {
		WithSimplePersistenceUnit persistence = new WithSimplePersistenceUnit() {};
		persistence.withTransaction(Bootstrap::crearNecesidadesConNecesidades);

		// registro de algunos usuarios de prueba
		usuarios().forEach(UsuariosRepositorio.Instance::registrar);


		// La donación necesita un donante ya guardado: lo registro y lo vuelvo a leer de la base
		persistence.withTransaction( () ->
			donadores().forEach(DonanteRepository.Instance::registrarDonante));

			Persona maria = DonanteRepository.Instance.buscarPorEmail("mariaa@gmail.com").orElseThrow();

		donaciones(maria);

	}

	private static List<Usuario> usuarios(){
		Usuario usuario1 = new Usuario(Rol.ADMIN,
				"neme@email.com", "nemesito");
		Usuario usuario2 = new Usuario(Rol.DONANTE,
				"mariamendoza@email.com", "gatos123");
		Usuario usuario3 = new Usuario(Rol.BENEFICIARIO,
				"patitasfelices@email.com", "#patitas12");

		return List.of(usuario1, usuario2, usuario3);
	}

	private static List<Donacion> donaciones(Persona donante) {
		// Subcategorías del catálogo: Bien no las guarda en cascada, así que se persisten antes
		Subcategoria subcatArroz = new Subcategoria(Categoria.ALIMENTOS, "ARROZ", UnidadMedida.UNIDADES);
		Subcategoria subcatFideos = new Subcategoria(Categoria.ALIMENTOS, "FIDEOS", UnidadMedida.UNIDADES);

		WithSimplePersistenceUnit persistencia = new WithSimplePersistenceUnit() {};

		Donacion donacion = persistencia.withTransaction(() -> {
			persistencia.entityManager().persist(subcatArroz);
			persistencia.entityManager().persist(subcatFideos);

			// Bienes: la cantidad está en cada bien y la segmentada la suma
			LocalDate vencimiento = LocalDate.now().plusMonths(6);
			Bien arrozBien = new Bien(subcatArroz, "foto.jpg", "Arroz Paquete 1kg", 10, new PorVencimiento(vencimiento));
			Bien fideosBien = new Bien(subcatFideos, "foto.jpg", "Fideos Matarazzo 500g", 5, new PorVencimiento(vencimiento));

			// Las segmentadas las arma la donación al crearse (una de arroz y una de fideos)
			List<Bien> bienes = new ArrayList<>(List.of(arrozBien, fideosBien));
			Donacion nueva = new Donacion("Donación de alimentos varios", bienes, donante);

			DonacionesRepository.Instance.guardar(nueva);
			return nueva;
		});

		System.out.println("Donaciones y Segmentadas inicializadas correctamente.");

		return List.of(donacion);
	}

	private static List<Persona> donadores() {
		PersonaFisica maria = new PersonaFisica();
		maria.setNombreIdentificador("Maria A");
		maria.setMail(new Mail("mariaa@gmail.com"));
		maria.setMedioPreferido(MedioPreferido.MAIL);
		maria.setEdad(46);
		maria.setIdentificacion(new Identificacion(TipoDocumento.DNI, "30111222"));
		PersonaJuridica org = new PersonaJuridica();
		org.setNombreIdentificador("Patitas");
		org.setMail(new Mail("patitas@org.com"));
		org.setMedioPreferido(MedioPreferido.MAIL);
		org.setRubro("alimenticio");
		org.setIdentificacion(new Identificacion(TipoDocumento.CUIT, "30712345671"));
		return Arrays.asList(maria, org);
	}

	private static void crearNecesidadesConNecesidades() {

		EntidadBeneficiaria comedor = new EntidadBeneficiaria(
				"Av. Medrano 951", new ArrayList<>(), "Comedor Los Pibes", null, "COMEDOR"
		);

		Necesidad nec1 = new Necesidad();
		nec1.setDescripcion("Comida para invierno");
		nec1.setTipo(Necesidad.TipoNecesidad.NORMAL);

		Peticion pet1 = new Peticion("ARROZ", 100);
		Peticion pet2 = new Peticion("FIDEOS", 50);
		nec1.agregarPeticion(pet1);
		nec1.agregarPeticion(pet2);
		comedor.agregarNecesidad(nec1);
		EntidadRepository.Instance.registrar(comedor);

		EntidadBeneficiaria hogar = new EntidadBeneficiaria(
				"Mozart 2300", new ArrayList<>(), "Hogar San José", null, "HOGAR");

		Necesidad nec2 = new Necesidad();
		nec2.setDescripcion("Medicinas semanales");
		nec2.setTipo(Necesidad.TipoNecesidad.RECURRENTE);
		nec2.setDiasRecurrencia(7);
		nec2.setProximoVencimiento(LocalDate.now().minusDays(7));
		Peticion pet3 = new Peticion("IBUPROFENO", 30);
		nec2.agregarPeticion(pet3);
		hogar.agregarNecesidad(nec2);
		EntidadRepository.Instance.registrar(hogar);

    }
}