package ar.edu.utn.frba.dds.utils;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class AsignacionCronScheduler {

  private static final String HORARIO_BAJA_CARGA = "0 */2 * * * ?"; //"0 0 3 * * ?";

  public static final JobKey JOB_KEY = JobKey.jobKey("asignaciónDonacionesJob", "grupoDonaciones");

  public static void iniciar(){
    try{
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      programarEn(scheduler);
      scheduler.start();

    }catch(Exception e){
      throw new RuntimeException("No se pudo programar el matmacking", e);
    }
  }

  /*
    public static void iniciar() {

    JobDetail job = JobBuilder.newJob(AsignacionDonacionesJob.class)
        .withIdentity("asignacionDonacionJob", "grupoDonaciones")
        .build();

    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity("asignacionDonacionTrigger","grupoDonaciones")
        .withSchedule(CronScheduleBuilder.cronSchedule(TODOS_LOS_DIAS_3_AM))
        .build();

    try {
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      scheduler.start();
      scheduler.scheduleJob(job, trigger);
    } catch (SchedulerException e ){
      throw new RuntimeException("No se pudo programar el matmcaking,", e);
    }

  }
   */

  public static void programarEn(Scheduler scheduler) throws SchedulerException{
    scheduler.scheduleJob(crearJob(),crearTigger());
  }

  static JobDetail crearJob() {
    return JobBuilder.newJob(AsignacionDonacionesJob.class)
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
