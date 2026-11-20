package ar.edu.utn.frba.dds.planificacion.utils;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class PlanificacionCronScheduler {
  private static final String HORARIO_BAJA_CARGA = "0 0 3 * * ?"; //"0 0 3 * * ?";

  public static final JobKey JOB_KEY = JobKey.jobKey("planificacionDonacionesJob", "grupoLogistica");

  public static void iniciar(){
    try{
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      programarEn(scheduler);
      scheduler.start();

    }catch(Exception e){
      throw new RuntimeException("No se pudo programar la planificación.", e);
    }
  }

  public static void ejecutarAhora(){
    try {
      StdSchedulerFactory.getDefaultScheduler().triggerJob(JOB_KEY);
    } catch (SchedulerException e) {
      throw new RuntimeException("No se pudo programar la planificación.", e);
    }
  }

  public static void programarEn(Scheduler scheduler) throws SchedulerException{
    scheduler.scheduleJob(crearJob(),crearTigger());
  }

  static JobDetail crearJob() {
    return JobBuilder.newJob(PlanificacionDonacionesJob.class)
        .withIdentity(JOB_KEY)
        .build();
  }

  static Trigger crearTigger() {
    return TriggerBuilder.newTrigger()
        .withIdentity("AsignaciónDonacionTrigger", "grupoDonaciones")
        .withSchedule(CronScheduleBuilder.cronSchedule(HORARIO_BAJA_CARGA))
        .build();
  }
}
