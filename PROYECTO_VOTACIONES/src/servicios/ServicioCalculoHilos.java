package servicios;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import modelo.ComunidadHilos;
import modelo.RangoHilos;
import persistencias.PorcentajesRangoedad;

public class ServicioCalculoHilos {
    private SessionFactory sessionFactory;

    public ServicioCalculoHilos(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<ComunidadHilos> calcularHilos() {
        List<ComunidadHilos> resultado = new ArrayList<>();
        Session session = sessionFactory.openSession();
        
        System.out.println("DEBUG: Iniciando consulta HQL para obtener comunidades...");
        
        try {
            List<PorcentajesRangoedad> comunidades =
                    session.createQuery("FROM PorcentajesRangoedad",
                                        PorcentajesRangoedad.class)
                           .list();
            
            System.out.println("DEBUG: Se han encontrado " + comunidades.size() + " comunidades en la BD.");

            for (PorcentajesRangoedad comunidad : comunidades) {
                String nombre = comunidad.getNombreComunidad();
                int totalHabitantes = comunidad.getTotalHabitantes();
                List<RangoHilos> listaRangos = new ArrayList<>();

                // CÁLCULO DE HILOS POR RANGO
                int h1 = calcularHilosPorRango(totalHabitantes, comunidad.getRango1825());
                int h2 = calcularHilosPorRango(totalHabitantes, comunidad.getRango2640());
                int h3 = calcularHilosPorRango(totalHabitantes, comunidad.getRango4165());
                int h4 = calcularHilosPorRango(totalHabitantes, comunidad.getRangoMas66());

                listaRangos.add(new RangoHilos("18_25", h1));
                listaRangos.add(new RangoHilos("26_40", h2));
                listaRangos.add(new RangoHilos("41_65", h3));
                listaRangos.add(new RangoHilos("MAS_66", h4));

                // APLICAR MÍNIMO DE 5 HILOS SI HABITANTES < 500.000
                if (totalHabitantes < 500000) {
                    for (int i = 0; i < listaRangos.size(); i++) {
                        RangoHilos rango = listaRangos.get(i);
                        if (rango.getNumeroHilos() < 5) {
                            listaRangos.set(i, new RangoHilos(rango.getRango(), 5));
                        }
                    }
                }

                int totalHilosComunidad = listaRangos.stream().mapToInt(RangoHilos::getNumeroHilos).sum();
                System.out.println("DEBUG: Comunidad: " + nombre + " | Habitantes: " + totalHabitantes + " | Total Hilos: " + totalHilosComunidad);
                
                resultado.add(new ComunidadHilos(nombre, listaRangos));
            }
        } catch (Exception e) {
            System.err.println("ERROR en ServicioCalculoHilos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return resultado;
    }

    private int calcularHilosPorRango(int totalHabitantes, int porcentajeRango) {
        // Calculamos habitantes del rango usando double para no perder precisión
        double habitantesRango = (double) totalHabitantes * ((double) porcentajeRango / 100.0);
        // Redondeamos hacia arriba: 1 hilo por cada 100.000 habitantes (mínimo 1 si hay habitantes)
        int hilos = (int) Math.ceil(habitantesRango / 100000.0);
        return hilos;
    }
}
