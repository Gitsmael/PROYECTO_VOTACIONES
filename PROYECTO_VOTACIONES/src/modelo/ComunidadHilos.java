package modelo;

import java.util.List;

public class ComunidadHilos {

    private String nombreComunidad;
    private List<RangoHilos> rangos;

    public ComunidadHilos(String nombreComunidad, List<RangoHilos> rangos) {
        this.nombreComunidad = nombreComunidad;
        this.rangos = rangos;
    }

    public String getNombreComunidad() {
        return nombreComunidad;
    }

    public List<RangoHilos> getRangos() {
        return rangos;
    }
}
