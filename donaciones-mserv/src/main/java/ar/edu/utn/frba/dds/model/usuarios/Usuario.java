package ar.edu.utn.frba.dds.model.usuarios;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Usuario {
  @Id
  private Long dominioId;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String hashPassword;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Rol rol;

  public Usuario(Rol rol, String email, String password) {
    this.rol = rol;
    this.email = email;
    this.hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
  }

  public Usuario() {}

  public String getEmail() {
    return email;
  }

  public String getHashPassword() {
    return hashPassword;
  }

  public Rol getRol() {
    return rol;
  }

  public Long getDominioId() {
    return dominioId;
  }

  public void setDominioId(Long dominioId) {
    this.dominioId = dominioId;
  }

}
