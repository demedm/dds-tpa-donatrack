package ar.edu.utn.frba.dds.logistica.AccionesRutas;


import ar.edu.utn.frba.dds.logistica.AccionesSobreRutas;
import ar.edu.utn.frba.dds.modelos.Ruta;

// Siempre se logean las rutas para mantener registro
public class LoggearRuta implements AccionesSobreRutas {
  // private final Logger log = LoggerFactory.getLogger(LoggearRuta.class);

  @Override
  public void actualizarRuta(Ruta ruta, boolean asignada) {
    // asignada ? log.info("") :
    // log.warning("");
  }
}
