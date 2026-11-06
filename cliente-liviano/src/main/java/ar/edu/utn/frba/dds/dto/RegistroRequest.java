package ar.edu.utn.frba.dds.dto;

public class RegistroRequest {
  // Cuenta
  private String nombre;
  private String email;
  private String password;
  private String rol;

  // Donante
  private String tipoPersona;
  private String nombreIdentificador;
  private String mailDonante;
  private String telefonoDonante;
  private String medioPreferido;

  // Entidad beneficiaria
  private String razonSocial;
  private String direccion;
  private String mailContacto;
  private String telefonoBeneficiario;

  public RegistroRequest() {}

  public RegistroRequest(String nombre, String mail, String password, String rol) {
    this.nombre = nombre;
    this.email = mail;
    this.password = password;
    this.rol = rol;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRol() {
    return rol;
  }

  public void setRol(String rol) {
    this.rol = rol;
  }

  public String getNombreIdentificador() {
    return nombreIdentificador;
  }

  public void setNombreIdentificador(String nombreIdentificador) {
    this.nombreIdentificador = nombreIdentificador;
  }

  public String getTipoPersona() {
    return tipoPersona;
  }

  public void setTipoPersona(String tipoPersona) {
    this.tipoPersona = tipoPersona;
  }

  public String getMailDonante() {
    return mailDonante;
  }

  public void setMailDonante(String mailDonante) {
    this.mailDonante = mailDonante;
  }

  public String getTelefonoDonante() {
    return telefonoDonante;
  }

  public void setTelefonoDonante(String telefonoDonante) {
    this.telefonoDonante = telefonoDonante;
  }

  public String getMedioPreferido() {
    return medioPreferido;
  }

  public void setMedioPreferido(String medioPreferido) {
    this.medioPreferido = medioPreferido;
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public void setRazonSocial(String razonSocial) {
    this.razonSocial = razonSocial;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getMailContacto() {
    return mailContacto;
  }

  public void setMailContacto(String mailContacto) {
    this.mailContacto = mailContacto;
  }

  public String getTelefonoBeneficiario() {
    return telefonoBeneficiario;
  }

  public void setTelefonoBeneficiario(String telefonoBeneficiario) {
    this.telefonoBeneficiario = telefonoBeneficiario;
  }
}
