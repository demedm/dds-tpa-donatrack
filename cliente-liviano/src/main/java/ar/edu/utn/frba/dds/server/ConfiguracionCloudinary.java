package ar.edu.utn.frba.dds.server;

// Datos de la cuenta de Cloudinary (fotos de necesidades y de entregas), leídos del .env.
public class ConfiguracionCloudinary {

  private static String cloudName;
  private static String uploadPreset;

  private ConfiguracionCloudinary() {
  }

  public static void aplicar() {
    cloudName = leer("CLOUDINARY_CLOUD_NAME");
    uploadPreset = leer("CLOUDINARY_UPLOAD_PRESET");
  }

  public static String cloudName() {
    return cloudName;
  }

  public static String uploadPreset() {
    return uploadPreset;
  }

  private static String leer(String variable) {
    String valor = System.getenv(variable);
    if (valor == null || valor.isBlank()) {
      throw new IllegalStateException("No se configuró " + variable + " (ver .env.example en la raiz del repo)");
    }
    return valor;
  }
}