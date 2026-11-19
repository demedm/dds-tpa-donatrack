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

    public List<String> getMailsList() {
        return mails;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setId(Long id) { this.id = id; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setMails(List<String> mails) { this.mails = mails; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
