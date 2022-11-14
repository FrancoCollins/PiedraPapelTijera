package juego.cliente.graphics;

import javax.swing.*;
import java.awt.*;

public class PantallaLobby extends PantallaBase {

    private JLabel lbl_jugadores;

    private JLabel jugadores;

    private JLabel lbl_MostrarClaveTorneo;

    private JTextPane lbl_claveTorneo;

    private JLabel lbl_nombreTorneo;

    public PantallaLobby(Graphics graphics){
        super(graphics);

        lbl_jugadores = new JLabel("Jugadores en el lobby: ");
        lbl_jugadores.setSize(200,30);
        lbl_jugadores.setLocation(50, 250);
        this.add(lbl_jugadores);

        jugadores = new JLabel();
        jugadores.setSize(100, 30);
        jugadores.setLocation(280, 250);
        this.add(jugadores);


        lbl_nombreTorneo = new JLabel();
        lbl_nombreTorneo.setSize(500, 40);
        lbl_nombreTorneo.setLocation(0, 80);
        lbl_nombreTorneo.setFont(lbl_nombreTorneo.getFont().
                deriveFont(Font.PLAIN, 35));
        this.add(lbl_nombreTorneo);

    }


    public void onJugadoresEnLobby(String j){
        jugadores.setText(j);
    }

    public void onClaveTorneo(String clave) {
        lbl_MostrarClaveTorneo = new JLabel("Clave del torneo:");
        lbl_MostrarClaveTorneo.setSize(200,30);
        lbl_MostrarClaveTorneo.setLocation(40,130);
        lbl_MostrarClaveTorneo.setFont(lbl_MostrarClaveTorneo.getFont()
                .deriveFont(Font.PLAIN, 20));
        this.add(lbl_MostrarClaveTorneo);

        lbl_claveTorneo = new JTextPane();
        lbl_claveTorneo.setContentType("text/html"); // let the text pane know this is what you want
        lbl_claveTorneo.setEditable(false); // as before
        lbl_claveTorneo.setBackground(null); // this is the same as a JLabel
        lbl_claveTorneo.setBorder(null); // remove the border
        lbl_claveTorneo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        lbl_claveTorneo.setSize(150, 30);
        lbl_claveTorneo.setLocation(40, 170);
        this.lbl_claveTorneo.setText(clave);
        this.add(lbl_claveTorneo);

        revalidate();
        repaint();
    }

    public void setNombreTorneo(String nombreTorneo) {
        this.lbl_nombreTorneo.setText(nombreTorneo);
    }
}
