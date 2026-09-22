package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.utils.LimpiezaNecesidadesJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class LimpiezaCronScheduler {

    public static void iniciar() {
        try {
            JobDetail job = JobBuilder.newJob(LimpiezaNecesidadesJob.class)
                    .withIdentity("limpiezaNecesidadesJob", "grupoCron")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("limpiezaNecesidadesTrigger", "grupoCron")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))//*/10 * * * * ? Si quier probar, para que se haga cada 10 segundos 
                    .build();

            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            scheduler.scheduleJob(job, trigger);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}