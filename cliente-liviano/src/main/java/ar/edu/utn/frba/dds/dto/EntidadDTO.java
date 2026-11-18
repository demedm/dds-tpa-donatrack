package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntidadDTO {


    private Long id;
    private String razonSocial;
    private String tipo;
    private String direccion;
    private List<String> mails = List.of();
    private String telefono;

    public Long getId() {
        return id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<String> getMails() {
        return mails;
    }

    public String getTelefono() {
        return telefono;
    }
}
