package juego.cliente.graphics;

import juego.Senal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PantallaEnfrentamiento extends JPanel {

    private Graphics graphics;

    private JLabel mensaje;

    private JLabel puntaje;

    private JLabel nombreRival;

    private JLabel piedra;

    private JLabel papel;

    private JLabel tijeras;


    private boolean opcionSeleccionada = false;

    public PantallaEnfrentamiento(Graphics graphics) {
        this.graphics = graphics;
        this.setSize(500,500);
        setLayout(null);


        puntaje = new JLabel("0 | 0");
        puntaje.setSize(100,60);
        puntaje.setFont(puntaje.getFont().deriveFont(Font.PLAIN, 20));
        puntaje.setLocation(250,130);
        this.add(puntaje);

        nombreRival = new JLabel();
        nombreRival.setSize(500,100);
        nombreRival.setFont(nombreRival.getFont().deriveFont(Font.PLAIN, 25));
        nombreRival.setLocation(200,0);
        this.add(nombreRival);

        mensaje = new JLabel();
        mensaje.setSize(500,100);
        mensaje.setFont(mensaje.getFont().deriveFont(Font.PLAIN, 30));
        mensaje.setLocation(0,50);
        this.add(mensaje);


        JPanel panel_piedra = new JPanel();
        panel_piedra.setBounds(54, 250, 100, 130);
        this.add(panel_piedra);

        piedra = new JLabel("");
        panel_piedra.add(piedra);
        piedra.setIcon(new ImageIcon(PantallaEnfrentamiento.class.getResource("/juego/cliente/graphics/img/piedra.png")));

        JLabel lblPiedra = new JLabel("Piedra");
        panel_piedra.add(lblPiedra);



        JPanel panel_papel = new JPanel();
        panel_papel.setBounds(200, 250, 100, 130);
        this.add(panel_papel);

        JLabel papel_img = new JLabel("");
        papel_img.setIcon(new ImageIcon(PantallaEnfrentamiento.class.getResource("/juego/cliente/graphics/img/papel.png")));
        panel_papel.add(papel_img);

        papel = new JLabel("Papel");
        panel_papel.add(papel);




        JPanel panel_tijeras = new JPanel();
        panel_tijeras.setBounds(357, 250, 100, 130);
        this.add(panel_tijeras);

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
                if(opcionSeleccionada)
                    return;

                graphics.getFunctionality().getSignalManager().enviarSenal(senal);
                mensaje.setText("Selección enviada!");

                opcionSeleccionada = true;

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

    public void onNombreDelRival(String nombre){
        nombreRival.setText("Rival: " + nombre);
    }

    public void onRondaGanada() {
        mensaje.setText("¡Has ganado la ronda!");
        opcionSeleccionada = false;
    }

    public void onRondaPerdida(){
        mensaje.setText("Has perdido la ronda :(");
        opcionSeleccionada = false;
    }

    public void onEmpate() {
        mensaje.setText("Empate!!!");
        opcionSeleccionada = false;
    }

    public void onEnfrentamientoGanado(){
        mensaje.setText("¡Has ganado el enfrentamiento!");
        opcionSeleccionada = true;
    }

    public void onEnfrentamientoPerdido(){
        mensaje.setText("Has perdido el enfrentamiento :(");
        opcionSeleccionada = true;
    }

    public void onEmpezarFinal(){
        mensaje.setText("¡Empieza la final!");
        opcionSeleccionada = false;
    }

    public void onObtenerPuntaje(int[] puntajes) {
        puntaje.setText(puntajes[0] + " | " + puntajes[1]);
    }
}