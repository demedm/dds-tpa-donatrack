package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.dto.CambioEstadoDTO;
import ar.edu.utn.frba.dds.model.Donaciones.Donacion;
import ar.edu.utn.frba.dds.model.Donaciones.DonacionSegmentada;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class DonacionesRepository {
    public static DonacionesRepository Instance = new DonacionesRepository();
    public List<Donacion> donaciones = new ArrayList<>();
    public List<DonacionSegmentada> donacionesAsignadas = new ArrayList<>();

    public void agregarDonacion(Donacion donacion){
        donaciones.add(donacion);
    }

    public void cambiarEstadoDonacion(Context ctx) {
        int idDonacion = Integer.parseInt(ctx.pathParam("{id}"));
        CambioEstadoDTO cambioEstado = ctx.bodyAsClass(CambioEstadoDTO.class);

        DonacionSegmentada donacion = obtenerDonacionAsignadaPorId(idDonacion);
        if(cambioEstado.getMotivoFallaEntrega() == null) {
            donacion.cambiarEstado(cambioEstado.getNuevoEstado());
        } else {
            donacion.fallarEntrega(cambioEstado.getMotivoFallaEntrega());
        }

        ctx.status(200);
    }

    // public  ResultadoBusqueda buscarProducto(String subcategoria, int cantidad){
    //     List<Bien> bienesFiltrrados = donaciones.stream()
    //         .flatMap(d->d.getBienes().stream())
    //         .filter(b->b.getSubcategoria().equals(subcategoria))
    //         .collect(Collectors.toList());

    //     List<Bien> bienesAsignados = new ArrayList<>();
    //     int restante = cantidad;
    //     for (Bien bien : bienesFiltrrados) {
    //     if (restante == 0) break;
    //     int aRestar = Math.min(bien.getCantidad(), restante);
    //     bien.copia(aRestar, EstadoDonacion.ASIGNACION_REALIZADA);
    //     bien.setCantidad(bien.getCantidad() - aRestar);
    //     restante -= aRestar;
    //     }
    //     return new ResultadoBusqueda(restante,bienesAsignados);
    // }

    public DonacionSegmentada obtenerDonacionAsignadaPorId(int id){
        return this.donacionesAsignadas.stream()
            .filter(d->d.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public Donacion obtenerPorId(String id){
        return this.donaciones.stream()
        .filter(d->d.getId().equals(id))
        .findFirst()
        .orElse(null);
    }

  //Obtener todas las donaciones  

    public List<Donacion> obtenerTodas(){
        return new ArrayList<>(this.donaciones);
    }

    public void eliminar(String id){
        this.donaciones.removeIf(d->d.getId().equals(id));
    }

    public void actualizar(Donacion donacion){
        Donacion existente = obtenerPorId(donacion.getId());

        existente.setDonacionesSegmentadas(donacion.getDonaciones());    }
    }

