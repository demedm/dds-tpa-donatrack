package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.usuarios.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.model.usuarios.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepositorio {
  public static UsuarioRepositorio Instance = new UsuarioRepositorio();
  public List<Usuario> usuarios = new ArrayList<>(); //choferes y admins
  public List<EntidadBeneficiaria> entidades = new ArrayList<>();

  public Usuario findUsuarioById(String id) {
    return usuarios.stream().filter(usuario -> usuario.getId().equals(id))
        .findFirst().orElse(null);
  }

  public EntidadBeneficiaria findEntidadById(String id) {
    return entidades.stream().filter(entidad -> entidad.getId().equals(id))
        .findFirst().orElse(null);
  }

}
