package juego.cliente.graphics;

import javax.swing.*;
import java.awt.*;

public class PantallaPerdedorTorneo extends PantallaBase {
    private JLabel perdedor;
    public PantallaPerdedorTorneo(Graphics graphics){
        super(graphics);
        perdedor = new JLabel("Felicitaciones!!! sos un tremendo perdedor!!! ");
        perdedor.setVisible(true);
        perdedor.setFont(new Font("TSCu_Comic", Font.BOLD, 18));
        perdedor.setSize(371, 147);
        perdedor.setLocation(57,58);
        this.add(perdedor);
    }
}
