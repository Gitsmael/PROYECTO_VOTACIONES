package controlador;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import persistencias.PorcentajesRangoedad;
import persistencias.VotosComunidadPartido;
import persistencias.VotosComunidadPartidoId;

public class PruebaHibernate {

    private SessionFactory sessionFactory;

    public PruebaHibernate(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // 1️⃣ Leer todas las comunidades
    public void leerComunidades() {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        List<PorcentajesRangoedad> lista =
                session.createQuery("FROM PorcentajesRangoedad", PorcentajesRangoedad.class)
                       .list();

        for (PorcentajesRangoedad p : lista) {

            System.out.println("Comunidad: " + p.getNombreComunidad());
            System.out.println("18-25: " + p.getRango1825() + "%");
            System.out.println("26-40: " + p.getRango2640() + "%");
            System.out.println("41-65: " + p.getRango4165() + "%");
            System.out.println("+66: " + p.getRangoMas66() + "%");
            System.out.println("Total habitantes: " + p.getTotalHabitantes());
            System.out.println("-----------------------------------");
        }

        tx.commit();
        session.close();
    }

    // 2️⃣ Insertar fila en VOTOS_COMUNIDAD_PARTIDO
    public void insertarVotosIniciales(String comunidad, String partido) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        VotosComunidadPartidoId id =
                new VotosComunidadPartidoId(comunidad, partido);

        VotosComunidadPartido voto = new VotosComunidadPartido();
        voto.setId(id);

        voto.setVotos19(0);
        voto.setVotos1017(0);
        voto.setVotos1825(0);
        voto.setVotos2640(0);
        voto.setVotos4165(0);
        voto.setVotosMas66(0);
        voto.setTotalVotos(0);

        session.save(voto);

        tx.commit();
        session.close();

        System.out.println("Insert correcto");
    }


    // 3️⃣ Recuperar un voto por clave compuesta
    public void recuperarVoto(String comunidad, String partido) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        VotosComunidadPartidoId id =
                new VotosComunidadPartidoId(comunidad, partido);

        VotosComunidadPartido voto =
                session.get(VotosComunidadPartido.class, id);

        if (voto != null) {
            System.out.println("Encontrado:");
            System.out.println("Total votos: " + voto.getTotalVotos());
        } else {
            System.out.println("No encontrado");
        }

        tx.commit();
        session.close();
    }


    // 4️⃣ Actualizar votos con HQL
    public void actualizarVoto(String comunidad, String partido) {

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query<?> query = session.createQuery(
        	    "update VotosComunidadPartido v " +
        	    "set v.votos1825 = v.votos1825 + 1, " +
        	    "    v.totalVotos = v.totalVotos + 1 " +
        	    "where v.id.nombreComunidad = :comunidad " +
        	    "and v.id.partidoPolitico = :partido"
        	);

        	query.setParameter("comunidad", comunidad);
        	query.setParameter("partido", partido);
        	query.executeUpdate();


        tx.commit();
        session.close();

    }


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SessionFactory sessionFactory=null;
		Configuration configuration = new Configuration();
		configuration.configure();
		sessionFactory = configuration.buildSessionFactory();
		
		PruebaHibernate prueba = new PruebaHibernate(sessionFactory);


		prueba.leerComunidades();

		prueba.insertarVotosIniciales("Andalucia", "X");

		prueba.recuperarVoto("Andalucia", "X");

		prueba.actualizarVoto("Andalucia", "X");

		prueba.recuperarVoto("Andalucia", "X");
		
		 sessionFactory.close(); 

	}

}
