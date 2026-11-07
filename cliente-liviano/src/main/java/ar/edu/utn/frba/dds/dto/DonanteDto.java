package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DonanteDto {
  private Long id;
  private String nombreIdentificador;

  public Long getId() { return id; }
  public String getNombreIdentificador() { return nombreIdentificador; }
}
