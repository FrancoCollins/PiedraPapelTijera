package juego.cliente.graphics;

import juego.Senal;

import javax.swing.*;
import javax.xml.stream.Location;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PantallaEnfrentamiento extends JPanel {

    private Graphics graphics;

    private JLabel piedra;

    private JLabel papel;

    private JLabel tijeras;


    private boolean senalEnviada = false;

    public PantallaEnfrentamiento(Graphics graphics) {
        this.graphics = graphics;
        this.setSize(500,500);
        setLayout(null);


        JPanel panel_piedra = new JPanel();
        panel_piedra.setBounds(54, 185, 100, 130);
        add(panel_piedra);

        piedra = new JLabel("");
        panel_piedra.add(piedra);
        piedra.setIcon(new ImageIcon(PantallaEnfrentamiento.class.getResource("/juego/cliente/graphics/img/piedra.png")));

        JLabel lblPiedra = new JLabel("Piedra");
        panel_piedra.add(lblPiedra);



        JPanel panel_papel = new JPanel();
        panel_papel.setBounds(200, 185, 100, 130);
        add(panel_papel);

        JLabel papel_img = new JLabel("");
        papel_img.setIcon(new ImageIcon(PantallaEnfrentamiento.class.getResource("/juego/cliente/graphics/img/papel.png")));
        panel_papel.add(papel_img);

        papel = new JLabel("Papel");
        panel_papel.add(papel);




        JPanel panel_tijeras = new JPanel();
        panel_tijeras.setBounds(357, 185, 100, 130);
        add(panel_tijeras);

        JLabel tijeras_img = new JLabel("");
        tijeras_img.setIcon(new ImageIcon(PantallaEnfrentamiento.class.getResource("/juego/cliente/graphics/img/tijeras.png")));
        panel_tijeras.add(tijeras_img);

        tijeras = new JLabel("Tijeras");
        panel_tijeras.add(tijeras);




        panel_piedra.addMouseListener(mouseListener(Senal.PIEDRA));
        panel_papel.addMouseListener(mouseListener(Senal.PAPEL));
        panel_tijeras.addMouseListener(mouseListener(Senal.TIJERA));
    }


    private MouseListener mouseListener(int senal){
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(senalEnviada)
                    return;

                graphics.getFunctionality().getSignalManager().enviarSenal(senal);
                    senalEnviada = true;

                System.out.println("Clicked " + senal);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }
}