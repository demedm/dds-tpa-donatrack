package ar.edu.utn.frba.dds;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.utn.frba.dds.model.necesidad.Necesidad;
import ar.edu.utn.frba.dds.model.necesidad.Peticion;
import ar.edu.utn.frba.dds.repositories.EntityManagerHelper;
import ar.edu.utn.frba.dds.repositories.NecesidadRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NecesidadTest {

    private Necesidad necesidadNormal;
    private Necesidad necesidadRecurrente;

    @BeforeEach
    void setUp() {
        necesidadNormal = new Necesidad();
        necesidadNormal.setEntidadId("ENT-001");
        necesidadNormal.setDescripcion("Ropa de abrigo");
        necesidadNormal.setTipo(Necesidad.TipoNecesidad.NORMAL);

        necesidadRecurrente = new Necesidad();
        necesidadRecurrente.setEntidadId("ENT-002");
        necesidadRecurrente.setDescripcion("Leche mensual");
        necesidadRecurrente.setTipo(Necesidad.TipoNecesidad.RECURRENTE);
        necesidadRecurrente.setDiasRecurrencia(30);
    }

    @AfterEach
    void terminarTransaccion() {
        // Si la transacción quedó activa al finalizar el test
        if (EntityManagerHelper.getEntityManager().getTransaction().isActive()) {
            EntityManagerHelper.rollback();
        }
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
        Peticion peticion = new Peticion("ARROZ", 50);
        necesidadNormal.agregarPeticion(peticion);

        assertEquals(1, necesidadNormal.getPeticiones().size());
        assertEquals("ARROZ", necesidadNormal.getPeticiones().get(0).getSubclase());
    }

    @Test
    void validarPeticionInvalida() {
        Peticion peticionInvalida = new Peticion("", -10);

        boolean esValida = peticionInvalida.getSubclase() != null 
                && !peticionInvalida.getSubclase().trim().isEmpty() 
                && peticionInvalida.getCantidadRequerida() != null 
                && peticionInvalida.getCantidadRequerida() > 0;

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
        Peticion peticion = new Peticion("CAMPERAS", 20);
        necesidadNormal.agregarPeticion(peticion);

        NecesidadRepository.getInstance().guardar(necesidadNormal);
        EntityManagerHelper.commit();

        EntityManagerHelper.getEntityManager().clear();

        Necesidad recuperada = NecesidadRepository.getInstance().findById(necesidadNormal.getId());

        assertNotNull(recuperada, "La necesidad debe existir en la BD");
        assertEquals("ENT-001", recuperada.getEntidadId());
        assertEquals(1, recuperada.getPeticiones().size(), "Debe guardar la petición en cascada");
        assertEquals("CAMPERAS", recuperada.getPeticiones().get(0).getSubclase());
    }

    @Test
    void testPersistirYConsultarRecurrenteVencida() {
        necesidadRecurrente.setProximoVencimiento(LocalDate.now().minusDays(7));

        NecesidadRepository.getInstance().guardar(necesidadRecurrente);
        
        NecesidadRepository.getInstance().guardar(necesidadNormal);
        
        EntityManagerHelper.commit();

        EntityManagerHelper.getEntityManager().clear();

        List<Necesidad> vencidasEnBD = NecesidadRepository.getInstance().findAllRecurrentesActivasVencidas();

        assertTrue(vencidasEnBD.stream().anyMatch(n -> n.getEntidadId().equals("ENT-002")),
                "Debe encontrar la necesidad recurrente vencida");
        assertFalse(vencidasEnBD.stream().anyMatch(n -> n.getEntidadId().equals("ENT-001")),
                "No debe incluir la necesidad normal");
    }
}