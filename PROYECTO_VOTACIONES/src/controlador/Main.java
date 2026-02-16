package controlador;

import javax.swing.SwingUtilities;

import vista.Vista;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Vista vista = new Vista();
            Controlador controlador = new Controlador(vista);

            vista.setVisible(true);
        });
    }
}
