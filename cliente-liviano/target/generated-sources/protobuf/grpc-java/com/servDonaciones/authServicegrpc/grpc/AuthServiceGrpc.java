package com.servDonaciones.authServicegrpc.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * The greeting service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: servDonaciones/autenticacion.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AuthServiceGrpc {

  private AuthServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "autenticacion.AuthService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.servDonaciones.authServicegrpc.grpc.LoginRequest,
      com.servDonaciones.authServicegrpc.grpc.LoginResponse> getAutenticarUsuarioMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AutenticarUsuario",
      requestType = com.servDonaciones.authServicegrpc.grpc.LoginRequest.class,
      responseType = com.servDonaciones.authServicegrpc.grpc.LoginResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.servDonaciones.authServicegrpc.grpc.LoginRequest,
      com.servDonaciones.authServicegrpc.grpc.LoginResponse> getAutenticarUsuarioMethod() {
    io.grpc.MethodDescriptor<com.servDonaciones.authServicegrpc.grpc.LoginRequest, com.servDonaciones.authServicegrpc.grpc.LoginResponse> getAutenticarUsuarioMethod;
    if ((getAutenticarUsuarioMethod = AuthServiceGrpc.getAutenticarUsuarioMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getAutenticarUsuarioMethod = AuthServiceGrpc.getAutenticarUsuarioMethod) == null) {
          AuthServiceGrpc.getAutenticarUsuarioMethod = getAutenticarUsuarioMethod =
              io.grpc.MethodDescriptor.<com.servDonaciones.authServicegrpc.grpc.LoginRequest, com.servDonaciones.authServicegrpc.grpc.LoginResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AutenticarUsuario"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.servDonaciones.authServicegrpc.grpc.LoginRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.servDonaciones.authServicegrpc.grpc.LoginResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("AutenticarUsuario"))
              .build();
        }
      }
    }
    return getAutenticarUsuarioMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.servDonaciones.authServicegrpc.grpc.Empty,
      com.servDonaciones.authServicegrpc.grpc.Usuario> getGetUsuarioMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUsuario",
      requestType = com.servDonaciones.authServicegrpc.grpc.Empty.class,
      responseType = com.servDonaciones.authServicegrpc.grpc.Usuario.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.servDonaciones.authServicegrpc.grpc.Empty,
      com.servDonaciones.authServicegrpc.grpc.Usuario> getGetUsuarioMethod() {
    io.grpc.MethodDescriptor<com.servDonaciones.authServicegrpc.grpc.Empty, com.servDonaciones.authServicegrpc.grpc.Usuario> getGetUsuarioMethod;
    if ((getGetUsuarioMethod = AuthServiceGrpc.getGetUsuarioMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getGetUsuarioMethod = AuthServiceGrpc.getGetUsuarioMethod) == null) {
          AuthServiceGrpc.getGetUsuarioMethod = getGetUsuarioMethod =
              io.grpc.MethodDescriptor.<com.servDonaciones.authServicegrpc.grpc.Empty, com.servDonaciones.authServicegrpc.grpc.Usuario>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUsuario"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.servDonaciones.authServicegrpc.grpc.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.servDonaciones.authServicegrpc.grpc.Usuario.getDefaultInstance()))
              .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("GetUsuario"))
              .build();
        }
      }
    }
    return getGetUsuarioMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.servDonaciones.authServicegrpc.grpc.Empty,
      com.servDonaciones.authServicegrpc.grpc.ListUsuarios> getGetUsuariosMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUsuarios",
      requestType = com.servDonaciones.authServicegrpc.grpc.Empty.class,
      responseType = com.servDonaciones.authServicegrpc.grpc.ListUsuarios.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.servDonaciones.authServicegrpc.grpc.Empty,
      com.servDonaciones.authServicegrpc.grpc.ListUsuarios> getGetUsuariosMethod() {
    io.grpc.MethodDescriptor<com.servDonaciones.authServicegrpc.grpc.Empty, com.servDonaciones.authServicegrpc.grpc.ListUsuarios> getGetUsuariosMethod;
    if ((getGetUsuariosMethod = AuthServiceGrpc.getGetUsuariosMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getGetUsuariosMethod = AuthServiceGrpc.getGetUsuariosMethod) == null) {
          AuthServiceGrpc.getGetUsuariosMethod = getGetUsuariosMethod =
              io.grpc.MethodDescriptor.<com.servDonaciones.authServicegrpc.grpc.Empty, com.servDonaciones.authServicegrpc.grpc.ListUsuarios>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUsuarios"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.servDonaciones.authServicegrpc.grpc.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.servDonaciones.authServicegrpc.grpc.ListUsuarios.getDefaultInstance()))
              .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("GetUsuarios"))
              .build();
        }
      }
    }
    return getGetUsuariosMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AuthServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthServiceStub>() {
        @java.lang.Override
        public AuthServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthServiceStub(channel, callOptions);
        }
      };
    return AuthServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AuthServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthServiceBlockingStub>() {
        @java.lang.Override
        public AuthServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthServiceBlockingStub(channel, callOptions);
        }
      };
    return AuthServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AuthServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthServiceFutureStub>() {
        @java.lang.Override
        public AuthServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthServiceFutureStub(channel, callOptions);
        }
      };
    return AuthServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    default void autenticarUsuario(com.servDonaciones.authServicegrpc.grpc.LoginRequest request,
        io.grpc.stub.StreamObserver<com.servDonaciones.authServicegrpc.grpc.LoginResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAutenticarUsuarioMethod(), responseObserver);
    }

    /**
     */
    default void getUsuario(com.servDonaciones.authServicegrpc.grpc.Empty request,
        io.grpc.stub.StreamObserver<com.servDonaciones.authServicegrpc.grpc.Usuario> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUsuarioMethod(), responseObserver);
    }

    /**
     */
    default void getUsuarios(com.servDonaciones.authServicegrpc.grpc.Empty request,
        io.grpc.stub.StreamObserver<com.servDonaciones.authServicegrpc.grpc.ListUsuarios> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUsuariosMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AuthService.
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static abstract class AuthServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AuthServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AuthService.
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class AuthServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AuthServiceStub> {
    private AuthServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public void autenticarUsuario(com.servDonaciones.authServicegrpc.grpc.LoginRequest request,
        io.grpc.stub.StreamObserver<com.servDonaciones.authServicegrpc.grpc.LoginResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAutenticarUsuarioMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUsuario(com.servDonaciones.authServicegrpc.grpc.Empty request,
        io.grpc.stub.StreamObserver<com.servDonaciones.authServicegrpc.grpc.Usuario> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getGetUsuarioMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getUsuarios(com.servDonaciones.authServicegrpc.grpc.Empty request,
        io.grpc.stub.StreamObserver<com.servDonaciones.authServicegrpc.grpc.ListUsuarios> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUsuariosMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AuthService.
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class AuthServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AuthServiceBlockingStub> {
    private AuthServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public com.servDonaciones.authServicegrpc.grpc.LoginResponse autenticarUsuario(com.servDonaciones.authServicegrpc.grpc.LoginRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAutenticarUsuarioMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.servDonaciones.authServicegrpc.grpc.Usuario> getUsuario(
        com.servDonaciones.authServicegrpc.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getGetUsuarioMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.servDonaciones.authServicegrpc.grpc.ListUsuarios getUsuarios(com.servDonaciones.authServicegrpc.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUsuariosMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AuthService.
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static final class AuthServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AuthServiceFutureStub> {
    private AuthServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a greeting
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.servDonaciones.authServicegrpc.grpc.LoginResponse> autenticarUsuario(
        com.servDonaciones.authServicegrpc.grpc.LoginRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAutenticarUsuarioMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.servDonaciones.authServicegrpc.grpc.ListUsuarios> getUsuarios(
        com.servDonaciones.authServicegrpc.grpc.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUsuariosMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_AUTENTICAR_USUARIO = 0;
  private static final int METHODID_GET_USUARIO = 1;
  private static final int METHODID_GET_USUARIOS = 2;

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
        case METHODID_AUTENTICAR_USUARIO:
          serviceImpl.autenticarUsuario((com.servDonaciones.authServicegrpc.grpc.LoginRequest) request,
              (io.grpc.stub.StreamObserver<com.servDonaciones.authServicegrpc.grpc.LoginResponse>) responseObserver);
          break;
        case METHODID_GET_USUARIO:
          serviceImpl.getUsuario((com.servDonaciones.authServicegrpc.grpc.Empty) request,
              (io.grpc.stub.StreamObserver<com.servDonaciones.authServicegrpc.grpc.Usuario>) responseObserver);
          break;
        case METHODID_GET_USUARIOS:
          serviceImpl.getUsuarios((com.servDonaciones.authServicegrpc.grpc.Empty) request,
              (io.grpc.stub.StreamObserver<com.servDonaciones.authServicegrpc.grpc.ListUsuarios>) responseObserver);
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
          getAutenticarUsuarioMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.servDonaciones.authServicegrpc.grpc.LoginRequest,
              com.servDonaciones.authServicegrpc.grpc.LoginResponse>(
                service, METHODID_AUTENTICAR_USUARIO)))
        .addMethod(
          getGetUsuarioMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.servDonaciones.authServicegrpc.grpc.Empty,
              com.servDonaciones.authServicegrpc.grpc.Usuario>(
                service, METHODID_GET_USUARIO)))
        .addMethod(
          getGetUsuariosMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.servDonaciones.authServicegrpc.grpc.Empty,
              com.servDonaciones.authServicegrpc.grpc.ListUsuarios>(
                service, METHODID_GET_USUARIOS)))
        .build();
  }

  private static abstract class AuthServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AuthServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.servDonaciones.authServicegrpc.grpc.Autenticacion.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AuthService");
    }
  }

  private static final class AuthServiceFileDescriptorSupplier
      extends AuthServiceBaseDescriptorSupplier {
    AuthServiceFileDescriptorSupplier() {}
  }

  private static final class AuthServiceMethodDescriptorSupplier
      extends AuthServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AuthServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (AuthServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AuthServiceFileDescriptorSupplier())
              .addMethod(getAutenticarUsuarioMethod())
              .addMethod(getGetUsuarioMethod())
              .addMethod(getGetUsuariosMethod())
              .build();
        }
      }
    }
    return result;
  }
}
