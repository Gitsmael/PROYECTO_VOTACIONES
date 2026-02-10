package vista;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controlador.Controlador;

public class Vista extends JFrame {

    private static final long serialVersionUID = 1L;

    public JPanel contentPane;
    public JPanel panelCards;
    public JPanel panelMapa;
    public JPanel panelInformacion;

    public final String PANEL_MAPA = "MAPA";
    public final String PANEL_INFO = "INFO";

    // Componentes compartidos
    public JLabel lblNombreCiudad;

    // -------------------------
    // CONSTRUCTOR
    // -------------------------
    public Vista() {
        setTitle("Simulador de Votaciones - España");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(60, 60, 1100, 800);


        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        panelCards = new JPanel();
        panelCards.setBounds(0, 0, 1084, 761);
        panelCards.setLayout(new CardLayout());
        contentPane.add(panelCards);

        crearPanelMapa();
        crearPanelInformacion();

        panelCards.add(panelMapa, PANEL_MAPA);
        panelCards.add(panelInformacion, PANEL_INFO);
    }

    // -------------------------
    // PANEL MAPA
    // -------------------------
    private void crearPanelMapa() {
        panelMapa = new JPanel();
        panelMapa.setLayout(null);
        panelMapa.setBackground(Color.WHITE);

        // MAPA DE ESPAÑA
        JLabel lblMapaEspana = new JLabel();
        lblMapaEspana.setBounds(0, 0, 764, 760);
        lblMapaEspana.setOpaque(false);
        panelMapa.add(lblMapaEspana);

        ImageIcon icon = new ImageIcon(getClass().getResource("/img/espania.png"));
        Image img = icon.getImage().getScaledInstance(
                lblMapaEspana.getWidth(),
                lblMapaEspana.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblMapaEspana.setIcon(new ImageIcon(img));

        // BOTONES POR COMUNIDAD (coordenadas aproximadas)
        crearComunidad(360, 280, "Madrid", panelMapa);
        crearComunidad(320, 500, "Andalucía", panelMapa);
        crearComunidad(570, 170, "Cataluña", panelMapa);
        crearComunidad(500, 350, "Com. Valenciana", panelMapa);
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
        crearComunidad(630, 350,"Islas Baleares",panelMapa);
        crearComunidad(100, 650, "Islas Canarias",panelMapa);
        crearComunidad(300,640, "Ceuta", panelMapa);
        crearComunidad(400,660, "Melilla", panelMapa);


        // PANEL RESULTADOS (DERECHA)
        JPanel panelResultados = new JPanel();
        panelResultados.setBounds(720, 0, 364, 760);
        panelResultados.setLayout(null);
        panelResultados.setBackground(Color.WHITE);
        panelMapa.add(panelResultados);

        JButton btnSimular = new JButton("SIMULAR VOTACIONES");
        btnSimular.setBounds(90, 20, 230, 40);
        panelResultados.add(btnSimular);

        JLabel lblResultados = new JLabel("RESULTADOS GENERALES");
        lblResultados.setBounds(90, 82, 230, 30);
        lblResultados.setHorizontalAlignment(SwingConstants.CENTER);
        lblResultados.setFont(new Font("Arial", Font.BOLD, 16));
        panelResultados.add(lblResultados);

        crearBarra(panelResultados, 80, 150, Color.BLUE, "X");
        crearBarra(panelResultados, 150, 190, Color.RED, "Y");
        crearBarra(panelResultados, 80, 400, Color.MAGENTA, "W");
        crearBarra(panelResultados, 150, 360, Color.GREEN, "Z");

        panelMapa.setComponentZOrder(lblMapaEspana, panelMapa.getComponentCount() - 1);


    }

    // -------------------------
    // PANEL INFORMACIÓN
    // -------------------------
    private void crearPanelInformacion() {
        panelInformacion = new JPanel();
        panelInformacion.setLayout(null);
        panelInformacion.setBackground(new Color(230, 230, 230));

        lblNombreCiudad = new JLabel("Comunidad");
        lblNombreCiudad.setBounds(200, 40, 400, 50);
        lblNombreCiudad.setFont(new Font("Arial", Font.BOLD, 32));
        lblNombreCiudad.setHorizontalAlignment(SwingConstants.CENTER);
        panelInformacion.add(lblNombreCiudad);

        JButton btnVolver = new JButton("Volver al mapa");
        btnVolver.setBounds(20, 20, 160, 35);
        btnVolver.addActionListener(e -> mostrarPanel(PANEL_MAPA));
        panelInformacion.add(btnVolver);
    }

    // -------------------------
    // MÉTODOS AUXILIARES
    // -------------------------
    private void crearComunidad(
            int x, int y,
            String nombre,
            JPanel contenedor
    ) {
        // BOTÓN CUADRADO
        JButton btn = new JButton();
        btn.setBounds(x, y, 24, 24);
        btn.setBackground(new Color(30, 144, 255)); // azul visible
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);

        btn.addActionListener(e -> {
            lblNombreCiudad.setText(nombre);
            mostrarPanel(PANEL_INFO);
        });

        // LABEL CON NOMBRE
        JLabel lbl = new JLabel(nombre);
        lbl.setBounds(x - 35, y + 28, 90, 18);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(Color.BLACK);

        contenedor.add(btn);
        contenedor.add(lbl);
    }


    private void crearBarra(JPanel panel, int x, int y, Color color, String texto) {
        JPanel barra = new JPanel();
        barra.setBounds(x, y, 25, 200);
        barra.setBackground(color);
        panel.add(barra);

        JLabel lbl = new JLabel(texto);
        lbl.setBounds(x, y + 210, 25, 20);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lbl);
    }

    // -------------------------
    // CARDLAYOUT
    // -------------------------
    public void mostrarPanel(String nombrePanel) {
        CardLayout cl = (CardLayout) panelCards.getLayout();
        cl.show(panelCards, nombrePanel);
    }

    // -------------------------
    // MAIN
    // -------------------------
    public static void main(String[] args) {
        try {
            Vista frame = new Vista();
            new Controlador(frame);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
