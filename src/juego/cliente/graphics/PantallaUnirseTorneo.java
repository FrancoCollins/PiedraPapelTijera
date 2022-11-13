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
        titulo.setSize(371, 147);
        titulo.setLocation(57,58);
        setLayout(null);
        this.add(titulo);

        tablaTorneos = new JTable();

        unirse_torneo = new JButton("Unirse al torneo");
        unirse_torneo.addActionListener(e -> graphics.getFunctionality().getSignalManager().enviarSenal(Senal.UNIRSE_TORNEO));
        unirse_torneo.setSize(144,27);

        entradaCodigoTorneo = new JTextField();

        this.setSize(500, 500);
        this.setVisible(true);
    }

    public void onListaTorneo(JTable tabla){
        this.tablaTorneos = tabla;
        this.tablaTorneos.setBounds(200, 200, 200, 200);
        this.add(tabla);

    }
}
