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

        List<PorcentajesRangoedad> comunidades =
                session.createQuery("FROM PorcentajesRangoedad",
                                    PorcentajesRangoedad.class)
                       .list();

        for (PorcentajesRangoedad comunidad : comunidades) {

            String nombre = comunidad.getNombreComunidad();
            int totalHabitantes = comunidad.getTotalHabitantes();

            List<RangoHilos> listaRangos = new ArrayList<>();

            //SOLO LOS QUE PUEDEN VOTAR
            listaRangos.add(new RangoHilos("18_25",calcularHilosPorRango(totalHabitantes,comunidad.getRango1825())));

            listaRangos.add(new RangoHilos("26_40",calcularHilosPorRango(totalHabitantes,comunidad.getRango2640())));

            listaRangos.add(new RangoHilos("41_65",calcularHilosPorRango(totalHabitantes,comunidad.getRango4165())));

            listaRangos.add(new RangoHilos("MAS_66",calcularHilosPorRango(totalHabitantes,comunidad.getRangoMas66())));

            // MINIMO 5 HILOS SI HABITANTES<500000
            if (totalHabitantes < 500000) {
                for (int i = 0; i < listaRangos.size(); i++) {

                    RangoHilos rango = listaRangos.get(i);

                    if (rango.getNumeroHilos() < 5) {
                        listaRangos.set(i,
                                new RangoHilos(rango.getRango(), 5));
                    }
                }
            }

            resultado.add(new ComunidadHilos(nombre, listaRangos));
        }

        session.close();
        return resultado;
    }

    private int calcularHilosPorRango(int totalHabitantes,
                                      int porcentajeRango) {

        int habitantesRango =
                (totalHabitantes * porcentajeRango) / 100;

        return (int) Math.ceil(habitantesRango / 100000.0);
        // REDONDEAR HACIA ARRIBA POR QUE SI NO DA ENTEROS
        // EJEMPLO :(3.1) = 4
    }
}
