package juego.cliente.graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PantallaInicial extends JPanel {

    private Graphics graphics;

    private JLabel titulo;

    private JButton createServer;
    private JButton joinServer;

    public PantallaInicial(Graphics graphics){
        this.graphics = graphics;

        titulo = new JLabel("Piedra, Papel, o Tijera!");
        titulo.setFont(new Font("TSCu_Comic", Font.BOLD, 34));
        titulo.setSize(371, 147);
        titulo.setLocation(57,58);
        setLayout(null);
        this.add(titulo);

        joinServer = new JButton("Unirse a un torneo");
        joinServer.addActionListener(e -> graphics.getFunctionality().getSignalManager().enviarSenalDeConexion());
        joinServer.setSize(144,27);

        createServer = new JButton("Crear torneo");
        createServer.setSize(108,27);
        createServer.setLocation(63, 228);
        this.add(createServer);
        joinServer.setLocation(262, 228);
        this.add(joinServer);

        this.setSize(500,500);
        this.setVisible(true);
    }
}
