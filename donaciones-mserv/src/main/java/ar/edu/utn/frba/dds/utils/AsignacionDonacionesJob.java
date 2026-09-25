package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.tareas.ProcesarDonaciones;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AsignacionDonacionesJob implements Job {

  @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      try{
        ProcesarDonaciones.ejecutar();

      }catch (Exception e){
        throw new JobExecutionException(e);
      }
  }
}
