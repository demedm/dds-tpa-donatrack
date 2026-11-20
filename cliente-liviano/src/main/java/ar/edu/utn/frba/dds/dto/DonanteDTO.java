package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DonanteDTO {

  private Long id;
  private String nombreIdentificador;

  public Long getId() {
    return id;
  }

  public String getNombreIdentificador() {
    return nombreIdentificador;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setNombreIdentificador(String nombreIdentificador) {
    this.nombreIdentificador = nombreIdentificador;
  }

}
