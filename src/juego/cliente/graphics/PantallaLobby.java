package juego.cliente.graphics;

import javax.swing.*;

public class PantallaLobby extends JPanel {

    private Graphics graphics;

    private JLabel lbl_jugadores;

    private JLabel jugadores;

    public PantallaLobby(Graphics graphics){
        this.graphics = graphics;

        lbl_jugadores = new JLabel("Jugadores en el lobby: ");
        lbl_jugadores.setSize(200,30);
        lbl_jugadores.setLocation(50, 250);
        this.add(lbl_jugadores);

        jugadores = new JLabel();
        jugadores.setSize(100, 30);
        jugadores.setLocation(280, 250);
        this.add(jugadores);


        this.setSize(500,500);
        this.setLayout(null);
        this.setVisible(true);
    }

    public void onJugadoresEnLobby(String j){
        jugadores.setText(j);
    }
}
