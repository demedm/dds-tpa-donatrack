package ar.edu.utn.frba.dds.dto;

import ar.edu.utn.frba.dds.model.donantes.Persona;
import ar.edu.utn.frba.dds.model.donantes.PersonaJuridica;
import ar.edu.utn.frba.dds.model.donantes.TipoEntidadJuridica;

public class PersonaJuridicaDTO extends PersonaDTO {
  private String rubro;
  private String tipoEntidad;

  public String getRubro() {
    return rubro;
  }

  public void setRubro(String rubro) {
    this.rubro = rubro;
  }

  public String getTipoEntidad() {
    return tipoEntidad;
  }

  public void setTipoEntidad(String tipoEntidad) {
    this.tipoEntidad = tipoEntidad;
  }

  @Override
  public Persona PersonaDto() {
    PersonaJuridica p = new PersonaJuridica();
    completarDatosComunes(p);
    p.setRubro(rubro);
    p.setTipo(TipoEntidadJuridica.valueOf(tipoEntidad));
    return p;
  }

  @Override
  public void aplicarCambios(Persona persona) {
    aplicarCambiosComunes(persona);
    PersonaJuridica personaJuridica = (PersonaJuridica) persona;
    if (rubro != null) {
      personaJuridica.setRubro(rubro);
    }
    if (tipoEntidad != null) {
      personaJuridica.setTipo(TipoEntidadJuridica.valueOf(tipoEntidad));
    }
  }
}
