package juego.cliente.graphics;

import juego.Senal;

import javax.swing.*;
import java.awt.*;

public class PantallaUnirseTorneo extends PantallaBase {

    private JTable tablaTorneos;
    private JTextField entradaCodigoTorneo;

    private JLabel titulo;

    private JButton unirse_torneo;

    private JButton btn_TorneoPrivado;

    private JButton btn_refresh;


    public PantallaUnirseTorneo(Graphics graphics) {
        super(graphics);

        titulo = new JLabel("Lista de torneos disponibles");
        titulo.setFont(new Font("TSCu_Comic", Font.BOLD, 34));
        titulo.setSize(500, 27);
        titulo.setLocation(0, 58);
        this.add(titulo);


        unirse_torneo = new JButton("Unirse al torneo");
        unirse_torneo.setSize(150, 30);
        unirse_torneo.setLocation(70, 420);
        this.add(unirse_torneo);

        btn_TorneoPrivado = new JButton("Torneo Privado");
        btn_TorneoPrivado.setSize(150, 30);
        btn_TorneoPrivado.setLocation(250, 420);
        btn_TorneoPrivado.addActionListener((event) -> cambiarAUnirseTorneoPrivado());
        this.add(btn_TorneoPrivado);

        btn_refresh = new JButton("Actualizar");
        btn_refresh.setSize(150, 30);
        btn_refresh.setLocation(340, 10);
        btn_refresh.addActionListener((event) -> refrescar());
        this.add(btn_refresh);

    }

    private void refrescar() {
        graphics.getFunctionality().getSignalManager().enviarSenal(Senal.SOLICITAR_LISTA_TORNEOS);
        System.out.println("Enviada senal de solicitar lista torneos");
        this.revalidate();
        this.repaint();
    }

    public void onListaTorneoRecibida(JTable tabla) {
        this.tablaTorneos = tabla;

        JScrollPane panel_tabla = new JScrollPane(tabla);
        panel_tabla.setSize(500, 300);
        panel_tabla.setLocation(0, 100);
        this.add(panel_tabla);

        unirse_torneo.addActionListener(e -> {
            if (tabla.getRowCount() == 0)
                return;

            int columnaConLaClave = 2;
            String clave = tabla.getValueAt(tabla.getSelectedRow(), columnaConLaClave).toString();

            graphics.getFunctionality().setClaveTorneo(clave);
            graphics.getFunctionality().getSignalManager().enviarSenal(Senal.UNIRSE_TORNEO_PUBLICO);
            graphics.getFunctionality().getSignalManager().enviarPaquete(clave);
        });
        panel_tabla.revalidate();
        panel_tabla.repaint();
        revalidate();
        repaint();
    }

    private void cambiarAUnirseTorneoPrivado() {
        this.btn_TorneoPrivado.setVisible(false);
        this.tablaTorneos.setVisible(false);
        this.titulo.setVisible(false);
        this.unirse_torneo.setVisible(false);

        JLabel lbl_ingreseElCodigo = new JLabel("Ingrese el código del torneo.");
        lbl_ingreseElCodigo.setSize(300, 40);
        lbl_ingreseElCodigo.setLocation(30, 50);
        this.add(lbl_ingreseElCodigo);

        JTextField codigo = new JTextField();
        codigo.setSize(200, 30);
        codigo.setLocation(150, 200);
        this.add(codigo);

        JButton btn_unirseAlTorneo = new JButton("Unirse al torneo");
        btn_unirseAlTorneo.setSize(200, 30);
        btn_unirseAlTorneo.setLocation(70, 420);
        btn_unirseAlTorneo.addActionListener(e -> {
            System.out.println("Enviado codigo " + codigo.getText());
            graphics.getFunctionality().getSignalManager().enviarSenal(Senal.UNIRSE_TORNEO_PRIVADO);
            graphics.getFunctionality().getSignalManager().enviarPaquete(codigo.getText());
        });
        this.add(btn_unirseAlTorneo);

        revalidate();
        repaint();
    }
}
