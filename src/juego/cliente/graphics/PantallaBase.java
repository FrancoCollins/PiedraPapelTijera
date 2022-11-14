package juego.cliente.graphics;

import javax.swing.*;

public class PantallaBase extends JPanel {

    protected final int ancho = 500;
    protected final int alto = 500;

    protected Graphics graphics;

    public PantallaBase(Graphics graphics){
        this.graphics = graphics;
        setLayout(null);
        setSize(ancho, alto);
        setVisible(true);


    }
}
