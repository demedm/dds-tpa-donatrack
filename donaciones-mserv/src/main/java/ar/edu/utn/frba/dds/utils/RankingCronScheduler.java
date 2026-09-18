// package ar.edu.utn.frba.dds.utils;

// import org.quartz.CronScheduleBuilder;
// import org.quartz.JobBuilder;
// import org.quartz.JobDetail;
// import org.quartz.Scheduler;
// import org.quartz.Trigger;
// import org.quartz.TriggerBuilder;
// import org.quartz.impl.StdSchedulerFactory;

// public class RankingCronScheduler {

//     public static void iniciar() {
//         try {
//             JobDetail job = JobBuilder.newJob(RankingJob.class)//crea el job
//                     .withIdentity("rankingJob", "grupoDonaciones")
//                     .build();

//                 //Definir el trigger ** ** ** 
//             Trigger trigger = TriggerBuilder.newTrigger()
//                     .withIdentity("rankingTrigger", "grupoDonaciones")
//                     .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
//                     .build();

//             Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//             scheduler.start();
//             scheduler.scheduleJob(job, trigger);

//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }