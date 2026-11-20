package ar.edu.utn.frba.dds.vistas;

import ar.edu.utn.frba.dds.dto.SegmentadaPanelDTO;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Filtros {

  public static String valor(Context ctx, String nombre){

    String valor = ctx.queryParam(nombre);
    return valor == null || valor.isBlank() ? null : valor;

  }

  public static List<Map<String, Object>> opciones(List<SegmentadaPanelDTO> todas, Function<SegmentadaPanelDTO, String> campo, String elegido){

    return todas.stream()
        .map(campo)
        .distinct()
        .sorted()
        .map(valor -> Map.<String, Object>of(
            "valor", valor,
            "texto", Formato.texto(valor),
            "elegida", valor.equals(elegido)))
        .toList();

  }

}
