package ar.edu.utn.frba.dds.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: necesidadesgrpc.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class NecesidadServiceGrpc {

  private NecesidadServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "NecesidadService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest,
      ar.edu.utn.frba.dds.grpc.NecesidadResponse> getCrearNecesidadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CrearNecesidad",
      requestType = ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest.class,
      responseType = ar.edu.utn.frba.dds.grpc.NecesidadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest,
      ar.edu.utn.frba.dds.grpc.NecesidadResponse> getCrearNecesidadMethod() {
    io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest, ar.edu.utn.frba.dds.grpc.NecesidadResponse> getCrearNecesidadMethod;
    if ((getCrearNecesidadMethod = NecesidadServiceGrpc.getCrearNecesidadMethod) == null) {
      synchronized (NecesidadServiceGrpc.class) {
        if ((getCrearNecesidadMethod = NecesidadServiceGrpc.getCrearNecesidadMethod) == null) {
          NecesidadServiceGrpc.getCrearNecesidadMethod = getCrearNecesidadMethod =
              io.grpc.MethodDescriptor.<ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest, ar.edu.utn.frba.dds.grpc.NecesidadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CrearNecesidad"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.NecesidadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NecesidadServiceMethodDescriptorSupplier("CrearNecesidad"))
              .build();
        }
      }
    }
    return getCrearNecesidadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest,
      ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse> getAgregarPeticionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AgregarPeticion",
      requestType = ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest.class,
      responseType = ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest,
      ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse> getAgregarPeticionMethod() {
    io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest, ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse> getAgregarPeticionMethod;
    if ((getAgregarPeticionMethod = NecesidadServiceGrpc.getAgregarPeticionMethod) == null) {
      synchronized (NecesidadServiceGrpc.class) {
        if ((getAgregarPeticionMethod = NecesidadServiceGrpc.getAgregarPeticionMethod) == null) {
          NecesidadServiceGrpc.getAgregarPeticionMethod = getAgregarPeticionMethod =
              io.grpc.MethodDescriptor.<ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest, ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AgregarPeticion"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NecesidadServiceMethodDescriptorSupplier("AgregarPeticion"))
              .build();
        }
      }
    }
    return getAgregarPeticionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.EntidadIdRequest,
      ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse> getListarNecesidadesPorEntidadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListarNecesidadesPorEntidad",
      requestType = ar.edu.utn.frba.dds.grpc.EntidadIdRequest.class,
      responseType = ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.EntidadIdRequest,
      ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse> getListarNecesidadesPorEntidadMethod() {
    io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.EntidadIdRequest, ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse> getListarNecesidadesPorEntidadMethod;
    if ((getListarNecesidadesPorEntidadMethod = NecesidadServiceGrpc.getListarNecesidadesPorEntidadMethod) == null) {
      synchronized (NecesidadServiceGrpc.class) {
        if ((getListarNecesidadesPorEntidadMethod = NecesidadServiceGrpc.getListarNecesidadesPorEntidadMethod) == null) {
          NecesidadServiceGrpc.getListarNecesidadesPorEntidadMethod = getListarNecesidadesPorEntidadMethod =
              io.grpc.MethodDescriptor.<ar.edu.utn.frba.dds.grpc.EntidadIdRequest, ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListarNecesidadesPorEntidad"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.EntidadIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NecesidadServiceMethodDescriptorSupplier("ListarNecesidadesPorEntidad"))
              .build();
        }
      }
    }
    return getListarNecesidadesPorEntidadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.NecesidadIdRequest,
      ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse> getObtenerNecesidadDetalleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ObtenerNecesidadDetalle",
      requestType = ar.edu.utn.frba.dds.grpc.NecesidadIdRequest.class,
      responseType = ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.NecesidadIdRequest,
      ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse> getObtenerNecesidadDetalleMethod() {
    io.grpc.MethodDescriptor<ar.edu.utn.frba.dds.grpc.NecesidadIdRequest, ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse> getObtenerNecesidadDetalleMethod;
    if ((getObtenerNecesidadDetalleMethod = NecesidadServiceGrpc.getObtenerNecesidadDetalleMethod) == null) {
      synchronized (NecesidadServiceGrpc.class) {
        if ((getObtenerNecesidadDetalleMethod = NecesidadServiceGrpc.getObtenerNecesidadDetalleMethod) == null) {
          NecesidadServiceGrpc.getObtenerNecesidadDetalleMethod = getObtenerNecesidadDetalleMethod =
              io.grpc.MethodDescriptor.<ar.edu.utn.frba.dds.grpc.NecesidadIdRequest, ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ObtenerNecesidadDetalle"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.NecesidadIdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NecesidadServiceMethodDescriptorSupplier("ObtenerNecesidadDetalle"))
              .build();
        }
      }
    }
    return getObtenerNecesidadDetalleMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NecesidadServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NecesidadServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NecesidadServiceStub>() {
        @java.lang.Override
        public NecesidadServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NecesidadServiceStub(channel, callOptions);
        }
      };
    return NecesidadServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NecesidadServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NecesidadServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NecesidadServiceBlockingStub>() {
        @java.lang.Override
        public NecesidadServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NecesidadServiceBlockingStub(channel, callOptions);
        }
      };
    return NecesidadServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NecesidadServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NecesidadServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NecesidadServiceFutureStub>() {
        @java.lang.Override
        public NecesidadServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NecesidadServiceFutureStub(channel, callOptions);
        }
      };
    return NecesidadServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void crearNecesidad(ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.NecesidadResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCrearNecesidadMethod(), responseObserver);
    }

    /**
     */
    default void agregarPeticion(ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAgregarPeticionMethod(), responseObserver);
    }

    /**
     */
    default void listarNecesidadesPorEntidad(ar.edu.utn.frba.dds.grpc.EntidadIdRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListarNecesidadesPorEntidadMethod(), responseObserver);
    }

    /**
     */
    default void obtenerNecesidadDetalle(ar.edu.utn.frba.dds.grpc.NecesidadIdRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getObtenerNecesidadDetalleMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service NecesidadService.
   */
  public static abstract class NecesidadServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return NecesidadServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service NecesidadService.
   */
  public static final class NecesidadServiceStub
      extends io.grpc.stub.AbstractAsyncStub<NecesidadServiceStub> {
    private NecesidadServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NecesidadServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NecesidadServiceStub(channel, callOptions);
    }

    /**
     */
    public void crearNecesidad(ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.NecesidadResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCrearNecesidadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void agregarPeticion(ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAgregarPeticionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listarNecesidadesPorEntidad(ar.edu.utn.frba.dds.grpc.EntidadIdRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListarNecesidadesPorEntidadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void obtenerNecesidadDetalle(ar.edu.utn.frba.dds.grpc.NecesidadIdRequest request,
        io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getObtenerNecesidadDetalleMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service NecesidadService.
   */
  public static final class NecesidadServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<NecesidadServiceBlockingStub> {
    private NecesidadServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NecesidadServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NecesidadServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public ar.edu.utn.frba.dds.grpc.NecesidadResponse crearNecesidad(ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCrearNecesidadMethod(), getCallOptions(), request);
    }

    /**
     */
    public ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse agregarPeticion(ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAgregarPeticionMethod(), getCallOptions(), request);
    }

    /**
     */
    public ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse listarNecesidadesPorEntidad(ar.edu.utn.frba.dds.grpc.EntidadIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListarNecesidadesPorEntidadMethod(), getCallOptions(), request);
    }

    /**
     */
    public ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse obtenerNecesidadDetalle(ar.edu.utn.frba.dds.grpc.NecesidadIdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getObtenerNecesidadDetalleMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service NecesidadService.
   */
  public static final class NecesidadServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<NecesidadServiceFutureStub> {
    private NecesidadServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NecesidadServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NecesidadServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ar.edu.utn.frba.dds.grpc.NecesidadResponse> crearNecesidad(
        ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCrearNecesidadMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse> agregarPeticion(
        ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAgregarPeticionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse> listarNecesidadesPorEntidad(
        ar.edu.utn.frba.dds.grpc.EntidadIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListarNecesidadesPorEntidadMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse> obtenerNecesidadDetalle(
        ar.edu.utn.frba.dds.grpc.NecesidadIdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getObtenerNecesidadDetalleMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREAR_NECESIDAD = 0;
  private static final int METHODID_AGREGAR_PETICION = 1;
  private static final int METHODID_LISTAR_NECESIDADES_POR_ENTIDAD = 2;
  private static final int METHODID_OBTENER_NECESIDAD_DETALLE = 3;

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
        case METHODID_CREAR_NECESIDAD:
          serviceImpl.crearNecesidad((ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest) request,
              (io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.NecesidadResponse>) responseObserver);
          break;
        case METHODID_AGREGAR_PETICION:
          serviceImpl.agregarPeticion((ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest) request,
              (io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse>) responseObserver);
          break;
        case METHODID_LISTAR_NECESIDADES_POR_ENTIDAD:
          serviceImpl.listarNecesidadesPorEntidad((ar.edu.utn.frba.dds.grpc.EntidadIdRequest) request,
              (io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse>) responseObserver);
          break;
        case METHODID_OBTENER_NECESIDAD_DETALLE:
          serviceImpl.obtenerNecesidadDetalle((ar.edu.utn.frba.dds.grpc.NecesidadIdRequest) request,
              (io.grpc.stub.StreamObserver<ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse>) responseObserver);
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
          getCrearNecesidadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ar.edu.utn.frba.dds.grpc.CrearNecesidadRequest,
              ar.edu.utn.frba.dds.grpc.NecesidadResponse>(
                service, METHODID_CREAR_NECESIDAD)))
        .addMethod(
          getAgregarPeticionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ar.edu.utn.frba.dds.grpc.AgregarPeticionRequest,
              ar.edu.utn.frba.dds.grpc.OperacionNecesidadResponse>(
                service, METHODID_AGREGAR_PETICION)))
        .addMethod(
          getListarNecesidadesPorEntidadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ar.edu.utn.frba.dds.grpc.EntidadIdRequest,
              ar.edu.utn.frba.dds.grpc.ListaNecesidadesResponse>(
                service, METHODID_LISTAR_NECESIDADES_POR_ENTIDAD)))
        .addMethod(
          getObtenerNecesidadDetalleMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ar.edu.utn.frba.dds.grpc.NecesidadIdRequest,
              ar.edu.utn.frba.dds.grpc.NecesidadDetailResponse>(
                service, METHODID_OBTENER_NECESIDAD_DETALLE)))
        .build();
  }

  private static abstract class NecesidadServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NecesidadServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ar.edu.utn.frba.dds.grpc.Necesidadesgrpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NecesidadService");
    }
  }

  private static final class NecesidadServiceFileDescriptorSupplier
      extends NecesidadServiceBaseDescriptorSupplier {
    NecesidadServiceFileDescriptorSupplier() {}
  }

  private static final class NecesidadServiceMethodDescriptorSupplier
      extends NecesidadServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    NecesidadServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (NecesidadServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NecesidadServiceFileDescriptorSupplier())
              .addMethod(getCrearNecesidadMethod())
              .addMethod(getAgregarPeticionMethod())
              .addMethod(getListarNecesidadesPorEntidadMethod())
              .addMethod(getObtenerNecesidadDetalleMethod())
              .build();
        }
      }
    }
    return result;
  }
}
