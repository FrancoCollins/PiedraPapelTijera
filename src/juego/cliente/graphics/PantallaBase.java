package juego.cliente.graphics;

import javax.swing.*;

public abstract class PantallaBase extends JPanel {

    protected final int ancho = 500;
    protected final int alto = 500;

    protected Graphics graphics;

    protected JButton boton_volver;

    public PantallaBase(Graphics graphics){
        this.graphics = graphics;
        setLayout(null);
        setSize(ancho, alto);
        setVisible(true);


    }
}
