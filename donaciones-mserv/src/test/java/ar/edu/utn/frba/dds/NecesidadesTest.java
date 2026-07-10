package ar.edu.utn.frba.dds;

import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import ar.edu.utn.frba.dds.server.Router;

import static org.assertj.core.api.Assertions.assertThat;


public class NecesidadesTest {
  @Test
    public void postCrearNecesidadRetorna201() throws IOException, InterruptedException {
        Javalin app = Javalin.create();
        new Router().configure(app);

        JavalinTest.test(app, (server, client) -> {
            String body = "{\"entidadId\":\"ent-1\",\"descripcion\":\"Necesito arroz\",\"tipo\":\"NORMAL\"}";
            assertThat(client.post("/necesidades", body).code()).isEqualTo(201);
        });
    }

    public void postPeticionANecesidadExistenteRetorna201() throws IOException, InterruptedException {
        Javalin app = Javalin.create();
        new Router().configure(app);

        JavalinTest.test(app, (server, client) -> {
            String body = "{\"entidadId\":\"ent-1\",\"descripcion\":\"Necesito arroz\",\"tipo\":\"NORMAL\"}";
            client.post("/necesidades", body).code();

            String bodyPeticion = "{\"subclase\":\"ropa\",\"cantidadRequerida\":5}";
            assertThat(client.post("/necesidades/ent-1/peticiones", bodyPeticion).code()).isEqualTo(201);

        });
    }


  @Test
    public void postCrearNecesidadRetornaLaNecesidadCreada() throws IOException, InterruptedException {
      Javalin app = Javalin.create();
      new Router().configure(app);

    JavalinTest.test(app, (server, client) -> {
        String body = "{\"entidadId\":\"ent-1\",\"descripcion\":\"Necesito arroz\",\"tipo\":\"NORMAL\"}";
        String responseBody = client.post("/necesidades", body).body().string();
        
        assertThat(responseBody).contains("Necesito arroz");
        assertThat(responseBody).contains("NORMAL");
        assertThat(responseBody).contains("ent-1");
    });}

  @Test
    public void postCrearNecesidadRecurrenteConDiasInvalidosRetorna400() throws IOException, InterruptedException {
      Javalin app = Javalin.create();
      new Router().configure(app);

    JavalinTest.test(app, (server, client) -> {
        String body = "{\"entidadId\":\"ent-1\",\"descripcion\":\"Necesito arroz\",\"tipo\":\"RECURRENTE\",\"diasRecurrencia\":-1}";
        var response = client.post("/necesidades", body);
        
        assertThat(response.code()).isEqualTo(400);
        assertThat(response.body().string()).contains("las necesidades recurrentes requieren los dias de recurrencia y mayores a 0");
    });
}

@Test
    public void postCrearNecesidadSinEntidadReferenciadaRetorna400() throws IOException, InterruptedException {
      Javalin app = Javalin.create();
      new Router().configure(app);

    JavalinTest.test(app, (server, client) -> {
        String body = "{\"descripcion\":\"Necesito arroz\",\"tipo\":\"RECURRENTE\",\"diasRecurrencia\":-1}";
        var response = client.post("/necesidades", body);
        
        assertThat(response.code()).isEqualTo(400);
        assertThat(response.body().string()).contains("el id de la entidad es obligatorio");
    });
}

@Test
    public void postCrearPeticionANecesidadInexistenteNotFound() throws IOException, InterruptedException {
      Javalin app = Javalin.create();
      new Router().configure(app);

    JavalinTest.test(app, (server, client) -> {
        String body = "{\"subclase\":\"ropa\",\"cantidadRequerida\":5}";
        var response = client.post("/necesidades/id-inexistente/peticiones", body);
        
        assertThat(response.code()).isEqualTo(404);
    });
}


}
