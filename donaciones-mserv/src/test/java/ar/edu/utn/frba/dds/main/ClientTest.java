package ar.edu.utn.frba.dds.main;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import ar.edu.utn.frba.dds.dto.DonacionSegmentadaDTO;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

class ClientTest {

  @Mock
  private HttpClient httpClientMock;
  @Mock
  private HttpResponse<String> httpResponseMock;

  // Cambiamos el tipo a GrpcClientLogisticaImplementation
  ClientLogistica client;

  @BeforeEach
  void setUp() {
    httpClientMock = mock(HttpClient.class);
    httpResponseMock = mock(HttpResponse.class);

    // Instanciamos la clase hija
    client = new ClientLogistica(httpClientMock);
  }

  @Test
  void enviaCorrectamenteDonacionALogistica() throws IOException, InterruptedException {
    when(httpResponseMock.statusCode()).thenReturn(200);
    when(httpResponseMock.body()).thenReturn("{}");
    when(httpClientMock.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
        .thenReturn(httpResponseMock);

    // enviarDonacionALogistica(id, entidad, direccion) ya no existe: en refactor1 se reemplazó
    // por enviarDonacionesEnLotes, que manda de a 100. Un lote de una donación equivale al envío de antes.
    client.enviarDonacionesEnLotes(unLoteDe(12, 33, "Calle Hola 1243"));

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

    assertDoesNotThrow(() -> client.enviarDonacionesEnLotes(unLoteDe(12, 45, "Av Libertador 3470")));
  }

  private static List<DonacionSegmentadaDTO> unLoteDe(int idDonacion, int idEntidad, String direccion) {
    DonacionSegmentadaDTO dto = new DonacionSegmentadaDTO();
    dto.setIdDonacion(idDonacion);
    dto.setIdEntidadAsignada(idEntidad);
    dto.setDireccionEntidad(direccion);
    return List.of(dto);
  }
}