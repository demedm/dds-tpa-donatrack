package ar.edu.utn.frba.dds.server;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

class RouterTest {

  private Javalin app;
  private HttpClient httpClient;
  private String baseUrl;

  @BeforeEach
  void setUp() {
    // Limpiamos el estado de los repos (son singletons compartidos entre tests)
    CamionRepositorio.Instance.getFlota().clear();
    RutaRepositorio.Instance.getAllRutas().clear();

    app = Javalin.create();
    new Router().configure(app);
    app.start(0); // puerto efímero, evita colisiones entre corridas
    baseUrl = "http://localhost:" + app.port();

    httpClient = HttpClient.newHttpClient();
  }

  @AfterEach
  void tearDown() {
    app.stop();
  }

  // OPERACIONES CRUD PARA CORRER TESTS (no son las misma que en client)

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

  // CAMIONES

  @Test
  void getCamionesDevuelveListaVaciaSiNoHayCamionesRegistrados() throws Exception {
    HttpResponse<String> response = get("/camiones/");

    assertEquals(200, response.statusCode());
    assertEquals("[]", response.body());
  }

  @Test
  void getCamionesDevuelveLaFlotaRegistrada() throws Exception {
    CamionRepositorio.Instance.registrarCamion(
        new Camion("AB123CD", 1000, 500, 220));

    HttpResponse<String> response = get("/camiones/");

    assertEquals(200, response.statusCode());
    assertTrue(response.body().contains("AB123CD"));
  }

  @Test
  void getCamionPorPatenteDevuelveElCamionCorrecto() throws Exception {
    CamionRepositorio.Instance.registrarCamion(
        new Camion("AB123CD", 1000, 500, 220));

    HttpResponse<String> response = get("/camiones/AB123CD");

    assertEquals(200, response.statusCode());
    assertTrue(response.body().contains("AB123CD"));
  }

  @Test
  void getCamionInexistenteDevuelve404() throws Exception {
    HttpResponse<String> response = get("/camiones/NOEXISTE");

    assertEquals(404, response.statusCode());
  }

  @Test
  void postCamionValidoLoRegistraYDevuelve201() throws Exception {
    String body = """
        { "patente": "XY999ZZ", "capacidadCarga": 800,
          "capacidadVolumen": 400, "altura": 200 }
        """;

    HttpResponse<String> response = post("/camiones/XY999ZZ", body);

    assertEquals(201, response.statusCode());
    assertTrue(response.body().contains("XY999ZZ"));
    assertEquals(1, CamionRepositorio.Instance.getFlota().size());
  }

  @Test
  void postCamionSinPatenteDevuelve400() throws Exception {
    String body = """
        { "patente": "", "capacidadCarga": 800,
          "capacidadVolumen": 400, "altura": 200 }
        """;

    HttpResponse<String> response = post("/camiones/XY999ZZ", body);

    assertEquals(400, response.statusCode());
    assertEquals(0, CamionRepositorio.Instance.getFlota().size());
  }

  // RUTAS

  @Test
  void getRutasDevuelveListaVaciaSiNoHayRutas() throws Exception {
    HttpResponse<String> response = get("/rutas/");

    assertEquals(200, response.statusCode());
    assertEquals("[]", response.body());
  }

  @Test
  void getEntregasDeUnaRutaDevuelveLasEntregasCorrectas() throws Exception {
    Entrega entrega = new Entrega("Calle Falsa 123", 1);
    Ruta ruta = new Ruta("AB123CD", List.of(entrega));
    RutaRepositorio.Instance.getAllRutas().add(ruta); // seed directo, sin pasar por addRuta

    HttpResponse<String> response = get("/rutas/" + ruta.getId() + "/entregas/");

    assertEquals(200, response.statusCode());
    assertTrue(response.body().contains("Calle Falsa 123"));
  }

  // ⚠️ Documenta el bug ACTUAL de findByid (get(0) sobre lista vacía).
  // Cuando se corrija a findFirst().orElse(null) + manejo de 404 en el controller,
  // este test debería esperar 404 en vez de 500.
  @Test
  void getRutaInexistenteActualmenteRompeConError500PorBugEnFindByid() throws Exception {
    HttpResponse<String> response = get("/rutas/no-existe");

    assertEquals(500, response.statusCode());
  }

  // CALLBACK: /callback/planificaciones

  @Test
  void recibirPlanificacionCreaNuevasRutasYResponde200() throws Exception {
    String body = """
        {
          "rutasPlanificadas": [
            {
              "patenteCamion": "AB123CD",
              "destinos": [
                { "direccion": "Calle Falsa 123", "donacionId": 1 }
              ]
            }
          ],
          "donacionesNoPlanificadas": []
        }
        """;

    HttpResponse<String> response = post("/callback/planificaciones/", body);

    assertEquals(200, response.statusCode());
    assertEquals(1, RutaRepositorio.Instance.getAllRutas().size());
    assertEquals("AB123CD",
        RutaRepositorio.Instance.getAllRutas().get(0).getPatenteAsignada());
  }

  @Test
  void recibirPlanificacionAsignaElCamionSiExisteEnLaFlota() throws Exception {
    CamionRepositorio.Instance.registrarCamion(
        new Camion("AB123CD", 1000, 500, 220));

    String body = """
        {
          "rutasPlanificadas": [
            {
              "patenteCamion": "AB123CD",
              "destinos": [
                { "direccion": "Calle Falsa 123", "donacionId": 1 }
              ]
            }
          ],
          "donacionesNoPlanificadas": []
        }
        """;

    post("/callback/planificaciones/", body);

    Camion camion = CamionRepositorio.Instance.findByPatente("AB123CD");
    assertNotNull(camion.getRutaActual());
    assertEquals("AB123CD", camion.getRutaActual().getPatenteAsignada());
  }

  // RECEPCION DE DONACIONES

  @Test
  void recibirDonacionDevuelve201YElDtoCorrespondiente() throws Exception {
    String body = """
        { "direccionEntidad": "Av. Siempreviva 742",
          "idEntidadAsignada": 7,
          "fechaVencimiento": null }
        """;

    HttpResponse<String> response = post("/donaciones/55", body);

    assertEquals(201, response.statusCode());
    assertTrue(response.body().contains("Av. Siempreviva 742"));
  }
}