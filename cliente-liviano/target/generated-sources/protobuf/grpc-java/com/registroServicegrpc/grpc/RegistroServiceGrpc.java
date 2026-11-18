package com.registroServicegrpc.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: registro.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class RegistroServiceGrpc {

  private RegistroServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "registro.RegistroService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.registroServicegrpc.grpc.RegistroPersonaRequest,
      com.registroServicegrpc.grpc.RegistroResponse> getRegistrarPersonaMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegistrarPersona",
      requestType = com.registroServicegrpc.grpc.RegistroPersonaRequest.class,
      responseType = com.registroServicegrpc.grpc.RegistroResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.registroServicegrpc.grpc.RegistroPersonaRequest,
      com.registroServicegrpc.grpc.RegistroResponse> getRegistrarPersonaMethod() {
    io.grpc.MethodDescriptor<com.registroServicegrpc.grpc.RegistroPersonaRequest, com.registroServicegrpc.grpc.RegistroResponse> getRegistrarPersonaMethod;
    if ((getRegistrarPersonaMethod = RegistroServiceGrpc.getRegistrarPersonaMethod) == null) {
      synchronized (RegistroServiceGrpc.class) {
        if ((getRegistrarPersonaMethod = RegistroServiceGrpc.getRegistrarPersonaMethod) == null) {
          RegistroServiceGrpc.getRegistrarPersonaMethod = getRegistrarPersonaMethod =
              io.grpc.MethodDescriptor.<com.registroServicegrpc.grpc.RegistroPersonaRequest, com.registroServicegrpc.grpc.RegistroResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegistrarPersona"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.registroServicegrpc.grpc.RegistroPersonaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.registroServicegrpc.grpc.RegistroResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RegistroServiceMethodDescriptorSupplier("RegistrarPersona"))
              .build();
        }
      }
    }
    return getRegistrarPersonaMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.registroServicegrpc.grpc.RegistroBeneficiariaRequest,
      com.registroServicegrpc.grpc.RegistroResponse> getRegistrarEntidadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegistrarEntidad",
      requestType = com.registroServicegrpc.grpc.RegistroBeneficiariaRequest.class,
      responseType = com.registroServicegrpc.grpc.RegistroResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.registroServicegrpc.grpc.RegistroBeneficiariaRequest,
      com.registroServicegrpc.grpc.RegistroResponse> getRegistrarEntidadMethod() {
    io.grpc.MethodDescriptor<com.registroServicegrpc.grpc.RegistroBeneficiariaRequest, com.registroServicegrpc.grpc.RegistroResponse> getRegistrarEntidadMethod;
    if ((getRegistrarEntidadMethod = RegistroServiceGrpc.getRegistrarEntidadMethod) == null) {
      synchronized (RegistroServiceGrpc.class) {
        if ((getRegistrarEntidadMethod = RegistroServiceGrpc.getRegistrarEntidadMethod) == null) {
          RegistroServiceGrpc.getRegistrarEntidadMethod = getRegistrarEntidadMethod =
              io.grpc.MethodDescriptor.<com.registroServicegrpc.grpc.RegistroBeneficiariaRequest, com.registroServicegrpc.grpc.RegistroResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegistrarEntidad"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.registroServicegrpc.grpc.RegistroBeneficiariaRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.registroServicegrpc.grpc.RegistroResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RegistroServiceMethodDescriptorSupplier("RegistrarEntidad"))
              .build();
        }
      }
    }
    return getRegistrarEntidadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.registroServicegrpc.grpc.RegistroUsuarioRequest,
      com.registroServicegrpc.grpc.RegistroResponse> getRegistrarUsuarioMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegistrarUsuario",
      requestType = com.registroServicegrpc.grpc.RegistroUsuarioRequest.class,
      responseType = com.registroServicegrpc.grpc.RegistroResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.registroServicegrpc.grpc.RegistroUsuarioRequest,
      com.registroServicegrpc.grpc.RegistroResponse> getRegistrarUsuarioMethod() {
    io.grpc.MethodDescriptor<com.registroServicegrpc.grpc.RegistroUsuarioRequest, com.registroServicegrpc.grpc.RegistroResponse> getRegistrarUsuarioMethod;
    if ((getRegistrarUsuarioMethod = RegistroServiceGrpc.getRegistrarUsuarioMethod) == null) {
      synchronized (RegistroServiceGrpc.class) {
        if ((getRegistrarUsuarioMethod = RegistroServiceGrpc.getRegistrarUsuarioMethod) == null) {
          RegistroServiceGrpc.getRegistrarUsuarioMethod = getRegistrarUsuarioMethod =
              io.grpc.MethodDescriptor.<com.registroServicegrpc.grpc.RegistroUsuarioRequest, com.registroServicegrpc.grpc.RegistroResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegistrarUsuario"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.registroServicegrpc.grpc.RegistroUsuarioRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.registroServicegrpc.grpc.RegistroResponse.getDefaultInstance()))
              .setSchemaDescriptor(new RegistroServiceMethodDescriptorSupplier("RegistrarUsuario"))
              .build();
        }
      }
    }
    return getRegistrarUsuarioMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RegistroServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RegistroServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RegistroServiceStub>() {
        @java.lang.Override
        public RegistroServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RegistroServiceStub(channel, callOptions);
        }
      };
    return RegistroServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RegistroServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RegistroServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RegistroServiceBlockingStub>() {
        @java.lang.Override
        public RegistroServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RegistroServiceBlockingStub(channel, callOptions);
        }
      };
    return RegistroServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RegistroServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RegistroServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RegistroServiceFutureStub>() {
        @java.lang.Override
        public RegistroServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RegistroServiceFutureStub(channel, callOptions);
        }
      };
    return RegistroServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void registrarPersona(com.registroServicegrpc.grpc.RegistroPersonaRequest request,
        io.grpc.stub.StreamObserver<com.registroServicegrpc.grpc.RegistroResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegistrarPersonaMethod(), responseObserver);
    }

    /**
     */
    default void registrarEntidad(com.registroServicegrpc.grpc.RegistroBeneficiariaRequest request,
        io.grpc.stub.StreamObserver<com.registroServicegrpc.grpc.RegistroResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegistrarEntidadMethod(), responseObserver);
    }

    /**
     */
    default void registrarUsuario(com.registroServicegrpc.grpc.RegistroUsuarioRequest request,
        io.grpc.stub.StreamObserver<com.registroServicegrpc.grpc.RegistroResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegistrarUsuarioMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service RegistroService.
   */
  public static abstract class RegistroServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return RegistroServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service RegistroService.
   */
  public static final class RegistroServiceStub
      extends io.grpc.stub.AbstractAsyncStub<RegistroServiceStub> {
    private RegistroServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RegistroServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RegistroServiceStub(channel, callOptions);
    }

    /**
     */
    public void registrarPersona(com.registroServicegrpc.grpc.RegistroPersonaRequest request,
        io.grpc.stub.StreamObserver<com.registroServicegrpc.grpc.RegistroResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegistrarPersonaMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registrarEntidad(com.registroServicegrpc.grpc.RegistroBeneficiariaRequest request,
        io.grpc.stub.StreamObserver<com.registroServicegrpc.grpc.RegistroResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegistrarEntidadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registrarUsuario(com.registroServicegrpc.grpc.RegistroUsuarioRequest request,
        io.grpc.stub.StreamObserver<com.registroServicegrpc.grpc.RegistroResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegistrarUsuarioMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service RegistroService.
   */
  public static final class RegistroServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<RegistroServiceBlockingStub> {
    private RegistroServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RegistroServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RegistroServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.registroServicegrpc.grpc.RegistroResponse registrarPersona(com.registroServicegrpc.grpc.RegistroPersonaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegistrarPersonaMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.registroServicegrpc.grpc.RegistroResponse registrarEntidad(com.registroServicegrpc.grpc.RegistroBeneficiariaRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegistrarEntidadMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.registroServicegrpc.grpc.RegistroResponse registrarUsuario(com.registroServicegrpc.grpc.RegistroUsuarioRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegistrarUsuarioMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service RegistroService.
   */
  public static final class RegistroServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<RegistroServiceFutureStub> {
    private RegistroServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RegistroServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RegistroServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.registroServicegrpc.grpc.RegistroResponse> registrarPersona(
        com.registroServicegrpc.grpc.RegistroPersonaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegistrarPersonaMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.registroServicegrpc.grpc.RegistroResponse> registrarEntidad(
        com.registroServicegrpc.grpc.RegistroBeneficiariaRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegistrarEntidadMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.registroServicegrpc.grpc.RegistroResponse> registrarUsuario(
        com.registroServicegrpc.grpc.RegistroUsuarioRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegistrarUsuarioMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REGISTRAR_PERSONA = 0;
  private static final int METHODID_REGISTRAR_ENTIDAD = 1;
  private static final int METHODID_REGISTRAR_USUARIO = 2;

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
        case METHODID_REGISTRAR_PERSONA:
          serviceImpl.registrarPersona((com.registroServicegrpc.grpc.RegistroPersonaRequest) request,
              (io.grpc.stub.StreamObserver<com.registroServicegrpc.grpc.RegistroResponse>) responseObserver);
          break;
        case METHODID_REGISTRAR_ENTIDAD:
          serviceImpl.registrarEntidad((com.registroServicegrpc.grpc.RegistroBeneficiariaRequest) request,
              (io.grpc.stub.StreamObserver<com.registroServicegrpc.grpc.RegistroResponse>) responseObserver);
          break;
        case METHODID_REGISTRAR_USUARIO:
          serviceImpl.registrarUsuario((com.registroServicegrpc.grpc.RegistroUsuarioRequest) request,
              (io.grpc.stub.StreamObserver<com.registroServicegrpc.grpc.RegistroResponse>) responseObserver);
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
          getRegistrarPersonaMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.registroServicegrpc.grpc.RegistroPersonaRequest,
              com.registroServicegrpc.grpc.RegistroResponse>(
                service, METHODID_REGISTRAR_PERSONA)))
        .addMethod(
          getRegistrarEntidadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.registroServicegrpc.grpc.RegistroBeneficiariaRequest,
              com.registroServicegrpc.grpc.RegistroResponse>(
                service, METHODID_REGISTRAR_ENTIDAD)))
        .addMethod(
          getRegistrarUsuarioMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.registroServicegrpc.grpc.RegistroUsuarioRequest,
              com.registroServicegrpc.grpc.RegistroResponse>(
                service, METHODID_REGISTRAR_USUARIO)))
        .build();
  }

  private static abstract class RegistroServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RegistroServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.registroServicegrpc.grpc.Registro.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RegistroService");
    }
  }

  private static final class RegistroServiceFileDescriptorSupplier
      extends RegistroServiceBaseDescriptorSupplier {
    RegistroServiceFileDescriptorSupplier() {}
  }

  private static final class RegistroServiceMethodDescriptorSupplier
      extends RegistroServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    RegistroServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (RegistroServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RegistroServiceFileDescriptorSupplier())
              .addMethod(getRegistrarPersonaMethod())
              .addMethod(getRegistrarEntidadMethod())
              .addMethod(getRegistrarUsuarioMethod())
              .build();
        }
      }
    }
    return result;
  }
}
