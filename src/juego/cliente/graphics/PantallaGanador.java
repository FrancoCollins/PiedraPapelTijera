package juego.cliente.graphics;

import javax.swing.*;
import java.awt.*;

public class PantallaGanador extends PantallaBase {
    private JLabel felicitacion;


    public PantallaGanador(Graphics graphics){
        super(graphics);
        felicitacion = new JLabel("Felicitaciones!!! eres el ganador del gran torneo ");
        felicitacion.setVisible(true);
        felicitacion.setFont(new Font("TSCu_Comic", Font.BOLD, 18));
        felicitacion.setSize(371, 147);
        felicitacion.setLocation(57,58);
        this.add(felicitacion);
    }
}
