package ar.edu.utn.frba.dds.model;


public class Camion {
  private String patente;
  private int capacidadVolumen;
  private int altura;
  private int capacidadCarga;
  private EstadoCamion estado;
  private Ruta rutaActual = null;
  private Ubicacion ubicacionActual;

  public void setEstado(EstadoCamion estado) {
    this.estado = estado;
  }

  public EstadoCamion getEstado()
  {
    return estado;
  }

  public Camion(String patente, int capacidadCarga, int capacidadVolumen,
                int altura) {
    this.altura = altura;
    this.capacidadCarga = capacidadCarga;
    this.capacidadVolumen = capacidadVolumen;
    this.estado = EstadoCamion.DISPONIBLE;
    this.patente = patente;
  }

  public String getPatente()
  {
    return this.patente;
  }

  public int getAltura() {
    return this.altura;
  }

  public int getCapacidadVolumen() {
    return this.capacidadVolumen;
  }

  public int getCapacidadCarga() {
    return this.capacidadCarga;
  }

  public Ruta getRutaActual() {
    return this.rutaActual;
  }

  public void asignarRuta(Ruta ruta) {
    this.rutaActual = ruta;
    estado = EstadoCamion.RUTA_ASIGNADA;
  }
/*
  public boolean asignarRuta(Ruta ruta) {
    if(estado == EstadoCamion.DISPONIBLE) {
      this.rutaActual = ruta;
      estado = EstadoCamion.RUTA_ASIGNADA;
      return true;
    }
    return false;
  }

  public void iniciarRuta() {
    estado = EstadoCamion.REALIZANDO_ENTREGAS;
    rutaActual.iniciarRuta();
  }

  public void visitarDestino(String direccion) {
    rutaActual.visitarParada(direccion);
  }

  public void regresarADeposito() {
    rutaActual.finalizarRuta();
    estado = EstadoCamion.DISPONIBLE;
  }

  public Ubicacion getUbicacionActual() {
    return ubicacionActual;
  }

  public void actualizarUbicacion(Double latitud, Double longitud) {
    // Solo permitimos actualizar si el camión está en ruta
    if (this.estado == EstadoCamion.REALIZANDO_ENTREGAS) {
      this.ubicacionActual = new Ubicacion(latitud, longitud, LocalDateTime.now());
    }
  }
*/
}