package vista;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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

    // Colores modernos
    private final Color COLOR_PRIMARIO = new Color(52, 152, 219); // Azul moderno
    private final Color COLOR_PRIMARIO_HOVER = new Color(41, 128, 185);
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_TEXTO = new Color(44, 62, 80);
    private final Color COLOR_BORDE = new Color(210, 215, 220);

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
        contentPane.setBackground(COLOR_FONDO);
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
        panelMapa.setBackground(COLOR_FONDO);

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
        panelResultados.setBounds(740, 20, 324, 720); // Ajustado para dar aire
        panelResultados.setLayout(null);
        panelResultados.setBackground(Color.WHITE);
        panelResultados.setBorder(new LineBorder(COLOR_BORDE, 1, true));
        panelMapa.add(panelResultados);

        JButton btnSimular = new JButton("SIMULAR VOTACIONES");
        btnSimular.setBounds(47, 30, 230, 45);
        btnSimular.setBackground(COLOR_PRIMARIO);
        btnSimular.setForeground(Color.WHITE);
        btnSimular.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSimular.setFocusPainted(false);
        btnSimular.setBorder(BorderFactory.createEmptyBorder());
        btnSimular.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover para btnSimular
        btnSimular.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnSimular.setBackground(COLOR_PRIMARIO_HOVER); }
            @Override
            public void mouseExited(MouseEvent e) { btnSimular.setBackground(COLOR_PRIMARIO); }
        });
        
        panelResultados.add(btnSimular);
        btnSimular.addActionListener(e -> { if (controlador != null) controlador.simularVotaciones(); });

        JLabel lblResultados = new JLabel("RESULTADOS GENERALES");
        lblResultados.setBounds(20, 100, 284, 30);
        lblResultados.setHorizontalAlignment(SwingConstants.CENTER);
        lblResultados.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblResultados.setForeground(COLOR_TEXTO);
        panelResultados.add(lblResultados);

        datasetPrincipal = new DefaultCategoryDataset();
        barChartPrincipal = ChartFactory.createBarChart("", "Partidos", "Votos", datasetPrincipal, PlotOrientation.VERTICAL, false, true, false);
        configurarEstiloGrafico(barChartPrincipal);
        
        chartPanelPrincipal = new ChartPanel(barChartPrincipal);
        chartPanelPrincipal.setBounds(10, 140, 304, 280);
        chartPanelPrincipal.setBackground(Color.WHITE);
        panelResultados.add(chartPanelPrincipal);
        
        panelRecuentosPrincipal = new JPanel();
        panelRecuentosPrincipal.setBounds(20, 430, 284, 260);
        panelRecuentosPrincipal.setBackground(Color.WHITE);
        panelRecuentosPrincipal.setLayout(null);
        panelResultados.add(panelRecuentosPrincipal);

        panelMapa.setComponentZOrder(lblMapaEspana, panelMapa.getComponentCount() - 1);
    }

    private void crearPanelInformacion() {
        panelInformacion = new JPanel();
        panelInformacion.setLayout(null);
        panelInformacion.setBackground(COLOR_FONDO);

        // Cabecera estilizada
        JPanel headerPanel = new JPanel();
        headerPanel.setBounds(0, 0, 1100, 120);
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setLayout(null);
        headerPanel.setBorder(new LineBorder(COLOR_BORDE, 0, false));
        panelInformacion.add(headerPanel);

        lblNombreCiudad = new JLabel("Comunidad");
        lblNombreCiudad.setBounds(0, 20, 1100, 50);
        lblNombreCiudad.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblNombreCiudad.setForeground(COLOR_TEXTO);
        lblNombreCiudad.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(lblNombreCiudad);

        JButton btnVolver = new JButton("← Volver al mapa");
        btnVolver.setBounds(30, 30, 160, 35);
        btnVolver.setBackground(Color.WHITE);
        btnVolver.setForeground(COLOR_PRIMARIO);
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVolver.setBorder(new LineBorder(COLOR_PRIMARIO, 2, true));
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> mostrarPanel(PANEL_MAPA));
        headerPanel.add(btnVolver);

        String[] rangos = {"Votaciones Globales", "18 a 25 años", "24 a 40 años", "41 a 65 años", "más de 66 años"};
        comboRangos = new JComboBox<>(rangos);
        comboRangos.setBounds(400, 75, 300, 35);
        comboRangos.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        comboRangos.setBackground(Color.WHITE);
        headerPanel.add(comboRangos);
        
        comboRangos.addActionListener(e -> {
            String seleccion = (String) comboRangos.getSelectedItem();
            if (controlador != null) {
                controlador.actualizarGraficoDetallado(lblNombreCiudad.getText(), seleccion);
            }
        });

        // Contenedor del gráfico con sombra simulada (borde)
        JPanel chartContainer = new JPanel();
        chartContainer.setBounds(100, 150, 900, 420);
        chartContainer.setBackground(Color.WHITE);
        chartContainer.setLayout(null);
        chartContainer.setBorder(new LineBorder(COLOR_BORDE, 1, true));
        panelInformacion.add(chartContainer);

        datasetInfo = new DefaultCategoryDataset();
        barChartInfo = ChartFactory.createBarChart("", "Partidos", "Votos", datasetInfo, PlotOrientation.VERTICAL, false, true, false);
        configurarEstiloGrafico(barChartInfo);
        
        chartPanelInfo = new ChartPanel(barChartInfo);
        chartPanelInfo.setBounds(20, 20, 860, 380);
        chartPanelInfo.setBackground(Color.WHITE);
        chartContainer.add(chartPanelInfo);

        panelRecuentosInfo = new JPanel();
        panelRecuentosInfo.setBounds(100, 590, 900, 120);
        panelRecuentosInfo.setBackground(Color.WHITE);
        panelRecuentosInfo.setBorder(new LineBorder(COLOR_BORDE, 1, true));
        panelRecuentosInfo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 40, 35));
        panelInformacion.add(panelRecuentosInfo);
    }

    private void configurarEstiloGrafico(JFreeChart chart) {
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(new Color(230, 230, 230));
        plot.setOutlineVisible(false);
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickLabelsVisible(false);
        rangeAxis.setTickMarksVisible(false);
        rangeAxis.setAxisLineVisible(false);
        
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
        renderer.setMaximumBarWidth(0.1); // Barras más estilizadas
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
                    
                    JPanel row = new JPanel(null);
                    row.setBounds(0, yOffset, 284, 30);
                    row.setBackground(Color.WHITE);
                    
                    JLabel lblPartido = new JLabel(partido);
                    lblPartido.setBounds(10, 0, 100, 25);
                    lblPartido.setFont(new Font("Segoe UI", Font.BOLD, 14));
                    lblPartido.setForeground(COLOR_TEXTO);
                    
                    JLabel lblVotos = new JLabel(String.format("%, d", votos) + " votos");
                    lblVotos.setBounds(120, 0, 150, 25);
                    lblVotos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    lblVotos.setHorizontalAlignment(SwingConstants.RIGHT);
                    
                    row.add(lblPartido);
                    row.add(lblVotos);
                    panelRecuentosPrincipal.add(row);
                    yOffset += 35;
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
                    
                    JLabel lbl = new JLabel("<html><div style='text-align: center;'><span style='font-size: 14px; font-weight: bold; color: #2c3e50;'>" + partido + "</span><br><span style='font-size: 12px;'>" + String.format("%, d", votos) + "</span></div></html>");
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
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
        btn.setBounds(x, y, 22, 22);
        btn.setBackground(new Color(189, 195, 199)); // Gris suave inicial
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(Color.WHITE, 2));
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> {
            lblNombreCiudad.setText(nombre);
            comboRangos.setSelectedIndex(0); 
            if (controlador != null) {
                controlador.actualizarGraficoDetallado(nombre, "Votaciones Globales");
            }
            mostrarPanel(PANEL_INFO);
        });

        JLabel lbl = new JLabel(nombre);
        lbl.setBounds(x - 40, y + 25, 100, 18);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(COLOR_TEXTO);
        
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
