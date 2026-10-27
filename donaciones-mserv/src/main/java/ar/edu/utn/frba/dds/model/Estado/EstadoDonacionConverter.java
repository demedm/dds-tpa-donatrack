package ar.edu.utn.frba.dds.model.Estado;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EstadoDonacionConverter implements AttributeConverter<EstadoDonacion, String> {

  @Override
  public String convertToDatabaseColumn(EstadoDonacion estado) {
    if (estado == null) {
      return null;
    }
    return estado.getNombre();
  }

  @Override
  public EstadoDonacion convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    return switch (dbData) {
      case "En depósito" -> new EnDeposito();
      case "Asignación realizada" -> new AsignacionRealizada();
      case "Lista para entregar" -> new ListaParaEntregar();
      case "En traslado" -> new EnTraslado();
      case "Entregada" -> new Entregada();
      case "Entrega fallida" -> new EntregaFallida();
      case "Vencida" -> new Vencida();
      default -> throw new IllegalArgumentException("Estado desconocido en la base de datos: " + dbData);
    };
  }
}