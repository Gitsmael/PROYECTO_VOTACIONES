package vista;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import controlador.Controlador;

public class Vista extends JFrame {

    private static final long serialVersionUID = 1L;

    public JPanel contentPane;
    public JPanel panelCards;
    public JPanel panelMapa;
    public JPanel panelInformacion;
    
    private ChartPanel chartPanelPrincipal;
    private DefaultCategoryDataset datasetPrincipal;
    private JFreeChart barChartPrincipal;
    private JPanel panelRecuentosPrincipal;

    private ChartPanel chartPanelInfo;
    private DefaultCategoryDataset datasetInfo;
    private JFreeChart barChartInfo;
    private JComboBox<String> comboRangos;
    private JPanel panelRecuentosInfo;

    public final String PANEL_MAPA = "MAPA";
    public final String PANEL_INFO = "INFO";

    public JLabel lblNombreCiudad;
    private Controlador controlador;
    
    private Map<String, JButton> botonesComunidades = new HashMap<>();
    private Map<String, Color> coloresPartidos = new HashMap<>();

    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
    }

    public Vista() {
        setTitle("Simulador de Votaciones - España");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(60, 60, 1100, 850);
        
        coloresPartidos.put("W", Color.BLUE);
        coloresPartidos.put("X", Color.RED);
        coloresPartidos.put("Y", Color.GREEN);
        coloresPartidos.put("Z", new Color(128, 0, 128));

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        panelCards = new JPanel();
        panelCards.setBounds(0, 0, 1084, 811);
        panelCards.setLayout(new CardLayout());
        contentPane.add(panelCards);

        crearPanelMapa();
        crearPanelInformacion();

        panelCards.add(panelMapa, PANEL_MAPA);
        panelCards.add(panelInformacion, PANEL_INFO);
    }

    private void crearPanelMapa() {
        panelMapa = new JPanel();
        panelMapa.setLayout(null);
        panelMapa.setBackground(Color.WHITE);

        JLabel lblMapaEspana = new JLabel();
        lblMapaEspana.setBounds(0, 0, 764, 760);
        panelMapa.add(lblMapaEspana);

        ImageIcon icon = new ImageIcon("src/img/espania.png");
        Image img = icon.getImage().getScaledInstance(764, 760, Image.SCALE_SMOOTH);
        lblMapaEspana.setIcon(new ImageIcon(img));

        crearComunidad(360, 280, "Madrid", panelMapa);
        crearComunidad(320, 500, "Andalucía", panelMapa);
        crearComunidad(570, 170, "Catalunia", panelMapa);
        crearComunidad(500, 350, "Comunidad Valenciana", panelMapa);
        crearComunidad(200, 100, "Galicia", panelMapa);
        crearComunidad(260, 360, "Extremadura", panelMapa);
        crearComunidad(380, 360, "Castilla-La Mancha", panelMapa); 
        crearComunidad(320, 180, "Castilla y León", panelMapa);
        crearComunidad(490, 150, "Aragón", panelMapa);
        crearComunidad(410, 50, "País Vasco", panelMapa);
        crearComunidad(440, 100, "Navarra", panelMapa);
        crearComunidad(410, 140, "La Rioja", panelMapa);
        crearComunidad(350, 40, "Cantabria", panelMapa);
        crearComunidad(270, 40, "Asturias", panelMapa);
        crearComunidad(450, 500, "Murcia", panelMapa);
        crearComunidad(630, 350,"Baleares",panelMapa);
        crearComunidad(100, 650, "Canarias",panelMapa);
        crearComunidad(300,640, "Ceuta", panelMapa);
        crearComunidad(400,660, "Melilla", panelMapa);

        JPanel panelResultados = new JPanel();
        panelResultados.setBounds(720, 0, 364, 760);
        panelResultados.setLayout(null);
        panelResultados.setBackground(Color.WHITE);
        panelMapa.add(panelResultados);

        JButton btnSimular = new JButton("SIMULAR VOTACIONES");
        btnSimular.setBounds(67, 20, 230, 40);
        panelResultados.add(btnSimular);
        btnSimular.addActionListener(e -> { if (controlador != null) controlador.simularVotaciones(); });

        JLabel lblResultados = new JLabel("RESULTADOS GENERALES");
        lblResultados.setBounds(67, 82, 230, 30);
        lblResultados.setHorizontalAlignment(SwingConstants.CENTER);
        lblResultados.setFont(new Font("Arial", Font.BOLD, 16));
        panelResultados.add(lblResultados);

        datasetPrincipal = new DefaultCategoryDataset();
        barChartPrincipal = ChartFactory.createBarChart("", "Partidos", "Votos", datasetPrincipal, PlotOrientation.VERTICAL, false, true, false);
        configurarEstiloGrafico(barChartPrincipal);
        
        chartPanelPrincipal = new ChartPanel(barChartPrincipal);
        chartPanelPrincipal.setBounds(20, 130, 320, 300);
        chartPanelPrincipal.setBackground(Color.WHITE);
        panelResultados.add(chartPanelPrincipal);
        
        panelRecuentosPrincipal = new JPanel();
        panelRecuentosPrincipal.setBounds(20, 440, 320, 200);
        panelRecuentosPrincipal.setBackground(Color.WHITE);
        panelRecuentosPrincipal.setLayout(null);
        panelResultados.add(panelRecuentosPrincipal);

        panelMapa.setComponentZOrder(lblMapaEspana, panelMapa.getComponentCount() - 1);
    }

    private void crearPanelInformacion() {
        panelInformacion = new JPanel();
        panelInformacion.setLayout(null);
        panelInformacion.setBackground(Color.WHITE);

        lblNombreCiudad = new JLabel("Comunidad");
        lblNombreCiudad.setBounds(0, 20, 1100, 50);
        lblNombreCiudad.setFont(new Font("Arial", Font.BOLD, 36));
        lblNombreCiudad.setHorizontalAlignment(SwingConstants.CENTER);
        panelInformacion.add(lblNombreCiudad);

        JButton btnVolver = new JButton("Volver al mapa");
        btnVolver.setBounds(20, 20, 160, 35);
        btnVolver.addActionListener(e -> mostrarPanel(PANEL_MAPA));
        panelInformacion.add(btnVolver);

        String[] rangos = {"Votaciones Globales", "18 a 25 años", "24 a 40 años", "41 a 65 años", "más de 66 años"};
        comboRangos = new JComboBox<>(rangos);
        comboRangos.setBounds(400, 80, 300, 30);
        comboRangos.setFont(new Font("Arial", Font.PLAIN, 16));
        panelInformacion.add(comboRangos);
        
        comboRangos.addActionListener(e -> {
            String seleccion = (String) comboRangos.getSelectedItem();
            if (controlador != null) {
                controlador.actualizarGraficoDetallado(lblNombreCiudad.getText(), seleccion);
            }
        });

        datasetInfo = new DefaultCategoryDataset();
        barChartInfo = ChartFactory.createBarChart("", "Partidos", "Votos", datasetInfo, PlotOrientation.VERTICAL, false, true, false);
        configurarEstiloGrafico(barChartInfo);
        
        chartPanelInfo = new ChartPanel(barChartInfo);
        chartPanelInfo.setBounds(150, 130, 800, 400);
        chartPanelInfo.setBackground(Color.WHITE);
        panelInformacion.add(chartPanelInfo);

        panelRecuentosInfo = new JPanel();
        panelRecuentosInfo.setBounds(150, 550, 800, 150);
        panelRecuentosInfo.setBackground(Color.WHITE);
        panelRecuentosInfo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 50, 10));
        panelInformacion.add(panelRecuentosInfo);
    }

    private void configurarEstiloGrafico(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelsVisible(false);
        rangeAxis.setTickMarksVisible(false);
        BarRenderer renderer = new BarRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public java.awt.Paint getItemPaint(int row, int column) {
                String partido = (String) getPlot().getDataset().getColumnKey(column);
                return coloresPartidos.getOrDefault(partido, Color.GRAY);
            }
        };
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());
        plot.setRenderer(renderer);
    }

    public void actualizarGraficoPrincipal(Map<String, Integer> resultados) {
        SwingUtilities.invokeLater(() -> {
            datasetPrincipal.clear();
            panelRecuentosPrincipal.removeAll();
            if (resultados != null && !resultados.isEmpty()) {
                int yOffset = 0;
                java.util.List<String> partidos = new java.util.ArrayList<>(resultados.keySet());
                java.util.Collections.sort(partidos);
                for (String partido : partidos) {
                    Integer votos = resultados.get(partido);
                    datasetPrincipal.addValue(votos, "Votos", partido);
                    JLabel lbl = new JLabel(partido + " = " + votos);
                    lbl.setBounds(10, yOffset, 300, 25);
                    lbl.setFont(new Font("Arial", Font.BOLD, 14));
                    lbl.setForeground(Color.BLACK);
                    panelRecuentosPrincipal.add(lbl);
                    yOffset += 30;
                }
            }
            chartPanelPrincipal.revalidate();
            chartPanelPrincipal.repaint();
            panelRecuentosPrincipal.revalidate();
            panelRecuentosPrincipal.repaint();
        });
    }

    public void actualizarGraficoDetallado(Map<String, Integer> resultados) {
        SwingUtilities.invokeLater(() -> {
            datasetInfo.clear();
            panelRecuentosInfo.removeAll();
            if (resultados != null && !resultados.isEmpty()) {
                java.util.List<String> partidos = new java.util.ArrayList<>(resultados.keySet());
                java.util.Collections.sort(partidos);
                for (String partido : partidos) {
                    Integer votos = resultados.get(partido);
                    datasetInfo.addValue(votos, "Votos", partido);
                    JLabel lbl = new JLabel(partido + " = " + votos);
                    lbl.setFont(new Font("Arial", Font.BOLD, 18));
                    lbl.setForeground(Color.BLACK);
                    panelRecuentosInfo.add(lbl);
                }
            }
            chartPanelInfo.revalidate();
            chartPanelInfo.repaint();
            panelRecuentosInfo.revalidate();
            panelRecuentosInfo.repaint();
        });
    }

    private String normalizar(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase()
                .replaceAll("[^a-z]", "")
                .trim();
    }

    public void actualizarColorComunidad(String nombreComunidadBD, String partidoGanador) {
        SwingUtilities.invokeLater(() -> {
            String normalBD = normalizar(nombreComunidadBD);
            
            // Lógica especial Castilla-La Mancha
            if (normalBD.contains("castilla") && normalBD.contains("mancha")) {
                for (Map.Entry<String, JButton> entry : botonesComunidades.entrySet()) {
                    String normalVista = normalizar(entry.getKey());
                    if (normalVista.contains("castilla") && normalVista.contains("mancha")) {
                        entry.getValue().setBackground(coloresPartidos.getOrDefault(partidoGanador, Color.GRAY));
                        entry.getValue().repaint();
                        return;
                    }
                }
            }

            for (Map.Entry<String, JButton> entry : botonesComunidades.entrySet()) {
                String normalVista = normalizar(entry.getKey());
                if (normalVista.equals(normalBD) || normalVista.contains(normalBD) || normalBD.contains(normalVista)) {
                    entry.getValue().setBackground(coloresPartidos.getOrDefault(partidoGanador, Color.GRAY));
                    entry.getValue().repaint();
                    return;
                }
            }
        });
    }

    private void crearComunidad(int x, int y, String nombre, JPanel contenedor) {
        JButton btn = new JButton();
        btn.setBounds(x, y, 24, 24);
        btn.setBackground(new Color(30, 144, 255));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.addActionListener(e -> {
            lblNombreCiudad.setText(nombre);
            comboRangos.setSelectedIndex(0); // Votaciones Globales
            if (controlador != null) {
                controlador.actualizarGraficoDetallado(nombre, "Votaciones Globales");
            }
            mostrarPanel(PANEL_INFO);
        });
        JLabel lbl = new JLabel(nombre);
        lbl.setBounds(x - 35, y + 28, 90, 18);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(Color.BLACK);
        contenedor.add(btn);
        contenedor.add(lbl);
        botonesComunidades.put(nombre, btn);
    }

    public void mostrarPanel(String nombrePanel) {
        CardLayout cl = (CardLayout) panelCards.getLayout();
        cl.show(panelCards, nombrePanel);
    }

    public static void main(String[] args) {
        try {
            Vista frame = new Vista();
            Controlador controlador = new Controlador(frame);
            frame.setControlador(controlador);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
