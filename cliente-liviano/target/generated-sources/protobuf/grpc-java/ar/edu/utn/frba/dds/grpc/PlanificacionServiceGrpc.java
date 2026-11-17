package ar.edu.utn.frba.dds.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: logisticagrpc.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class PlanificacionServiceGrpc {

  private PlanificacionServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "PlanificacionService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest,
      ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse> getSolicitarPlanificacionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SolicitarPlanificacion",
      requestType = ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest.class,
      responseType = ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest,
      ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse> getSolicitarPlanificacionMethod() {
    io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest, ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse> getSolicitarPlanificacionMethod;
    if ((getSolicitarPlanificacionMethod = PlanificacionServiceGrpc.getSolicitarPlanificacionMethod) == null) {
      synchronized (PlanificacionServiceGrpc.class) {
        if ((getSolicitarPlanificacionMethod = PlanificacionServiceGrpc.getSolicitarPlanificacionMethod) == null) {
          PlanificacionServiceGrpc.getSolicitarPlanificacionMethod = getSolicitarPlanificacionMethod =
              io.grpc.MethodDescriptor.<ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest, ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SolicitarPlanificacion"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PlanificacionServiceMethodDescriptorSupplier("SolicitarPlanificacion"))
              .build();
        }
      }
    }
    return getSolicitarPlanificacionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest,
      ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse> getObtenerEntregasEnCursoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ObtenerEntregasEnCurso",
      requestType = ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest.class,
      responseType = ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest,
      ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse> getObtenerEntregasEnCursoMethod() {
    io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest, ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse> getObtenerEntregasEnCursoMethod;
    if ((getObtenerEntregasEnCursoMethod = PlanificacionServiceGrpc.getObtenerEntregasEnCursoMethod) == null) {
      synchronized (PlanificacionServiceGrpc.class) {
        if ((getObtenerEntregasEnCursoMethod = PlanificacionServiceGrpc.getObtenerEntregasEnCursoMethod) == null) {
          PlanificacionServiceGrpc.getObtenerEntregasEnCursoMethod = getObtenerEntregasEnCursoMethod =
              io.grpc.MethodDescriptor.<ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest, ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ObtenerEntregasEnCurso"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PlanificacionServiceMethodDescriptorSupplier("ObtenerEntregasEnCurso"))
              .build();
        }
      }
    }
    return getObtenerEntregasEnCursoMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PlanificacionServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PlanificacionServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PlanificacionServiceStub>() {
        @java.lang.Override
        public PlanificacionServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PlanificacionServiceStub(channel, callOptions);
        }
      };
    return PlanificacionServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PlanificacionServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PlanificacionServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PlanificacionServiceBlockingStub>() {
        @java.lang.Override
        public PlanificacionServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PlanificacionServiceBlockingStub(channel, callOptions);
        }
      };
    return PlanificacionServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PlanificacionServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PlanificacionServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PlanificacionServiceFutureStub>() {
        @java.lang.Override
        public PlanificacionServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PlanificacionServiceFutureStub(channel, callOptions);
        }
      };
    return PlanificacionServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void solicitarPlanificacion(ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSolicitarPlanificacionMethod(), responseObserver);
    }

    /**
     */
    default void obtenerEntregasEnCurso(ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getObtenerEntregasEnCursoMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service PlanificacionService.
   */
  public static abstract class PlanificacionServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return PlanificacionServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service PlanificacionService.
   */
  public static final class PlanificacionServiceStub
      extends io.grpc.stub.AbstractAsyncStub<PlanificacionServiceStub> {
    private PlanificacionServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PlanificacionServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PlanificacionServiceStub(channel, callOptions);
    }

    /**
     */
    public void solicitarPlanificacion(ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSolicitarPlanificacionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void obtenerEntregasEnCurso(ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getObtenerEntregasEnCursoMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service PlanificacionService.
   */
  public static final class PlanificacionServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<PlanificacionServiceBlockingStub> {
    private PlanificacionServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PlanificacionServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PlanificacionServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse solicitarPlanificacion(ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSolicitarPlanificacionMethod(), getCallOptions(), request);
    }

    /**
     */
    public ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse obtenerEntregasEnCurso(ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getObtenerEntregasEnCursoMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service PlanificacionService.
   */
  public static final class PlanificacionServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<PlanificacionServiceFutureStub> {
    private PlanificacionServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PlanificacionServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PlanificacionServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse> solicitarPlanificacion(
        ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSolicitarPlanificacionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse> obtenerEntregasEnCurso(
        ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getObtenerEntregasEnCursoMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SOLICITAR_PLANIFICACION = 0;
  private static final int METHODID_OBTENER_ENTREGAS_EN_CURSO = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SOLICITAR_PLANIFICACION:
          serviceImpl.solicitarPlanificacion((ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest) request,
              (io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse>) responseObserver);
          break;
        case METHODID_OBTENER_ENTREGAS_EN_CURSO:
          serviceImpl.obtenerEntregasEnCurso((ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest) request,
              (io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSolicitarPlanificacionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ar.edu.utn.frba.dds.grpc.SolicitudPlanificacionRequest,
              ar.edu.utn.frba.dds.grpc.ConfirmationPlanificacionResponse>(
                service, METHODID_SOLICITAR_PLANIFICACION)))
        .addMethod(
          getObtenerEntregasEnCursoMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ar.edu.utn.frba.dds.grpc.EntregasEnCursoRequest,
              ar.edu.utn.frba.dds.grpc.EntregasEnCursoResponse>(
                service, METHODID_OBTENER_ENTREGAS_EN_CURSO)))
        .build();
  }

  private static abstract class PlanificacionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PlanificacionServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ar.edu.utn.frba.dds.grpc.Logisticagrpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PlanificacionService");
    }
  }

  private static final class PlanificacionServiceFileDescriptorSupplier
      extends PlanificacionServiceBaseDescriptorSupplier {
    PlanificacionServiceFileDescriptorSupplier() {}
  }

  private static final class PlanificacionServiceMethodDescriptorSupplier
      extends PlanificacionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    PlanificacionServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PlanificacionServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PlanificacionServiceFileDescriptorSupplier())
              .addMethod(getSolicitarPlanificacionMethod())
              .addMethod(getObtenerEntregasEnCursoMethod())
              .build();
        }
      }
    }
    return result;
  }
}
