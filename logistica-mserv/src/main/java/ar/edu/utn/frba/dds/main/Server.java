package ar.edu.utn.frba.dds.main;

import ar.edu.utn.frba.dds.grpcServiceImplementation.GrpcServiceLogisticaImplementation;
import ar.edu.utn.frba.dds.grpcServiceImplementation.GrpcServiceUsuarioImplementation;
import ar.edu.utn.frba.dds.main.templates.JavalinHandlebars;
import ar.edu.utn.frba.dds.main.templates.JavalinRenderer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.json.JavalinJackson;

import java.io.IOException;

public class




Server {
  public void start() throws IOException {
    Bootstrap.init();
    var app = Javalin.create(config -> {
      config.jsonMapper(new JavalinJackson().updateMapper(mapper -> {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new Jdk8Module());
      }));
      initializeTemplating(config);
      initializeStaticFiles(config);
    });

    int grpcPort = 9091;
    io.grpc.Server grpcServer = io.grpc.ServerBuilder
            .forPort(grpcPort)
            .addService(new GrpcServiceLogisticaImplementation())
            .addService(new GrpcServiceUsuarioImplementation())
            .build()
            .start();

    System.out.println("🚀 Servidor gRPC de Logística escuchando en el puerto " + grpcPort);
    new Router().configure(app);

    app.start(9002);
  }

  private void initializeTemplating(JavalinConfig config) {
    config.fileRenderer(
        new JavalinRenderer().register("hbs", new JavalinHandlebars())
    );
  }

  private static void initializeStaticFiles(JavalinConfig config) {
    config.staticFiles.add(staticFileConfig -> {
      staticFileConfig.hostedPath = "/assets";
      staticFileConfig.directory = "/assets";
    });
  }

}