package ar.edu.utn.frba.dds.controllers.autenticacion;

public class LoginResponse {
  private String token;

  public LoginResponse(String token) {
    this.token = token;
  }

  public LoginResponse() {}

  public String getToken() {
    return token;
  }
}
