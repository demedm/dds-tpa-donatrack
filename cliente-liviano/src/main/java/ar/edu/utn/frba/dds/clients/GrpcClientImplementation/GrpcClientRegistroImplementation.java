package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import com.authServicegrpc.grpc.AuthServiceGrpc;
import com.authServicegrpc.grpc.LoginResponse;
import com.registroServicegrpc.grpc.RegistroResponse;
import com.registroServicegrpc.grpc.RegistroServiceGrpc;
import com.registroServicegrpc.grpc.RegistroUsuarioRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.mindrot.jbcrypt.BCrypt;

public class GrpcClientRegistroImplementation {
  private final RegistroServiceGrpc.RegistroServiceBlockingStub stub;

  public GrpcClientRegistroImplementation() {
    String host = System.getenv().getOrDefault("DONACIONES_GRPC_HOST", "localhost");
    int port = Integer.parseInt(System.getenv().getOrDefault("DONACIONES_GRPC_PORT", "9090"));

    ManagedChannel channel = ManagedChannelBuilder
        .forAddress(host, port)
        .usePlaintext()
        .build();

    this.stub = RegistroServiceGrpc.newBlockingStub(channel);
  }

  public RegistroResponse registrarUsuario(String email, String password, String rol) {
    String hashpass = BCrypt.hashpw(password, BCrypt.gensalt());
    return stub.registrarUsuario(
        RegistroUsuarioRequest.newBuilder()
            .setMail(email)
            .setPassword(hashpass)
            .setRol(rol)
            .build()
    );
  }
}
