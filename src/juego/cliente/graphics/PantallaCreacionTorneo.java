package juego.cliente.graphics;

import juego.Senal;
import juego.servidor.Torneo;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PantallaCreacionTorneo extends JPanel {

    private Graphics graphics;

    private boolean senalEnviada = false;

    public PantallaCreacionTorneo(Graphics graphics){
        this.graphics = graphics;


        JPanel panel_partida_publica = new JPanel();
        panel_partida_publica.setBounds(226, 60, 200, 250);
        this.add(panel_partida_publica);

        JLabel img_torneoPublico = new JLabel("");
        img_torneoPublico.setIcon(new ImageIcon(PantallaCreacionTorneo.class.getResource("/juego/cliente/graphics/img/partida_privada.png")));
        panel_partida_publica.add(img_torneoPublico);

        JLabel lblTorneoPublico = new JLabel("Torneo Público");
        panel_partida_publica.add(lblTorneoPublico);





        JPanel panel_partida_privada = new JPanel();
        panel_partida_privada.setBounds(226, 60, 200, 250);
        this.add(panel_partida_privada);

        JLabel img_torneoPrivado = new JLabel("");
        img_torneoPrivado.setIcon(new ImageIcon(PantallaCreacionTorneo.class.getResource("/juego/cliente/graphics/img/partida_publica.png")));
        panel_partida_privada.add(img_torneoPrivado);

        JLabel lblTorneoPrivado = new JLabel("Torneo Privado");
        panel_partida_privada.add(lblTorneoPrivado);

        panel_partida_publica.addMouseListener(listener(Senal.CREAR_TORNEO_PUBLICO));
        panel_partida_privada.addMouseListener(listener(Senal.CREAR_TORNEO_PRIVADO));


        this.setSize(500,500);
        this.setVisible(true);
    }



    public MouseListener listener(int senal){
        return new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(senalEnviada)
                    return;

                graphics.getFunctionality().getSignalManager().enviarSenalDeConexion();
                graphics.getFunctionality().getSignalManager().enviarSenal(senal);
                graphics.getFunctionality().getSignalManager().enviarPaquete("Nombre del torneo");
                graphics.getFunctionality().getSignalManager().enviarPaquete("4");
                senalEnviada = true;
                graphics.setContentPane(graphics.getPantallaConexion());
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
