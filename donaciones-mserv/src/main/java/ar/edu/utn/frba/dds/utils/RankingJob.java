// package ar.edu.utn.frba.dds.utils;

// import ar.edu.utn.frba.dds.controllers.MatchmakingController;
// import org.quartz.Job;
// import org.quartz.JobExecutionContext;
// import org.quartz.JobExecutionException;

// public class RankingJob implements Job {

//     @Override
//     public void execute(JobExecutionContext context) throws JobExecutionException {
//         System.out.println("Ejecutando Job nocturno de ranking...");
        
//         try {
//             MatchmakingController matchmakingController = new MatchmakingController();
//             matchmakingController.obtenerRanking();
//         } catch (Exception e) {
//             System.err.println("Error al ejecutar el ranking por Cron: " + e.getMessage());
//             e.printStackTrace();
//         }
//     }
// }