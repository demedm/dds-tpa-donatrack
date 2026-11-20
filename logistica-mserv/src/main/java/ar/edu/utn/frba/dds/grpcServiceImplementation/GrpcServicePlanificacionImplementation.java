package ar.edu.utn.frba.dds.grpcServiceImplementation;

import ar.edu.utn.frba.dds.grpc.PlanificacionServiceGrpc;
import ar.edu.utn.frba.dds.main.HttpClient.ClientPlanificacion;
import ar.edu.utn.frba.dds.planificacion.ItemPlanificacion;
import ar.edu.utn.frba.dds.repositories.planificacion.PlanificacionRepositorio;
import ar.edu.utn.frba.dds.scripts.dto.planificacion.SolicitudPlanificacionDto;
import com.planificacionServiceGrpc.grpc.DonacionAPlanificar;
import com.planificacionServiceGrpc.grpc.DonacionRechazada;
import com.planificacionServiceGrpc.grpc.EnviarDonacionesRequest;
import com.planificacionServiceGrpc.grpc.EnviarDonacionesResponse;
import com.planificacionServiceGrpc.grpc.MotivoRechazo;
import com.planificacionServiceGrpc.grpc.PlaniServiceGrpc;
import com.planificacionServiceGrpc.grpc.PlanificacionRequest;
import com.planificacionServiceGrpc.grpc.PlanificacionResponse;
import io.grpc.Grpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.time.LocalDate;
import java.util.List;

public class GrpcServicePlanificacionImplementation extends PlaniServiceGrpc.PlaniServiceImplBase {

  private final ClientPlanificacion clientPlanificacion;
  private final String callbackUrl;
  private final int tamanioLote;

  public GrpcServicePlanificacionImplementation(
      ClientPlanificacion clientPlanificacion,
      String callbackUrl,
      int tamanioLote) {

    this.clientPlanificacion = clientPlanificacion;
    this.callbackUrl = callbackUrl;
    this.tamanioLote = tamanioLote;
  }

  @Override
  public void enviarDonaciones(EnviarDonacionesRequest request,
                               StreamObserver<EnviarDonacionesResponse> responseObserver) {
    try {
      EnviarDonacionesResponse.Builder resp = EnviarDonacionesResponse.newBuilder();
      int aceptadas = 0;

      for (DonacionAPlanificar d : request.getDonacionesList()) {
        MotivoRechazo motivo = validar(d);
        if (motivo == null && PlanificacionRepositorio.Instance.existeDonacion(d.getDonacionId())) {
          motivo = MotivoRechazo.DUPLICADA;
        }

        if (motivo != null) {
          resp.addRechazadas(DonacionRechazada.newBuilder()
              .setDonacionId(d.getDonacionId()).setMotivo(motivo));
          continue;
        }

        PlanificacionRepositorio.Instance.registrar(aItem(d));
        aceptadas++;
        System.out.println("PLANIFICACION: Donacion " + d.getDonacionId() +
            " Direccion: " + d.getDireccion() +
            " Beneficiario: " + d.getBeneficiarioId());
      }

      responseObserver.onNext(resp.setAceptadas(aceptadas).build());
      responseObserver.onCompleted();

    } catch (Exception e) {
      e.printStackTrace();
      responseObserver.onError(Status.INTERNAL
          .withDescription("Error al registrar las donaciones").withCause(e).asRuntimeException());
    }
  }

  private MotivoRechazo validar(DonacionAPlanificar d) {
    if (d.getDonacionId() == 0 || d.getBeneficiarioId() == 0
        || d.getDireccion().isBlank() || d.getCantidad() <= 0) {
      return MotivoRechazo.DATOS_INVALIDOS;
    }
    return null;
  }

  private ItemPlanificacion aItem(DonacionAPlanificar d) {
    ItemPlanificacion i = new ItemPlanificacion();
    i.setDonacionId(d.getDonacionId());
    i.setBeneficiarioId(d.getBeneficiarioId());
    i.setDireccion(d.getDireccion());
    i.setFechaVencimiento(d.getFechaVencimiento().isBlank()
        ? null : LocalDate.parse(d.getFechaVencimiento()));
    i.setCantidad(d.getCantidad());
    i.setUnidadMedida(d.getUnidadMedida());
    i.setUso(d.getUso());
    return i;
  }

  @Override
  public void iniciarPlanificacionManual(
      PlanificacionRequest request,
      StreamObserver<PlanificacionResponse> responseObserver) {

    SolicitudPlanificacionDto solicitud = null;

    try {
      List<Long> camionesIds = request.getCamionesIdList();

      // Preparar lote y solicitud.
      solicitud = PlanificacionRepositorio.Instance
          .prepararSolicitudManual(
              camionesIds,
              tamanioLote,
              callbackUrl
          );

      // Enviar solicitud al planificador externo.
      clientPlanificacion.enviar(solicitud);

      responseObserver.onNext(
          PlanificacionResponse.newBuilder()
              .setExito(true)
              .setMensaje(
                  "Solicitud de planificación enviada correctamente. "
                      + "Lote: " + solicitud.getLoteId()
              )
              .build()
      );

      responseObserver.onCompleted();

    } catch (Exception e) {

      e.printStackTrace();

      // Si ya se creó el lote, revertirlo cuando falla el envío.
      if (solicitud != null) {
        try {
          PlanificacionRepositorio.Instance
              .revertirLote(solicitud.getLoteId());
        } catch (Exception errorReversion) {
          errorReversion.printStackTrace();
        }
      }

      responseObserver.onNext(
          PlanificacionResponse.newBuilder()
              .setExito(false)
              .setMensaje(
                  "No se pudo iniciar la planificación: "
                      + e.getMessage()
              )
              .build()
      );

      responseObserver.onCompleted();
    }
  }

  private void responderPlanificacion(
      StreamObserver<PlanificacionResponse> observer,
      boolean exito,
      String mensaje) {

    observer.onNext(
        PlanificacionResponse.newBuilder()
            .setExito(exito)
            .setMensaje(mensaje)
            .build()
    );

    observer.onCompleted();
  }
}
