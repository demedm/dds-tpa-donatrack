package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.session.Rol;

public class UsuarioLogueado {
  private Long id;
  private Rol rol;

  public UsuarioLogueado(Long id, String rol) {
    this.id = id;
    this.rol = Rol.strTo(rol.toUpperCase());
  }

  public UsuarioLogueado() {}

  public Rol getRol() {
    return this.rol;
  }

  public Long getId() {
    return id;
  }
}
