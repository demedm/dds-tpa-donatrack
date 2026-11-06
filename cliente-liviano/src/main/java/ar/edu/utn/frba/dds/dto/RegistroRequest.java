package ar.edu.utn.frba.dds.dto;

public class RegistroRequest {
  private String nombre;
  private String email;
  private String password;
  private String rol;

  public RegistroRequest(String nombre, String email, String password, String rol) {
    this.email = email;
    this.rol = rol;
    this.nombre = nombre;
    this.password = password;
  }

  public String getNombre() {
    return nombre;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getRol() {
    return rol;
  }
}
