package controlador;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import vista.Vista;

public class Controlador {

    private Vista vista;

    // Comunidad seleccionada actualmente
    private String comunidadActual;

    // Resultados simulados
    private Map<String, Integer> resultados;

    public Controlador(Vista vista) {
        this.vista = vista;
        this.resultados = new HashMap<>();

        inicializar();
    }

    // ---------------------------------------
    // Inicialización
    // ---------------------------------------
    private void inicializar() {

        // Botón volver al mapa
        // (lo creamos en la vista, así que lo buscamos por texto)
        // Alternativa mejor: hacer btnVolver público en la vista
        // Aquí asumimos que el botón ya llama a mostrarPanel

        // Botón simular votaciones
        // (no es público, así que lo simulamos desde aquí)
        // 👉 Recomendación: hacerlo público si quieres más control
    }

    // ---------------------------------------
    // Llamado desde la Vista al pulsar comunidad
    // ---------------------------------------
    public void seleccionarComunidad(String nombreComunidad) {
        this.comunidadActual = nombreComunidad;

        vista.lblNombreCiudad.setText(nombreComunidad);

        // Limpiar resultados anteriores
        limpiarResultados();
    }

    // ---------------------------------------
    // Simular votaciones
    // ---------------------------------------
    public void simularVotaciones() {
        if (comunidadActual == null) return;

        Random r = new Random();

        resultados.put("X", r.nextInt(100));
        resultados.put("Y", r.nextInt(100));
        resultados.put("W", r.nextInt(100));
        resultados.put("Z", r.nextInt(100));

        actualizarBarras();
    }

    // ---------------------------------------
    // Actualizar barras de la vista
    // ---------------------------------------
    private void actualizarBarras() {
        // Este método está preparado para cuando
        // hagas las barras dinámicas (height variable)
        // De momento es conceptual
        // Ejemplo futuro:
        //
        // vista.actualizarBarra("X", resultados.get("X"));
        // vista.actualizarBarra("Y", resultados.get("Y"));
    }

    // ---------------------------------------
    // Limpiar resultados
    // ---------------------------------------
    private void limpiarResultados() {
        resultados.clear();
    }
}