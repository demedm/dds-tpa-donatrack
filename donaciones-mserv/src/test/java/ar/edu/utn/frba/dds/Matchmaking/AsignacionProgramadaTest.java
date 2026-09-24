package ar.edu.utn.frba.dds.Matchmaking;

import ar.edu.utn.frba.dds.model.Bienes.Bien;
import ar.edu.utn.frba.dds.model.Bienes.BienPerecedero;
import ar.edu.utn.frba.dds.model.Bienes.Categoria;
import ar.edu.utn.frba.dds.model.Bienes.Subcategoria;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import ar.edu.utn.frba.dds.model.entidad.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.DonacionesRepository;
import ar.edu.utn.frba.dds.repositories.EntidadRepository;
import ar.edu.utn.frba.dds.utils.AsignacionCronScheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;
import org.quartz.listeners.JobListenerSupport;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


public class AsignacionProgramadaTest {

  private Scheduler scheduler;

  @BeforeEach
  void crearScheduler() throws Exception {
    Properties config = new Properties();
    config.setProperty("org.quartz.scheduler.instanceName", "test-" + UUID.randomUUID());
    config.setProperty("org.quartz.threadPool.threadCount", "1");
    scheduler = new StdSchedulerFactory(config).getScheduler();

    AsignacionCronScheduler.programarEn(scheduler);
  }

  @AfterEach
  void apagarScheduler() throws Exception {
    scheduler.shutdown(true);
  }

  private CronTrigger triggerProgramado() throws Exception {
    return (CronTrigger) scheduler.getTriggersOfJob(AsignacionCronScheduler.JOB_KEY).get(0);
  }

  // horario sin esperar a que sean las 3

  @Nested
  class Horario {

    private CronExpression horario;
    private TimeZone zona;

    @BeforeEach
    void setUp() throws Exception {
      CronTrigger trigger = triggerProgramado();
      zona = trigger.getTimeZone();
      horario = new CronExpression(trigger.getCronExpression());
      horario.setTimeZone(zona);
    }

    @Test
    void siSonLasDosDeLaTardeCorreALasTresDeLaMadrugadaSiguiente() {
      assertEquals(fecha("2026-09-24T03:00"), horario.getNextValidTimeAfter(fecha("2026-09-23T14:00")));
    }

    @Test
    void siEsDeMadrugadaAntesDeLasTresCorreEseMismoDia() {
      assertEquals(fecha("2026-09-23T03:00"), horario.getNextValidTimeAfter(fecha("2026-09-23T01:30")));
    }

    @Test
    void despuesDeCorrerVuelveACorrerAlDiaSiguiente() {
      assertEquals(fecha("2026-09-24T03:00"), horario.getNextValidTimeAfter(fecha("2026-09-23T03:00")));
    }

    @Test
    void soloLasTresEnPuntoEsHorarioDeEjecucion() {
      assertTrue(horario.isSatisfiedBy(fecha("2026-09-23T03:00")));
      assertFalse(horario.isSatisfiedBy(fecha("2026-09-23T03:02")));
      assertFalse(horario.isSatisfiedBy(fecha("2026-09-23T15:00")));
    }

    @Test
    void laProximaEjecucionProgramadaEsALasTres() throws Exception {
      Date proxima = triggerProgramado().getNextFireTime();

      LocalDateTime cuando = LocalDateTime.ofInstant(proxima.toInstant(), zona.toZoneId());
      assertEquals(3, cuando.getHour());
      assertEquals(0, cuando.getMinute());
    }

    private Date fecha(String fechaHora) {
      return Date.from(LocalDateTime.parse(fechaHora).atZone(zona.toZoneId()).toInstant());
    }
  }

  // ---------- La ejecucion: Quartz de verdad con los repositorios ----------

  @Nested
  class Ejecucion {

    private Donacion donacion;
    private DonacionSegmentada arroz;
    private EntidadBeneficiaria comedor;

    @BeforeEach
    void cargarDatos() {
      comedor = new EntidadBeneficiaria("Av. Medrano 951", new ArrayList<>(), "Comedor de prueba", null, "COMEDOR");
      EntidadRepository.Instance.registrar(comedor);

      Subcategoria subcatArroz = new Subcategoria(Categoria.ALIMENTOS, "ARROZ");
      Bien paquete = new BienPerecedero(subcatArroz, "foto.jpg", "Arroz 1kg", new Date());
      arroz = new DonacionSegmentada(10, subcatArroz, paquete);
      donacion = new Donacion("Donacion de prueba", new ArrayList<>(List.of(paquete)), null);
      donacion.setId("TEST-" + UUID.randomUUID());
      donacion.setDonacionesSegmentadas(List.of(arroz));
      DonacionesRepository.Instance.guardar(donacion);
    }

    @AfterEach
    void borrarDatos() {
      // Los repositorios son compartidos entre tests: dejamos todo como estaba
      DonacionesRepository.Instance.eliminar(donacion.getId());
      EntidadRepository.Instance.eliminarPorId(comedor.getId());
    }

    @Test
    void alProgramarloQuedaRegistradoElJobDeAsignacion() throws Exception {
      assertTrue(scheduler.checkExists(AsignacionCronScheduler.JOB_KEY));
      assertEquals(1, scheduler.getTriggersOfJob(AsignacionCronScheduler.JOB_KEY).size());
    }

    @Test
    void cuandoQuartzDisparaElJobLasDonacionesEnDepositoQuedanConPropuestas() throws Exception {
      CountDownLatch termino = avisarCuandoTermine();
      scheduler.start();

      scheduler.triggerJob(AsignacionCronScheduler.JOB_KEY);   // "hacer como que son las 3"

      assertTrue(termino.await(5, TimeUnit.SECONDS), "El job no corrio");
      assertNotNull(arroz.getResultadosPropuestos(), "El matchmaking no dejo efecto");
      assertTrue(arroz.getResultadosPropuestos().entidadesPropuestas().contains(comedor));
    }

    private CountDownLatch avisarCuandoTermine() throws Exception {
      CountDownLatch termino = new CountDownLatch(1);
      scheduler.getListenerManager().addJobListener(new JobListenerSupport() {
        @Override
        public String getName() {
          return "esperar-matchmaking";
        }

        @Override
        public void jobWasExecuted(JobExecutionContext context, JobExecutionException error) {
          termino.countDown();
        }
      }, KeyMatcher.keyEquals(AsignacionCronScheduler.JOB_KEY));
      return termino;
    }
  }
}
