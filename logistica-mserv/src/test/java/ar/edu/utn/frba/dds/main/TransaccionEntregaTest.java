package ar.edu.utn.frba.dds.main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoEntrega;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class TransaccionEntregaTest {
  private Javalin app;
  private HttpClient httpClient;
  private String baseUrl;
  private CamionRepositorio camionRepositorio;
  private RutaRepositorio rutaRepositorio;
  private EntregaRepositorio entregaRepositorio;
  private UsuarioRepositorio usuarioRepositorio;

  private Entrega entrega;
  private Entrega entrega1;
  private Entrega entrega2;
  private Camion camion;
  private Camion camion2;
  private EntidadBeneficiaria entidadBeneficiaria1;
  private EntidadBeneficiaria entidadBeneficiaria2;
  private Chofer chofer;

  @BeforeEach
  void setUp() {
    camionRepositorio = new CamionRepositorio();
    rutaRepositorio = new RutaRepositorio();
    entregaRepositorio = new EntregaRepositorio();
    usuarioRepositorio = new UsuarioRepositorio();

    app = Javalin.create();
    new Router().configure(app);
    app.start(0); // puerto efímero, evita colisiones
    baseUrl = "http://localhost:" + app.port();

    httpClient = HttpClient.newHttpClient();

    entrega = new Entrega("Calle Falsa 123", (long)1);
    entrega1 = new Entrega("Av Siempreviva 123", 13L);
    entrega2 = new Entrega("Calle Armando 748", 7L);
    camion = new Camion("AB123CD", 1000.0, 500.0, 220.0);
    camion2 = new Camion("XY999ZZ", 800.0, 400.0, 200.0);
    entidadBeneficiaria1 = new EntidadBeneficiaria("patitasfelices@outlook.com",
        "Av Siempreviva 123", "Patitas Felices ORG");
    entidadBeneficiaria2 = new EntidadBeneficiaria("extiendomismanos@gmail.com",
        "Calle Armando 748", "Extiendo Mis Manos Coop.");
    chofer = new Chofer("carlosmendez@gmail.com", "Carlos", "Mendez");
  }

  @AfterEach
  void tearDown() {
    app.stop();
  }

  /* -------------------------------------------------------------------- */

  // OPERACIONES CRUD PARA CORRER TESTS (no son las misma que en Client)

  private HttpResponse<String> get(String path) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + path))
        .GET().build();
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }

  private HttpResponse<String> post(String path, String jsonBody) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + path))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }

  private HttpResponse<String> put(String path, String jsonBody) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + path))
        .header("Content-Type", "application/json")
        .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }

  private HttpResponse<String> patch(String path, String jsonBody) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + path))
        .header("Content-Type", "application/json")
        .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
        .build();
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }

  private HttpResponse<String> delete(String path) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(baseUrl + path))
        .DELETE()
        .build();
    return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
  }

  /* -------------------------------------------------------------------- */

  @Test
  void confirmarRecepcionEntregaCambiaElEstadoDeEntrega() throws Exception {
    camionRepositorio.registrar(camion2);
    usuarioRepositorio.registrar(entidadBeneficiaria1);
    entrega1.setEntidadBeneficiaria(entidadBeneficiaria1);
    entregaRepositorio.registrar(entrega1);

    String body = """
        {
          "camionId": """ + camion2.getId() + """
          ,
          "anio": 2026, 
          "mes": 8,
          "dia": 26,
          "hora": 15,
          "minutos": 37
        }
        """;

    HttpResponse<String> response = post("/usuarios/" + entidadBeneficiaria1.getId() +
        "/entregas/" + entrega1.getId() + "/confirmar", body);
    assertEquals(200, response.statusCode());
    assertEquals(EstadoEntrega.ENTREGADA, entregaRepositorio.buscarEstadoPorId(entrega1.getId()));
  }

  @Test
  void marcarComoNoRecepcionadaCambiaEstadoDeEntrega() throws Exception {
    camionRepositorio.registrar(camion2);
    usuarioRepositorio.registrar(entidadBeneficiaria1);
    entrega1.setEntidadBeneficiaria(entidadBeneficiaria1);
    entregaRepositorio.registrar(entrega1);
    HttpResponse<String> response = post("/usuarios/" + entidadBeneficiaria1.getId() +
        "/entregas/" + entrega1.getId() + "/no-entregado", " {} ");
    assertEquals(200, response.statusCode());
    assertEquals(EstadoEntrega.NO_RECIBIDA, entregaRepositorio.buscarEstadoPorId(entrega1.getId()));
  }
}
