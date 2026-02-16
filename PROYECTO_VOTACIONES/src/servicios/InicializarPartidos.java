package servicios;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import persistencias.PorcentajesRangoedad;
import persistencias.VotosComunidadPartido;
import persistencias.VotosComunidadPartidoId;

public class InicializarPartidos {

    private SessionFactory sessionFactory;

    public InicializarPartidos(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void inicializarPartidos() {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try {

            List<PorcentajesRangoedad> comunidades = session.createQuery("FROM PorcentajesRangoedad",PorcentajesRangoedad.class).list();

            String[] partidos = {"X", "Y", "W", "Z"};

            for (PorcentajesRangoedad comunidad : comunidades) {

                for (String partido : partidos) {

                    VotosComunidadPartidoId id =
                            new VotosComunidadPartidoId(
                                    comunidad.getNombreComunidad(),
                                    partido);

                    // Comprobamos si ya existe
                    VotosComunidadPartido existente =
                            session.get(VotosComunidadPartido.class, id);

                    if (existente == null) {

                        VotosComunidadPartido nuevo =
                                new VotosComunidadPartido();

                        nuevo.setId(id);
                        nuevo.setVotos19(0);
                        nuevo.setVotos1017(0);
                        nuevo.setVotos1825(0);
                        nuevo.setVotos2640(0);
                        nuevo.setVotos4165(0);
                        nuevo.setVotosMas66(0);
                        nuevo.setTotalVotos(0);

                        session.save(nuevo);
                    }
                }
            }

            tx.commit();
            System.out.println("Partidos inicializados correctamente.");

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
