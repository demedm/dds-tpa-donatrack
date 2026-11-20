package ar.edu.utn.frba.dds.planificacion.utils;

import ar.edu.utn.frba.dds.planificacion.tareas.PlanificarDonaciones;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class PlanificacionDonacionesJob implements Job {

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    try{
      PlanificarDonaciones.ejecutar();
    }catch (Exception e){
      throw new JobExecutionException(e);
    } finally {
      WithSimplePersistenceUnit.dispose();
    }
  }
}
