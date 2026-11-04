package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.main.templates.JavalinHandlebars;
import ar.edu.utn.frba.dds.main.templates.JavalinRenderer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinJackson;

// IMPORT CRÍTICO NUESTRO PARA LA BASE DE DATOS
import ar.edu.utn.frba.dds.repositories.EntityManagerHelper;

import java.io.IOException;

public class Server {
  public void start() throws IOException, InterruptedException {
    // Tus compañeros lo comentaron porque seguramente ya se está llamando desde App.main()
    // Bootstrap.init();
    java.nio.file.Files.createDirectories(java.nio.file.Path.of("public/uploads"));
    var app = Javalin.create(config -> {
      config.staticFiles.add("/public");
      config.jsonMapper(new JavalinJackson().updateMapper(mapper -> {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new Jdk8Module());
      }));

      initializeTemplating(config);
      initializeStaticFiles(config);
    });

    new Router().configure(app);

    app.start(9001);
  }

  private void initializeTemplating(JavalinConfig config) {
    config.fileRenderer(
        new JavalinRenderer().register("hbs", new JavalinHandlebars())
    );
  }

  private static void initializeStaticFiles(JavalinConfig config) {
    config.staticFiles.add(staticFileConfig -> {
      // Usamos la carpeta public que creamos recién para que no crashee
      staticFileConfig.hostedPath = "/public";
      staticFileConfig.directory = "/public";
    });

    config.staticFiles.add(staticFileConfig -> {
      staticFileConfig.hostedPath = "/uploads";
      staticFileConfig.directory = "public/uploads";
      staticFileConfig.location = Location.EXTERNAL;
    });
  }
}