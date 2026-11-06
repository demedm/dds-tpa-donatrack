package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.GrpcServiceImplementation.GrpcServiceDonacionImplementation;
import ar.edu.utn.frba.dds.GrpcServiceImplementation.GrpcServiceUsuarioImplementation;
import ar.edu.utn.frba.dds.main.templates.JavalinHandlebars;
import ar.edu.utn.frba.dds.main.templates.JavalinRenderer;
import ar.edu.utn.frba.dds.rutas.Routes;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.staticfiles.Location;
import io.javalin.json.JavalinJackson;


import java.io.IOException;

public class Server {
  public void start() throws IOException, InterruptedException {
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

      config.router.apiBuilder(() -> new Routes().configure());
      
    });

    int grpcPort = 9090;
    io.grpc.Server grpcServer = io.grpc.ServerBuilder
            .forPort(grpcPort)
            .addService(new GrpcServiceDonacionImplementation())
            .addService(new GrpcServiceUsuarioImplementation())
            .build()
            .start();

    System.out.println("🚀 Servidor gRPC de Donaciones escuchando en el puerto " + grpcPort);
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