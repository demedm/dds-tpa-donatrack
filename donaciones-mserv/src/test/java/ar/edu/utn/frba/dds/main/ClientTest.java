package ar.edu.utn.frba.dds.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    client = new Client(httpClientMock);
  }

  @Test
  void enviaCorrectamenteDonacionALogistica() throws IOException, InterruptedException {
    when(httpResponseMock.statusCode()).thenReturn(200);
    when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponseMock);

    client.enviarDonacionALogistica(12, 33, "Calle Hola 1243");

    // Verificamos que efectivamente se haya intentado mandar el request
    verify(httpClientMock, times(1))
        .send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
  }

  @Test
  void manejaCorrectamenteExcepciones() throws IOException, InterruptedException {
      when(httpResponseMock.statusCode()).thenReturn(404);
      when(httpResponseMock.body()).thenReturn("Donación no encontrada");
      when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
          .thenReturn(httpResponseMock);

      assertDoesNotThrow(() -> client.enviarDonacionALogistica(
          12, 45, "Av Libertador 3470"));
  }

}