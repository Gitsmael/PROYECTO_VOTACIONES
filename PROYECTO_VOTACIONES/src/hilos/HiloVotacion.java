package hilos;

import java.util.concurrent.ThreadLocalRandom;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class HiloVotacion implements Runnable {

    private SessionFactory sessionFactory;
    private String comunidad;
    private String rangoEdad;

    public HiloVotacion(SessionFactory sessionFactory,
                        String comunidad,
                        String rangoEdad) {
        this.sessionFactory = sessionFactory;
        this.comunidad = comunidad;
        this.rangoEdad = rangoEdad;
    }

    @Override
    public void run() {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        try {

            int numero = ThreadLocalRandom.current().nextInt(1, 101);

            String partido = calcularPartido(numero);

            Query<?> query = session.createQuery(
                "update VotosComunidadPartido v " +
                "set v.totalVotos = v.totalVotos + 1, " +
                actualizarCampoRango() +
                " where v.id.nombreComunidad = :comunidad " +
                "and v.id.partidoPolitico = :partido"
            );

            query.setParameter("comunidad", comunidad);
            query.setParameter("partido", partido);

            query.executeUpdate();

            tx.commit();

            Thread.sleep(100);

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private String calcularPartido(int numero) {

        switch (rangoEdad) {

            case "18_25":
                if (numero <= 30) return "X";
                else if (numero <= 50) return "Y";
                else if (numero <= 70) return "W";
                else return "Z";

            case "26_40":
                if (numero <= 20) return "X";
                else if (numero <= 55) return "Y";
                else if (numero <= 85) return "W";
                else return "Z";

            case "41_65":
                if (numero <= 10) return "X";
                else if (numero <= 55) return "Y";
                else if (numero <= 90) return "W";
                else return "Z";

            case "MAS_66":
                if (numero <= 25) return "X";
                else if (numero <= 60) return "Y";
                else if (numero <= 95) return "W";
                else return "Z";
        }

        return "X";
    }

    private String actualizarCampoRango() {

        switch (rangoEdad) {

            case "18_25":
                return "v.votos1825 = v.votos1825 + 1";

            case "26_40":
                return "v.votos2640 = v.votos2640 + 1";

            case "41_65":
                return "v.votos4165 = v.votos4165 + 1";

            case "MAS_66":
                return "v.votosMas66 = v.votosMas66 + 1";
        }

        return "";
    }
}
