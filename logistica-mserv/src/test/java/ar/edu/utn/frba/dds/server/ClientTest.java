package ar.edu.utn.frba.dds.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.model.Camion;
import ar.edu.utn.frba.dds.model.Entrega;
import ar.edu.utn.frba.dds.scripts.dto.RequestPlanificacionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ClientTest {
  private HttpClient httpClientMock;
  Client client;
  private HttpResponse<String> httpResponseMock;
  private HttpResponse<String> responseOk;
  private HttpResponse<String> responseError;

  private List<RequestPlanificacionDTO> donaciones50;
  private List<RequestPlanificacionDTO> donaciones250;
  private List<Camion> camiones;

  @BeforeEach
  void setUp() {
    httpClientMock = mock(HttpClient.class);
    httpResponseMock = mock(HttpResponse.class);
    client = new Client(httpClientMock, "http://localhost:9001/");

    responseOk = mock(HttpResponse.class);
    when(responseOk.statusCode()).thenReturn(200);
    when(responseOk.body()).thenReturn("{\"responseId\":\"abc-123\"}");

    responseError = mock(HttpResponse.class);
    when(responseError.statusCode()).thenReturn(500);
    when(responseError.body()).thenReturn("Error interno del proveedor");

    donaciones50 = crearDonaciones(50);
    donaciones250 = crearDonaciones(250);
    camiones = crearCamiones(3);
  }

  private List<RequestPlanificacionDTO> crearDonaciones(int cantidad) {
    List<RequestPlanificacionDTO> donaciones = new ArrayList<>();
    for (int i = 0; i < cantidad; i++) {
      donaciones.add(mock(RequestPlanificacionDTO.class));
    }
    return donaciones;
  }

  private List<Camion> crearCamiones(int cantidad) {
    List<Camion> camiones = new ArrayList<>();
    for (int i = 0; i < cantidad; i++) {
      camiones.add(mock(Camion.class));
    }
    return camiones;
  }

  @Test
  void notificaCorrectamenteCambioEstado() throws IOException, InterruptedException {
    when(httpResponseMock.statusCode()).thenReturn(200);
    when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponseMock);

    client.notificarCambioEstado(123, "ENTREGADA");

    // Verificamos que efectivamente se haya intentado mandar el request
    verify(httpClientMock, times(1))
        .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
  }

  @Test
  void manejaCorrectamenteExcepciones() throws Exception {
    when(httpResponseMock.statusCode()).thenReturn(404);
    when(httpResponseMock.body()).thenReturn("Donación no encontrada");
    when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponseMock);

    assertDoesNotThrow(() -> client.notificarCambioEstado(123, "ENTREGADA"));
  }

  @Test
  void noRealizaLlamadasSiNoHayDonaciones() throws Exception {
    client.solicitudPlanificacion(Collections.emptyList(), camiones);

    verify(httpClientMock, times(0))
        .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
  }

  @Test
  void enviaUnSoloLoteCuandoHayMenosDe100Donaciones() throws Exception {
    when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(responseOk);

    client.solicitudPlanificacion(donaciones50, camiones);

    verify(httpClientMock, times(1))
        .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
  }

  @Test
  void divideEnTresLotesCuandoHay250Donaciones() throws Exception {
    when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(responseOk);

    client.solicitudPlanificacion(donaciones250, camiones);

    verify(httpClientMock, times(3))
        .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
  }

  @Test
  void noLanzaExcepcionSiElProveedorFallaEnTodosLosLotes() throws Exception {
    when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(responseError);

    assertDoesNotThrow(() ->
        client.solicitudPlanificacion(donaciones250, camiones)
    );

    verify(httpClientMock, times(3))
        .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
  }

}