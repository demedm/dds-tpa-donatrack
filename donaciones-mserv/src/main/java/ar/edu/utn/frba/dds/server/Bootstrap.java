package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.BienPerecedero;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.donantes.Identificacion;
import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaFisica;
import ar.edu.utn.frba.dds.model.donantes.PersonaJuridica;
import ar.edu.utn.frba.dds.model.donantes.TipoDocumento;
import ar.edu.utn.frba.dds.model.medioscontacto.Mail;
import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.repositories.DonanteRepository;
import ar.edu.utn.frba.dds.repositories.NecesidadRepository;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;

import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bootstrap {
	public static void init() {
		// List<Persona> donadores = donadores();
		// donadores.forEach((usuario) -> {
		// 	try {
		// 		DonanteRepository.Instance.registrarDonante(usuario);
		// 	} catch (Exception e) {
		// 		System.out.println("No se pudo notificar a " + usuario.getNombreIdentificador());
		// 	}
		// });

		List<Necesidad> necesidades = crearNecesidades(); //Cargo las necesidades para probar 
			necesidades.forEach(n -> {
				try {
					NecesidadRepository.getInstance().guardar(n);
					System.out.println("Necesidad guardada: " + n.getDescripcion());
				} catch (Exception e) {
					System.out.println("Error guardando necesidad: " + e.getMessage());
					e.printStackTrace();
				}
			});

		List<Donacion> donaciones = donaciones();
		donaciones();
		// Persona gaston = donadores.get(0);
		// // donaciones.stream().limit(4).forEach((donacion) -> gaston.capturar(pokemon));

	}

	private static List<Donacion> donaciones() {
        // Subcategorías asociadas a la categoría ALIMENTOS
        Subcategoria subcatArroz = new Subcategoria(Categoria.ALIMENTOS, "ARROZ");
        Subcategoria subcatFideos = new Subcategoria(Categoria.ALIMENTOS, "FIDEOS");

        // Bienes asociados
        Bien arrozBien = new BienPerecedero(subcatArroz, "foto.jpg", "Arroz Paquete 1kg", new Date());
        Bien fideosBien = new BienPerecedero(subcatFideos, "foto.jpg", "Fideos Matarazzo 500g", new Date());

        List<Bien> bienes = new ArrayList<>(List.of(arrozBien, fideosBien));
        Donacion donacion = new Donacion("Donación de alimentos varios", bienes, null);
        donacion.setId("DON-001");

        
        // Segmentada 1: 10 paquetes de Arroz en depósito
        DonacionSegmentada segArroz = new DonacionSegmentada(10, subcatArroz, arrozBien);
        
        // Segmentada 2: 5 paquetes de Fideos en depósito
        DonacionSegmentada segFideos = new DonacionSegmentada(5, subcatFideos, fideosBien);

        donacion.setDonacionesSegmentadas(List.of(segArroz, segFideos));

        DonacionesRepository.Instance.guardar(donacion);

        System.out.println("Donaciones y Segmentadas inicializadas correctamente.");

        return List.of(donacion);

    // Donacion donacion = new Donacion("Donación de alimentos", bienes, null);
    // DonacionesRepository.Instance.guardar(donacion);

    // return List.of(donacion);
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

	private static List<Necesidad> crearNecesidades() {
        Necesidad nec1 = new Necesidad();
        nec1.setEntidadId("ent-001");
        nec1.setDescripcion("Comida para invierno");
        nec1.setTipo(Necesidad.TipoNecesidad.NORMAL);
        
        Peticion pet1 = new Peticion("ARROZ", 100);
        Peticion pet2 = new Peticion("FIDEOS", 50);
        nec1.agregarPeticion(pet1);
        nec1.agregarPeticion(pet2);
        
        Necesidad nec2 = new Necesidad();
        nec2.setEntidadId("ent-002");
        nec2.setDescripcion("Medicinas semanales");
        nec2.setTipo(Necesidad.TipoNecesidad.RECURRENTE);
        nec2.setDiasRecurrencia(7);
        nec2.setProximoVencimiento(LocalDate.now().minusDays(7));
        
        Peticion pet3 = new Peticion("IBUPROFENO", 30);
        nec2.agregarPeticion(pet3);
        
        return List.of(nec1, nec2);
    }
}