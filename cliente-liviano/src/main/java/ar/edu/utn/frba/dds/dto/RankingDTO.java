package ar.edu.utn.frba.dds.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RankingDTO {
    private Long donacionSegmentadaId;
    private boolean huboCoincidencias;
    private List<EntidadDTO> propuestas = List.of();
    private Map<String, List<EntidadDTO>> porAlgoritmo = new LinkedHashMap<>();

    public Long getDonacionSegmentadaId() {
        return donacionSegmentadaId;
    }

    public boolean isHuboCoincidencias() {
        return huboCoincidencias;
    }

    public List<EntidadDTO> getPropuestas() {
        return propuestas;
    }

    public Map<String, List<EntidadDTO>> getPorAlgoritmo() {
        return porAlgoritmo;
    }

    public void setDonacionSegmentadaId(Long donacionSegmentadaId) {
        this.donacionSegmentadaId = donacionSegmentadaId;
    }

    public void setHuboCoincidencias(boolean huboCoincidencias) {
        this.huboCoincidencias = huboCoincidencias;
    }

    public void setPropuestas(List<EntidadDTO> propuestas) {
        this.propuestas = propuestas;
    }

    public void setPorAlgoritmo(Map<String, List<EntidadDTO>> porAlgoritmo) {
        this.porAlgoritmo = porAlgoritmo;
    }
}
