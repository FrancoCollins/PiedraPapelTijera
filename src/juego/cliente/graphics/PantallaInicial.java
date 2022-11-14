package juego.cliente.graphics;

import juego.Senal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PantallaInicial extends PantallaBase {

    private JLabel titulo;

    private JButton createServer;
    private JButton joinServer;

    public PantallaInicial(Graphics graphics){
        super(graphics);

        titulo = new JLabel("Piedra, Papel, o Tijera!");
        titulo.setFont(new Font("TSCu_Comic", Font.BOLD, 34));
        titulo.setSize(371, 147);
        titulo.setLocation(57,58);
        this.add(titulo);

        joinServer = new JButton("Unirse a un torneo");
        joinServer.addActionListener(e -> {
            graphics.getFunctionality().getSignalManager().enviarSenal(Senal.CONECTARSE);
            System.out.println("Enviada senal de conectarse");
            graphics.getFunctionality().getSignalManager().enviarSenal(Senal.SOLICITAR_LISTA_TORNEOS);
            System.out.println("Enviada senal de solicitar lista torneos");
            graphics.setContentPane(graphics.getPantallaUnirseTorneo());
        });
        joinServer.setSize(144,27);

        createServer = new JButton("Crear torneo");
        createServer.addActionListener(e -> graphics.setContentPane(graphics.getPantallaCreacionTorneo()));
        createServer.setSize(108,27);
        createServer.setLocation(63, 228);
        this.add(createServer);
        joinServer.setLocation(262, 228);
        this.add(joinServer);

    }
}
