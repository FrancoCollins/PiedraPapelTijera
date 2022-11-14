package juego.cliente.graphics;

import juego.Senal;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PantallaCreacionTorneo extends PantallaBase {


    private boolean senalEnviada = false;

    private JPanel pnl_partidaPublica;

    private JPanel pnl_partidaPrivada;

    public PantallaCreacionTorneo(Graphics graphics){
        super(graphics);

        pnl_partidaPublica = new JPanel();
        pnl_partidaPublica.setBounds(125, 230, 250, 250);
        this.add(pnl_partidaPublica);

        JLabel img_torneoPublico = new JLabel("");
        img_torneoPublico.setIcon(new ImageIcon(PantallaCreacionTorneo.class.getResource("/juego/cliente/graphics/img/partida_privada.png")));
        pnl_partidaPublica.add(img_torneoPublico);

        JLabel lblTorneoPublico = new JLabel("Torneo Público");
        pnl_partidaPublica.add(lblTorneoPublico);





        pnl_partidaPrivada = new JPanel();
        pnl_partidaPrivada.setBounds(125, 0, 250, 250);
        this.add(pnl_partidaPrivada);

        JLabel img_torneoPrivado = new JLabel("");
        img_torneoPrivado.setIcon(new ImageIcon(PantallaCreacionTorneo.class.getResource("/juego/cliente/graphics/img/partida_publica.png")));
        pnl_partidaPrivada.add(img_torneoPrivado);

        JLabel lblTorneoPrivado = new JLabel("Torneo Privado");
        pnl_partidaPrivada.add(lblTorneoPrivado);

        pnl_partidaPublica.addMouseListener(listener(Senal.CREAR_TORNEO_PUBLICO));
        pnl_partidaPrivada.addMouseListener(listener(Senal.CREAR_TORNEO_PRIVADO));


        this.setSize(500,500);
        this.setVisible(true);
    }



    private void pedirNombreDelTorneo(int senal){
        this.pnl_partidaPrivada.setVisible(false);
        this.pnl_partidaPublica.setVisible(false);

        JLabel lbl_nombreTorneo = new JLabel("Introduzca el nombre del torneo.");
        lbl_nombreTorneo.setSize(200,30);
        lbl_nombreTorneo.setLocation(50, 50);
        this.add(lbl_nombreTorneo);

        JTextField txt_nombreTorneo = new JTextField();
        txt_nombreTorneo.setSize(200, 30);
        txt_nombreTorneo.setLocation(50, 90);
        this.add(txt_nombreTorneo);

        JButton btn_enviar = new JButton("Enviar");
        btn_enviar.setSize(80,30);
        btn_enviar.setLocation(70, 320);
        btn_enviar.addActionListener(e -> {
            graphics.setNombreTorneo(txt_nombreTorneo.getText());
            graphics.getFunctionality().getSignalManager().enviarSenalDeConexion();
            graphics.getFunctionality().getSignalManager().enviarSenal(senal);
            graphics.getFunctionality().getSignalManager().enviarPaquete(txt_nombreTorneo.getText());
            graphics.getFunctionality().getSignalManager().enviarPaquete("4");
            senalEnviada = true;
            graphics.cambiarPantalla(graphics.getPantallaConexion());
        });
        this.add(btn_enviar);

        this.setLayout(null);
    }



    public MouseListener listener(int senal){
        return new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if(senalEnviada)
                    return;

                pedirNombreDelTorneo(senal);
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
