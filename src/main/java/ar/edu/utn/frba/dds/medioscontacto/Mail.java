package ar.edu.utn.frba.dds.medioscontacto;

import ar.edu.utn.frba.dds.Notificacion;

public class Mail implements MedioContacto {
  String direccionMail;

  public Mail(String direccionCasilla) {
    if (direccionCasilla.contains("@") && direccionCasilla.contains(".com")) {
      direccionMail = direccionCasilla;
    }
  }

  @Override
  public String getMedioContacto() {
    return direccionMail;
  }

  @Override
  public void contactar(Notificacion notificacion) {
    System.out.println(" === [SIMULADOR SERVICIO EXTERNO: SMTP EMAIL] ===");
    System.out.println("Enviando correo a: " + notificacion.getDestinatario());
    System.out.println("Asunto: ¡Bienvenido a Donatrack!");
    System.out.println("Cuerpo: " + notificacion.getMensaje());
    System.out.println("=================================================");

    notificacion.marcarComoCompletada();
  }

}
