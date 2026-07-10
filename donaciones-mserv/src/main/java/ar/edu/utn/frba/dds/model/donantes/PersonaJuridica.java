package ar.edu.utn.frba.dds.model.donantes;

import ar.edu.utn.frba.dds.model.medioscontacto.MedioContacto;

import java.util.ArrayList;
import java.util.List;

public class PersonaJuridica extends Persona {
  private String rubro;
  private TipoEntidadJuridica tipo;
  private List<MedioContacto> listaContactos = new ArrayList<>();

  public PersonaJuridica() {}

  public void setTipo(TipoEntidadJuridica tipo) {
    this.tipo = tipo;
  }

  public void setRubro(String rubro) {
    this.rubro = rubro;
  }

  public void setListaContactos(List<MedioContacto> contacto) {
    this.listaContactos.addAll(contacto);
  }

  public TipoEntidadJuridica getTipo() {
    return this.tipo;
  }

  public String getRubro() {
    return this.rubro;
  }

  @Override
  public void actualizarInfo(Persona nuevosDatos) {
    super.actualizarInfo(nuevosDatos);
    PersonaJuridica nuevaJuridica = (PersonaJuridica) nuevosDatos;
    this.rubro = nuevaJuridica.getRubro();
    this.tipo = nuevaJuridica.getTipo();
  }

  //Gubernamental, ONG, Empresa, Institución
  private TipoEntidadJuridica getTipoEntidad(String nombreEntidad) {
    if (nombreEntidad.contains("Fundación") || nombreEntidad.contains("Asociación Civil")) {
      return TipoEntidadJuridica.ONG;
    }
    if (nombreEntidad.contains("S.A.")
        || nombreEntidad.contains("S.R.L.")
        || nombreEntidad.contains("S.A.S.")) {
      return TipoEntidadJuridica.EMPRESA;
    }
    if (nombreEntidad.contains("Cooperativa")) {
      return TipoEntidadJuridica.INSTITUCION;
    }
    return TipoEntidadJuridica.GUBERNAMENTAL;
  }

}
