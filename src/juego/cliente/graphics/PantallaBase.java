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

        boton_volver = new JButton("Volver");
        boton_volver.setLocation(20,20);
        boton_volver.setSize(150, 30);
        boton_volver.addActionListener(e -> {
            graphics.cambiarPantalla(graphics.getPantallaInicial());
        });
        this.add(boton_volver);
    }
}
