package ar.edu.utn.frba.dds.clients.GrpcClientImplementation;

import com.authServicegrpc.grpc.AuthServiceGrpc;
import com.authServicegrpc.grpc.LoginResponse;
import com.registroServicegrpc.grpc.*;
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

  public RegistroResponse registrarPersona(String tipoPersona, String nombre, String mail,
                                           String telefono, String medioPreferido, String password) {
    return stub.registrarPersona(
            RegistroPersonaRequest.newBuilder()
                    .setTipoPersona(tipoPersona)
                    .setNombreIdentificador(nombre)
                    .setMailDonante(mail)
                    .setTelefonoDonante(telefono)
                    .setMedioPreferido(medioPreferido)
                    .setPassword(BCrypt.hashpw(password, BCrypt.gensalt()))
                    .build());
  }

  public RegistroResponse registrarEntidad(String razonSocial, String direccion, String mail,
                                           String telefono, String password) {
    return stub.registrarEntidad(
            RegistroBeneficiariaRequest.newBuilder()
                    .setRazonSocial(razonSocial)
                    .setDireccion(direccion)
                    .setMailContacto(mail)
                    .setTelefonoBeneficiario(telefono)
                    .setPassword(BCrypt.hashpw(password, BCrypt.gensalt()))
                    .build());
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
