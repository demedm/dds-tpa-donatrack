package ar.edu.utn.frba.dds.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.utn.frba.dds.model.Entrega;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class ClientTest {
  @Mock
  private HttpClient httpClientMock;
  @Mock
  private HttpResponse<String> httpResponseMock;
  Client client;

  @BeforeEach
  void setUp() {
    httpClientMock = mock(HttpClient.class);
    httpResponseMock = mock(HttpResponse.class);
    client = new Client(httpClientMock, "http://localhost:9001/");
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
  void notificarFallaDeEntrega() {
  }

  @Test
  void solicitudPlanificacion() {
  }

}