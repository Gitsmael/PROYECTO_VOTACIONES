package modelo;

public class RangoHilos {

    private String rango;
    private int numeroHilos;

    public RangoHilos(String rango, int numeroHilos) {
        this.rango = rango;
        this.numeroHilos = numeroHilos;
    }

    public String getRango() {
        return rango;
    }

    public int getNumeroHilos() {
        return numeroHilos;
    }

    @Override
    public String toString() {
        return "Rango: " + rango + " -> " + numeroHilos + " hilos";
    }
}
