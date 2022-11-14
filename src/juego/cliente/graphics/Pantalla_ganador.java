package juego.cliente.graphics;

import juego.cliente.graphics.Graphics;
import juego.cliente.graphics.PantallaBase;
import juego.cliente.graphics.PantallaCreacionTorneo;

import javax.swing.*;
import java.awt.*;

public class Pantalla_ganador extends PantallaBase {
    private JLabel felicitacion;


    public Pantalla_ganador(Graphics graphics){
        super(graphics);
        felicitacion = new JLabel("Felicitaciones!!! eres el ganador del gran torneo ");
        felicitacion.setVisible(true);
        felicitacion.setFont(new Font("TSCu_Comic", Font.BOLD, 34));
        felicitacion.setSize(371, 147);
        felicitacion.setLocation(57,58);
        this.add(felicitacion);
    }
}
