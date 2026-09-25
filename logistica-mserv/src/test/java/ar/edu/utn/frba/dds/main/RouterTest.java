package ar.edu.utn.frba.dds.main;

import static org.junit.jupiter.api.Assertions.*;


import ar.edu.utn.frba.dds.exceptions.CamionNotFoundException;
import ar.edu.utn.frba.dds.exceptions.EntregaNotFoundException;
import ar.edu.utn.frba.dds.exceptions.RutaNotFoundException;
import ar.edu.utn.frba.dds.exceptions.UsuarioNotFoundException;
import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.model.EstadoCamion;
import ar.edu.utn.frba.dds.model.EstadoRuta;
import ar.edu.utn.frba.dds.model.Ruta;
import ar.edu.utn.frba.dds.model.usuarios.Chofer;
import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.repositories.CamionRepositorio;
import ar.edu.utn.frba.dds.repositories.EntregaRepositorio;
import ar.edu.utn.frba.dds.repositories.RutaRepositorio;
import ar.edu.utn.frba.dds.repositories.UsuarioRepositorio;
import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
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
/*
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

  // CAMIONES

  @Test
  void getCamionesDevuelveListaVaciaSiNoHayCamionesRegistrados() throws Exception {
    HttpResponse<String> response = get("/camiones/");

    assertEquals(200, response.statusCode());
    // assertEquals("[]", response.body());
  }

  @Test
  void getCamionesDevuelveLaFlotaRegistrada() throws Exception {
    camionRepositorio.registrar(camion);

    HttpResponse<String> response = get("/camiones/");

    assertEquals(200, response.statusCode());
    assertTrue(response.body().contains("AB123CD"));
  }

  @Test
  void getCamionPorPatenteDevuelveElCamionCorrecto() throws Exception {
    camionRepositorio.registrar(camion);

    HttpResponse<String> response = get("/camiones/" + camion.getId().toString());

    assertEquals(200, response.statusCode());
    assertTrue(response.body().contains("AB123CD"));
  }

  @Test
  void postCamionLoRegistraYDevuelve201() throws Exception {
    String body = """
        { 
          "patente": "XY999ZZ", 
          "capacidadCarga": 800,
          "capacidadVolumen": 400, 
          "altura": 200 
        }
        """;

    HttpResponse<String> response = post("/camiones/XY999ZZ", body);

    assertEquals(201, response.statusCode());
    assertTrue(response.body().contains("XY999ZZ"));
    assertEquals(1, CamionRepositorio.Instance.mostrarTodos().size());
  }

  @Test
  void postCamionSinPatenteDevuelve400() throws Exception {
    String body = """
        { 
          "patente": "", 
          "capacidadCarga": 800,
          "capacidadVolumen": 400, 
          "altura": 200 
        }
        """;

    HttpResponse<String> response = post("/camiones/XY999ZZ", body);

    assertEquals(400, response.statusCode());
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
    Ruta rutaPrueba = new Ruta(List.of(entrega));
    rutaPrueba.asignarCamion(camion);
    camionRepositorio.registrar(camion);
    entregaRepositorio.registrar(entrega);
    rutaRepositorio.registrar(rutaPrueba);

    HttpResponse<String> response = get("/rutas/" + rutaPrueba.getId() + "/entregas/");

    assertEquals(200, response.statusCode());
    assertTrue(response.body().contains("Calle Falsa 123"));
  }

  @Test
  public void postRutaLaRegistraYDevuelve201() throws Exception {
    usuarioRepositorio.registrar(entidadBeneficiaria1);
    usuarioRepositorio.registrar(entidadBeneficiaria2);
    usuarioRepositorio.registrar(chofer);
    camionRepositorio.registrar(camion2);
    entrega1.setEntidadBeneficiaria(entidadBeneficiaria1);
    entrega2.setEntidadBeneficiaria(entidadBeneficiaria2);
    entregaRepositorio.registrar(entrega1);
    entregaRepositorio.registrar(entrega2);

    String body = """
        { 
          "entregas": 
          [
            {
              "id": """ + entrega1.getId() + """
              ,
              "direccion": "Av Siempreviva 123",
              "donacionId": 13
            },
            {
              "id": """ + entrega2.getId() + """
              ,
              "direccion": "Calle Armando 748",
              "donacionId": 7
            }
          ],
          "camion": {
            "id": """ + camion2.getId() + """
            ,
            "patente": "XY999ZZ", 
            "capacidadCarga": 800,
            "capacidadVolumen": 400, 
            "altura": 200 
          },
          "chofer": {
            "id": """ + chofer.getId() + """
            ,
            "email": "carlosmendez@gmail.com",
            "nombre": "Carlos",
            "apellido": "Mendez"
          }
        }
        """;

    HttpResponse<String> response = post("/rutas/", body);

    assertEquals(201, response.statusCode());
    assertEquals(1, RutaRepositorio.Instance.mostrarTodos().size());
    Ruta ruta = RutaRepositorio.Instance.mostrarTodos().get(0);
    assertEquals(chofer, ruta.getChofer());
    assertEquals(EstadoRuta.NO_INICIADA, ruta.getEstado());
  }

  @Test
  public void patchDeRutaActualizaLaRuta() throws Exception {
    entregaRepositorio.registrar(entrega1);
    entregaRepositorio.registrar(entrega2);
    camionRepositorio.registrar(camion2);
    usuarioRepositorio.registrar(chofer);
    Ruta rutaPrueba = new Ruta(List.of(entrega1, entrega2));
    rutaPrueba.asignarCamion(camion2);
    rutaPrueba.asignarChofer(chofer);
    rutaRepositorio.registrar(rutaPrueba);

    String body = """
        { 
          "estado": "CANCELADA"
        }
        """;

    HttpResponse<String> response = patch("/rutas/" + rutaPrueba.getId().toString(), body);

    assertEquals(200, response.statusCode());
    System.out.println(response.body());
    assertEquals(EstadoRuta.CANCELADA, rutaRepositorio.buscarEstadoPorId(rutaPrueba.getId()));
  }

  @Test
  public void deleteDeRutaEliminaLaRuta() throws Exception {
    entregaRepositorio.registrar(entrega1);
    entregaRepositorio.registrar(entrega2);
    camionRepositorio.registrar(camion2);
    usuarioRepositorio.registrar(chofer);
    Ruta rutaPrueba = new Ruta(List.of(entrega1, entrega2));
    rutaPrueba.asignarCamion(camion2);
    rutaPrueba.asignarChofer(chofer);
    rutaRepositorio.registrar(rutaPrueba);

    HttpResponse<String> response = delete("/rutas/" + rutaPrueba.getId().toString());

    assertEquals(200, response.statusCode());
    assertThrows(RutaNotFoundException.class, () ->
        rutaRepositorio.buscarPorId(rutaPrueba.getId()));
    assertThrows(EntregaNotFoundException.class, () ->
        entregaRepositorio.buscarPorId(entrega1.getId()));
    assertThrows(EntregaNotFoundException.class, () ->
        entregaRepositorio.buscarPorId(entrega2.getId()));
    assertEquals(EstadoCamion.DISPONIBLE, CamionRepositorio.Instance
        .buscarEstadoPorId(camion2.getId()));
  }

  // CALLBACK: /callback/planificaciones
  /*
  @Test
  void recibirPlanificacionCreaNuevasRutasYResponde200() throws Exception {
    camionRepositorio.registrar(camion);
    usuarioRepositorio.registrar(chofer);

    String body = """
        {
          "rutasPlanificadas": [
            {
              "camion": {
                  "id": """ + camion.getId() + """
                  , 
                  "patente": "AB123CD", 
                  "capacidadCarga": 1000.0,
                  "capacidadVolumen": 500.0, 
                  "altura": 220.0
              },
              "destinos": [
                { 
                  "direccion": "Calle Falsa 123", 
                  "donacionId": 1 
                }
              ]
            }
          ],
          "donacionesNoPlanificadas": []
        }
        """;

    HttpResponse<String> response = post("/callback/planificaciones/", body);
    // assertEquals(1, entregaRepositorio.mostrarTodos().size());
    assertEquals(200, response.statusCode());
    var rutas = rutaRepositorio.mostrarTodos();
    assertEquals("AB123CD", rutas.get(0).getCamion().getPatente());
  }

  @Test
  void recibirPlanificacionAsignaElCamion() throws Exception {
    // tiene que haber camiones y choferes disponibles para crear una ruta
    camionRepositorio.registrar(camion);
    usuarioRepositorio.registrar(chofer);

    String body = """
        {
          "rutasPlanificadas": [
            {
              "camion": {
                  "id": """ + camion.getId() + """
                  ,
                  "patente": "AB123CD", 
                  "capacidadCarga": 1000,
                  "capacidadVolumen": 500, 
                  "altura": 220
              },
              "destinos": [
                { 
                  "direccion": "Calle Falsa 123", 
                  "donacionId": 1 
                }
              ]
            }
          ],
          "donacionesNoPlanificadas": []
        }
        """;

    HttpResponse<String> response = post("/callback/planificaciones/", body);
    assertEquals(200, response.statusCode());

    assertEquals(EstadoCamion.RUTA_ASIGNADA, camionRepositorio.buscarEstadoPorId(camion.getId()));
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
*/
}