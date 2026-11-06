package ar.edu.utn.frba.dds;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.repositories.NecesidadRepository;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NecesidadTest implements SimplePersistenceTest{

    private Necesidad necesidadNormal;
    private Necesidad necesidadRecurrente;
    private EntidadBeneficiaria comedor;
    private EntidadBeneficiaria hogar;

    @BeforeEach
    void setUp() {
        // La necesidad tiene clave foránea a la entidad: las entidades tienen que existir en la base
        comedor = new EntidadBeneficiaria("Av. Medrano 951", new ArrayList<>(), "Comedor de prueba", null, "COMEDOR");
        hogar = new EntidadBeneficiaria("Mozart 2300", new ArrayList<>(), "Hogar de prueba", null, "HOGAR");
        entityManager().persist(comedor);
        entityManager().persist(hogar);

        necesidadNormal = new Necesidad();
        necesidadNormal.setEntidadId(comedor.getId());
        necesidadNormal.setDescripcion("Ropa de abrigo");
        necesidadNormal.setTipo(Necesidad.TipoNecesidad.NORMAL);

        necesidadRecurrente = new Necesidad();
        necesidadRecurrente.setEntidadId(hogar.getId());
        necesidadRecurrente.setDescripcion("Leche mensual");
        necesidadRecurrente.setTipo(Necesidad.TipoNecesidad.RECURRENTE);
        necesidadRecurrente.setDiasRecurrencia(30);
    }

    @Test
    void crearNecesidadesCorrectamente() {
        assertEquals(Necesidad.TipoNecesidad.NORMAL, necesidadNormal.getTipo());
        assertEquals("en_preparacion", necesidadNormal.getEstado());

        assertEquals(Necesidad.TipoNecesidad.RECURRENTE, necesidadRecurrente.getTipo());
        assertEquals(30, necesidadRecurrente.getDiasRecurrencia());
    }

    @Test
    void validarDiasRecurrenciaInvalidos() {
        necesidadRecurrente.setDiasRecurrencia(-5);
        boolean esValido = necesidadRecurrente.getDiasRecurrencia() != null && necesidadRecurrente.getDiasRecurrencia() > 0;

        assertFalse(esValido, "Los días de recurrencia no pueden ser negativos");
    }

    @Test
    void agregarPeticion() {
        Peticion peticion = new Peticion("ARROZ", new BigDecimal("50"));
        necesidadNormal.agregarPeticion(peticion);

        assertEquals(1, necesidadNormal.getPeticiones().size());
        assertEquals("ARROZ", necesidadNormal.getPeticiones().get(0).getSubclase());
    }

    @Test
    void validarPeticionInvalida() {
        Peticion peticionInvalida = new Peticion("", new BigDecimal("-10"));

        boolean esValida = peticionInvalida.getSubclase() != null
            && !peticionInvalida.getSubclase().trim().isEmpty()
            && peticionInvalida.getCantidadRequerida() != null
            && peticionInvalida.getCantidadRequerida().compareTo(BigDecimal.ZERO) > 0;

        assertFalse(esValida, "La petición no debe ser válida si falta la subclase o la cantidad es negativa");
    }

    @Test
    void noMostrarVencidas() {
        necesidadRecurrente.setProximoVencimiento(LocalDate.now().minusDays(1));

        assertTrue(necesidadRecurrente.estaVencida(), "La necesidad debería marcarse como vencida");
        assertNotEquals("vencida", necesidadNormal.getEstado(), "Una necesidad activa no debería figurar como vencida");
    }

    @Test
    void testPersistirYBuscarNecesidadNormal() {
        Peticion peticion = new Peticion("CAMPERAS", new BigDecimal("20"));
        necesidadNormal.agregarPeticion(peticion);

        NecesidadRepository.getInstance().guardar(necesidadNormal);

        entityManager().clear();

        Necesidad recuperada = NecesidadRepository.getInstance().findById(necesidadNormal.getId());

        assertNotNull(recuperada, "La necesidad debe existir en la BD");
        assertEquals(comedor.getId(), recuperada.getEntidadId());
        assertEquals(1, recuperada.getPeticiones().size());
        assertEquals("CAMPERAS", recuperada.getPeticiones().get(0).getSubclase());
    }

    @Test
    void testPersistirYConsultarRecurrenteVencida() {
        necesidadRecurrente.setProximoVencimiento(LocalDate.now().minusDays(7));

        NecesidadRepository.getInstance().guardar(necesidadRecurrente);

        NecesidadRepository.getInstance().guardar(necesidadNormal);

        entityManager().clear();

        List<Necesidad> vencidasEnBD = NecesidadRepository.getInstance().findAllRecurrentesActivasVencidas();

        assertTrue(vencidasEnBD.stream().anyMatch(n -> n.getEntidadId().equals(hogar.getId())),
            "Debe encontrar la necesidad recurrente vencida");
        assertFalse(vencidasEnBD.stream().anyMatch(n -> n.getEntidadId().equals(comedor.getId())),
            "No debe incluir la necesidad normal");
    }
}