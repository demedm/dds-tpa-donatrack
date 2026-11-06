package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.tareas.ProcesarDonaciones;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

// Quartz no corre dos ejecuciones de este job a la vez: si llega otra, espera su turno
@DisallowConcurrentExecution
public class AsignacionDonacionesJob implements Job {

  @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
      try{
        ProcesarDonaciones.ejecutar();
      }catch (Exception e){
        throw new JobExecutionException(e);
      } finally {
        WithSimplePersistenceUnit.dispose();
      }
  }
}
