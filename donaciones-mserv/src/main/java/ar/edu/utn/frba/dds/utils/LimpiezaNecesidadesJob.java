package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.controllers.NecesidadController;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LimpiezaNecesidadesJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Ejecutando limpieza nocturna de necesidades vencidas");
        
        try {
            NecesidadController controller = new NecesidadController();
            controller.actualizarVencidas();
        } catch (Exception e) {
            System.err.println("Error al ejecutar el Job de necesidades: " + e.getMessage());
            e.printStackTrace();
        }
    }
}