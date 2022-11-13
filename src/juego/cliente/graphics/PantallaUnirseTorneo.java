package juego.cliente.graphics;

import juego.Senal;

import javax.swing.*;
import java.awt.*;

public class PantallaUnirseTorneo extends JPanel {

    private JTable tablaTorneos;
    private JTextField entradaCodigoTorneo;

    private Graphics graphics;

    private JLabel titulo;

    private JButton unirse_torneo;

    public PantallaUnirseTorneo(Graphics graphics) {
        this.graphics = graphics;

        titulo = new JLabel("Lista de torneos disponibles");
        titulo.setFont(new Font("TSCu_Comic", Font.BOLD, 34));
        titulo.setSize(500, 27);
        titulo.setLocation(0,58);
        setLayout(null);
        this.add(titulo);


        unirse_torneo = new JButton("Unirse al torneo");
        unirse_torneo.setSize(144,27);
        unirse_torneo.setLocation(150, 420);
        this.add(unirse_torneo);

        entradaCodigoTorneo = new JTextField();

        this.setSize(500, 500);
        this.setVisible(true);
    }

    public void onListaTorneoRecibida(JTable tabla){
        this.tablaTorneos = tabla;

        JScrollPane panel_tabla = new JScrollPane(tabla);
        panel_tabla.setSize(500,300);
        panel_tabla.setLocation(0,100);
        this.add(panel_tabla);

        unirse_torneo.addActionListener(e -> {
            int columnaConLaClave = 2;
            String clave = tabla.getValueAt(tabla.getSelectedRow(), columnaConLaClave).toString();

            graphics.getFunctionality().setClaveTorneo(clave);
            graphics.getFunctionality().getSignalManager().enviarSenal(Senal.UNIRSE_TORNEO);
            graphics.getFunctionality().getSignalManager().enviarPaquete(clave);
        });

        revalidate();
        repaint();
    }
}
