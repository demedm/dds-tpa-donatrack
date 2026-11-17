package com.servLogistica.monitoreoServiceGrpc.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: servLogistica/monitoreo.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class monitoreoServiceGrpc {

  private monitoreoServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "monitoreo.monitoreoService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.servLogistica.monitoreoServiceGrpc.grpc.Camion,
      com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion> getGetUbicacionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetUbicacion",
      requestType = com.servLogistica.monitoreoServiceGrpc.grpc.Camion.class,
      responseType = com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.servLogistica.monitoreoServiceGrpc.grpc.Camion,
      com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion> getGetUbicacionMethod() {
    io.grpc.MethodDescriptor<com.servLogistica.monitoreoServiceGrpc.grpc.Camion, com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion> getGetUbicacionMethod;
    if ((getGetUbicacionMethod = monitoreoServiceGrpc.getGetUbicacionMethod) == null) {
      synchronized (monitoreoServiceGrpc.class) {
        if ((getGetUbicacionMethod = monitoreoServiceGrpc.getGetUbicacionMethod) == null) {
          monitoreoServiceGrpc.getGetUbicacionMethod = getGetUbicacionMethod =
              io.grpc.MethodDescriptor.<com.servLogistica.monitoreoServiceGrpc.grpc.Camion, com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetUbicacion"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.servLogistica.monitoreoServiceGrpc.grpc.Camion.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion.getDefaultInstance()))
              .setSchemaDescriptor(new monitoreoServiceMethodDescriptorSupplier("GetUbicacion"))
              .build();
        }
      }
    }
    return getGetUbicacionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static monitoreoServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<monitoreoServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<monitoreoServiceStub>() {
        @java.lang.Override
        public monitoreoServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new monitoreoServiceStub(channel, callOptions);
        }
      };
    return monitoreoServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static monitoreoServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<monitoreoServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<monitoreoServiceBlockingStub>() {
        @java.lang.Override
        public monitoreoServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new monitoreoServiceBlockingStub(channel, callOptions);
        }
      };
    return monitoreoServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static monitoreoServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<monitoreoServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<monitoreoServiceFutureStub>() {
        @java.lang.Override
        public monitoreoServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new monitoreoServiceFutureStub(channel, callOptions);
        }
      };
    return monitoreoServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getUbicacion(com.servLogistica.monitoreoServiceGrpc.grpc.Camion request,
        io.grpc.stub.StreamObserver<com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetUbicacionMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service monitoreoService.
   */
  public static abstract class monitoreoServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return monitoreoServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service monitoreoService.
   */
  public static final class monitoreoServiceStub
      extends io.grpc.stub.AbstractAsyncStub<monitoreoServiceStub> {
    private monitoreoServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected monitoreoServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new monitoreoServiceStub(channel, callOptions);
    }

    /**
     */
    public void getUbicacion(com.servLogistica.monitoreoServiceGrpc.grpc.Camion request,
        io.grpc.stub.StreamObserver<com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetUbicacionMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service monitoreoService.
   */
  public static final class monitoreoServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<monitoreoServiceBlockingStub> {
    private monitoreoServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected monitoreoServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new monitoreoServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion getUbicacion(com.servLogistica.monitoreoServiceGrpc.grpc.Camion request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetUbicacionMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service monitoreoService.
   */
  public static final class monitoreoServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<monitoreoServiceFutureStub> {
    private monitoreoServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected monitoreoServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new monitoreoServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion> getUbicacion(
        com.servLogistica.monitoreoServiceGrpc.grpc.Camion request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetUbicacionMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_UBICACION = 0;

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
        case METHODID_GET_UBICACION:
          serviceImpl.getUbicacion((com.servLogistica.monitoreoServiceGrpc.grpc.Camion) request,
              (io.grpc.stub.StreamObserver<com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion>) responseObserver);
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
          getGetUbicacionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.servLogistica.monitoreoServiceGrpc.grpc.Camion,
              com.servLogistica.monitoreoServiceGrpc.grpc.Ubicacion>(
                service, METHODID_GET_UBICACION)))
        .build();
  }

  private static abstract class monitoreoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    monitoreoServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.servLogistica.monitoreoServiceGrpc.grpc.Monitoreo.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("monitoreoService");
    }
  }

  private static final class monitoreoServiceFileDescriptorSupplier
      extends monitoreoServiceBaseDescriptorSupplier {
    monitoreoServiceFileDescriptorSupplier() {}
  }

  private static final class monitoreoServiceMethodDescriptorSupplier
      extends monitoreoServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    monitoreoServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (monitoreoServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new monitoreoServiceFileDescriptorSupplier())
              .addMethod(getGetUbicacionMethod())
              .build();
        }
      }
    }
    return result;
  }
}
