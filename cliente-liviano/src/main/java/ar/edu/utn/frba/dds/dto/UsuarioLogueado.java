package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.session.Rol;

public class UsuarioLogueado {
  private Long id;
  private Rol rol;

  public UsuarioLogueado(Long id, String rol) {
    this.id = id;
    this.rol = Rol.strTo(rol.toUpperCase());
  }

  public void setRol(Rol rol) {
    this.rol = rol;
  }

  public Long getId() {
    return id;
  }
}
