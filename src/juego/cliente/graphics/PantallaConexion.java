package juego.cliente.graphics;

import javax.swing.*;

public class PantallaConexion extends JPanel {

    private Graphics graphics;
    private JLabel conexion;

    private JLabel lbl_nombre;
    private JTextField nombreDeUsuario;

    private JButton enviar;

    public PantallaConexion(Graphics graphics){
        this.graphics = graphics;
        nombreDeUsuario = new JTextField();
        lbl_nombre = new JLabel("Nombre de usuario: ");
        conexion = new JLabel("Conectando...");
        enviar = new JButton("Enviar");

        this.add(nombreDeUsuario);
        this.add(lbl_nombre);
        this.add(conexion);
        this.add(enviar);

        conexion.setSize(200,200);

        this.setLayout(null);
        this.setSize(500,500);
        this.setVisible(true);
    }

    public void onConectando(){
        conexion.setText("Conectando...");

        lbl_nombre.setVisible(false);
        nombreDeUsuario.setVisible(false);
        enviar.setVisible(false);
    }

    public void onConexionExitosa(){
        conexion.setText("Conexion exitosa!");


        lbl_nombre.setSize(194,40);
        lbl_nombre.setLocation(100,154);

        nombreDeUsuario.setSize(217,40);
        nombreDeUsuario.setLocation(216,155);

        enviar.setSize(100,50);
        enviar.setLocation(200, 300);

        lbl_nombre.setVisible(true);
        nombreDeUsuario.setVisible(true);
        enviar.setVisible(true);

        enviar.addActionListener((e) -> {
            graphics.getFunctionality()
                .getSignalManager()
                .enviarPaquete(nombreDeUsuario.getText());
            onConectando();
        });
    }
}
