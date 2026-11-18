package ar.edu.utn.frba.dds.vistas;

import java.math.BigDecimal;

public class Formato {

    public static String texto(String valor){
        if(valor == null || valor.isBlank()) return "—";
        String t = valor.replace('_',' ').toLowerCase();
        return Character.toUpperCase(t.charAt(0)) + t.substring(1);
    }

    public static String fecha(String iso){
        if(iso == null || iso.isBlank()) return "—";
        String[] p = iso.split("-");
        return p.length == 3 ? p[2] + "/" + p[1] + "/" + p[0] : iso;
    }

    public static String cantidad(BigDecimal cantidad, String unidad){
        String n = cantidad == null ? "?":
        cantidad.stripTrailingZeros().toPlainString();
        return n + " " + texto(unidad).toLowerCase();
    }

    //clase css del badge segun el estado
    public static String badge(String estado){
        if(estado == null) return "badge-pendiente";
        return switch(estado) {
            case "En depósito" -> "badge-pendiente";
            case "Vencida", "Entrega fallida" -> "badge-vencida";
            default -> "badge-asignada";
        };
    }
}

