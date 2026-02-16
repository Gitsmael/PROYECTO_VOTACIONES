package controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.io.File;

import javax.swing.SwingUtilities;

import org.hibernate.Session;
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
	private SessionFactory sessionFactory;

	public Controlador(Vista vista) {
		this.vista = vista;
		File configFile = new File("src/hibernate.cfg.xml");
		if (configFile.exists()) {
			sessionFactory = new Configuration().configure(configFile).buildSessionFactory();
		} else {
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
		inicializarSistema();
	}

	private void inicializarSistema() {
		InicializarPartidos init = new InicializarPartidos(sessionFactory);
		init.inicializarPartidos();
	}

	public void seleccionarComunidad(String nombreComunidad) {
		// La vista ahora llama directamente a actualizarGraficoDetallado al entrar
	}

	public void simularVotaciones() {
		new Thread(() -> {
			ServicioCalculoHilos calculo = new ServicioCalculoHilos(sessionFactory);
			List<ComunidadHilos> lista = calculo.calcularHilos();
			ExecutorService executor = Executors.newFixedThreadPool(10);
			for (ComunidadHilos comunidad : lista) {
				for (RangoHilos rango : comunidad.getRangos()) {
					String r = rango.getRango();
					if (r.contains("1 a 9") || r.contains("10 a 17")) {
						continue;
					}
					for (int i = 0; i < rango.getNumeroHilos(); i++) {
						executor.execute(new HiloVotacion(sessionFactory, comunidad.getNombreComunidad(), r));
					}
				}
			}
			executor.shutdown();
			try {
				executor.awaitTermination(10, TimeUnit.MINUTES);
				cargarResultadosNacionales();
				actualizarColoresMapaPorComunidad();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	public void actualizarGraficoDetallado(String comunidad, String rangoSeleccionado) {
		new Thread(() -> {
			try (Session session = sessionFactory.openSession()) {
				String columna;
				switch (rangoSeleccionado) {
					case "18 a 25 años": columna = "votos1825"; break;
					case "24 a 40 años": columna = "votos2640"; break;
					case "41 a 65 años": columna = "votos4165"; break;
					case "más de 66 años": columna = "votosMas66"; break;
					case "Votaciones Globales": columna = "totalVotos"; break;
					default: return;
				}

				String hql;
				if (comunidad.toLowerCase().contains("castilla") && comunidad.toLowerCase().contains("mancha")) {
					hql = "select v.id.partidoPolitico, sum(v." + columna + ") " +
					      "from VotosComunidadPartido v " +
					      "where v.id.nombreComunidad like '%Castilla%Mancha%' " +
					      "group by v.id.partidoPolitico";
				} else {
					hql = "select v.id.partidoPolitico, sum(v." + columna + ") " +
					      "from VotosComunidadPartido v " +
					      "where v.id.nombreComunidad = :comunidad " +
					      "group by v.id.partidoPolitico";
				}

				var query = session.createQuery(hql, Object[].class);
				if (!comunidad.toLowerCase().contains("mancha")) {
					query.setParameter("comunidad", comunidad);
				}
				
				List<Object[]> datos = query.list();
				Map<String, Integer> resultados = new HashMap<>();
				for (Object[] fila : datos) {
					resultados.put((String) fila[0], ((Number) fila[1]).intValue());
				}
				SwingUtilities.invokeLater(() -> vista.actualizarGraficoDetallado(resultados));
			} catch (Exception e) {
				System.err.println("Error al cargar detalles: " + e.getMessage());
			}
		}).start();
	}

	private void actualizarColoresMapaPorComunidad() {
		try (Session session = sessionFactory.openSession()) {
			String hql = "select v.id.nombreComunidad, v.id.partidoPolitico " +
			             "from VotosComunidadPartido v " +
			             "where v.totalVotos = (select max(v2.totalVotos) " +
			             "                     from VotosComunidadPartido v2 " +
			             "                     where v2.id.nombreComunidad = v.id.nombreComunidad)";
			List<Object[]> resultados = session.createQuery(hql, Object[].class).list();
			for (Object[] fila : resultados) {
				String comunidad = (String) fila[0];
				String partido = (String) fila[1];
				SwingUtilities.invokeLater(() -> vista.actualizarColorComunidad(comunidad, partido));
			}
		} catch (Exception e) {
			System.err.println("Error al actualizar colores: " + e.getMessage());
		}
	}

	private void cargarResultadosNacionales() {
		try (Session session = sessionFactory.openSession()) {
			String hql = "select v.id.partidoPolitico, sum(v.totalVotos) " +
			             "from VotosComunidadPartido v " +
			             "group by v.id.partidoPolitico";
			List<Object[]> datos = session.createQuery(hql, Object[].class).list();
			Map<String, Integer> resultados = new HashMap<>();
			for (Object[] fila : datos) {
				resultados.put((String) fila[0], ((Number) fila[1]).intValue());
			}
			SwingUtilities.invokeLater(() -> vista.actualizarGraficoPrincipal(resultados));
		} catch (Exception e) {
			System.err.println("Error al cargar nacionales: " + e.getMessage());
		}
	}
}
