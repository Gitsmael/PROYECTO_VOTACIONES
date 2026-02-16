package controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import hilos.HiloVotacion;
import modelo.ComunidadHilos;
import modelo.RangoHilos;
import servicios.ServicioCalculoHilos;
import servicios.InicializarPartidos;
import vista.Vista;

public class Controlador {

    private Vista vista;
    private String comunidadActual;

    private SessionFactory sessionFactory;

    public Controlador(Vista vista) {

        this.vista = vista;
        sessionFactory = new Configuration().configure().buildSessionFactory();

        inicializarSistema();
    }

    // ---------------------------------------
    // Inicialización general del sistema
    // ---------------------------------------
    private void inicializarSistema() {

        // 1️⃣ Crear filas iniciales de partidos
    	InicializarPartidos init = new InicializarPartidos(sessionFactory);

        init.inicializarPartidos();
    }

    // ---------------------------------------
    // Selección de comunidad desde la vista
    // ---------------------------------------
    public void seleccionarComunidad(String nombreComunidad) {
        this.comunidadActual = nombreComunidad;
        vista.lblNombreCiudad.setText(nombreComunidad);
    }

    // ---------------------------------------
    // Simulación REAL
    // ---------------------------------------
    public void simularVotaciones() {

        ServicioCalculoHilos calculo =
                new ServicioCalculoHilos(sessionFactory);

        List<ComunidadHilos> lista = calculo.calcularHilos();

        ExecutorService executor =
                Executors.newFixedThreadPool(10);

        for (ComunidadHilos comunidad : lista) {

            for (RangoHilos rango : comunidad.getRangos()) {

                for (int i = 0; i < rango.getNumeroHilos(); i++) {

                    executor.execute(new HiloVotacion(
                            sessionFactory,
                            comunidad.getNombreComunidad(),
                            rango.getRango()));
                }
            }
        }

        executor.shutdown();

        try {
            executor.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        cargarResultadosNacionales();
    }
    
    
    private void cargarResultadosNacionales() {

        var session = sessionFactory.openSession();

        var query = session.createQuery(
                "select v.id.partidoPolitico, sum(v.totalVotos) " +
                "from VotosComunidadPartido v " +
                "group by v.id.partidoPolitico",
                Object[].class
        );

        List<Object[]> datos = query.list();

        Map<String, Integer> resultados = new HashMap<>();

        for (Object[] fila : datos) {
            String partido = (String) fila[0];
            Long votos = (Long) fila[1];
            resultados.put(partido, votos.intValue());
        }

        session.close();

        System.out.println("RESULTADOS NACIONALES");

        resultados.forEach((p, v) ->
                System.out.println(p + " -> " + v));
    }



    // ---------------------------------------
    // Cargar resultados reales desde BD
    // ---------------------------------------
    private void cargarResultadosPorComunidad(String comunidad) {

        var session = sessionFactory.openSession();

        var query = session.createQuery(
                "select v.id.partidoPolitico, sum(v.totalVotos) " +
                "from VotosComunidadPartido v " +
                "where v.id.nombreComunidad = :comunidad " +
                "group by v.id.partidoPolitico",
                Object[].class
        );

        query.setParameter("comunidad", comunidad);

        List<Object[]> datos = query.list();

        Map<String, Integer> resultados = new HashMap<>();

        for (Object[] fila : datos) {
            String partido = (String) fila[0];
            Long votos = (Long) fila[1];
            resultados.put(partido, votos.intValue());
        }

        session.close();

        actualizarVista(resultados);
    }


    // ---------------------------------------
    // Actualizar interfaz
    // ---------------------------------------
    private void actualizarVista(Map<String, Integer> resultados) {

        // Ejemplo conceptual

        System.out.println("Resultados en " + comunidadActual);

        resultados.forEach((p, v) ->
                System.out.println(p + " -> " + v));

        // vista.actualizarBarra("X", resultados.get("X"));
        // vista.actualizarBarra("Y", resultados.get("Y"));
        // vista.actualizarBarra("W", resultados.get("W"));
        // vista.actualizarBarra("Z", resultados.get("Z"));
    }

    // ---------------------------------------
    // Cerrar recursos al finalizar
    // ---------------------------------------
    public void cerrar() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
