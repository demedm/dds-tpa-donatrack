package ar.edu.utn.frba.dds.main;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.json.JavalinJackson;

import ar.edu.utn.frba.dds.repositories.EntityManagerHelper;

import java.io.IOException;

public class Server {
  public void start() throws IOException, InterruptedException {
    Bootstrap.init();

    var app = Javalin.create(config -> {
      config.jsonMapper(new JavalinJackson().updateMapper(mapper -> {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new Jdk8Module());
      }));

      // initializeTemplating(config); // <-- Comentado para evitar el error de Handlebars
      initializeStaticFiles(config);
    });

    new Router().configure(app);

    // CRÍTICO PARA JPA-EXTRAS: Libera la conexión a la base de datos
    app.after(ctx -> {
      EntityManagerHelper.closeEntityManager();
    });

    app.start(9001);
  }

  // Método comentado hasta que te traigas los archivos de tus compañeros
  /*
  private void initializeTemplating(JavalinConfig config) {
    config.fileRenderer(
        new JavalinRenderer().register("hbs", new JavalinHandlebars())
    );
  }
  */

  private static void initializeStaticFiles(JavalinConfig config) {
    config.staticFiles.add(staticFileConfig -> {
     // staticFileConfig.hostedPath = "/assets";
      //taticFileConfig.directory = "/assets";
    });
  }
}