package ar.edu.utn.frba.dds;

public class Mail implements MedioContacto {

  private String info;

  public Mail(String correoElectronico) {
    this.info = correoElectronico;
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
