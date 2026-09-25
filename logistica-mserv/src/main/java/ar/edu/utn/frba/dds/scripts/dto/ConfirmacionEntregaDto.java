package ar.edu.utn.frba.dds.scripts.dto;

import java.time.LocalDateTime;

public class ConfirmacionEntregaDto {
  private Long camionId;
  private int anio;
  private int mes;
  private int dia;
  private int hora;
  private int minutos;

  public ConfirmacionEntregaDto() {}

  public ConfirmacionEntregaDto(Long camionId, int anio, int mes, int dia, int hora, int minutos) {
    this.camionId = camionId;
    this.anio = anio;
    this.mes = mes;
    this.dia = dia;
    this.hora = hora;
    this.minutos = minutos;
  }

  public Long getCamionId() {
    return camionId;
  }

  public void setCamionId(Long camionId) {
    this.camionId = camionId;
  }

  public int getAnio() {
    return anio;
  }

  public void setAnio(int anio) {
    this.anio = anio;
  }

  public int getMes() {
    return mes;
  }

  public void setMes(int mes) {
    this.mes = mes;
  }

  public int getDia() {
    return dia;
  }

  public void setDia(int dia) {
    this.dia = dia;
  }

  public int getHora() {
    return hora;
  }

  public void setHora(int hora) {
    this.hora = hora;
  }

  public int getMinutos() {
    return minutos;
  }

  public void setMinutos(int minutos) {
    this.minutos = minutos;
  }
}
