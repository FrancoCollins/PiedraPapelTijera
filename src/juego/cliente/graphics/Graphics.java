package juego.cliente.graphics;

import juego.cliente.GameFunctionality;

import javax.swing.*;

public class Graphics extends JFrame {

    private PantallaInicial pantallaInicial;

    private PantallaConexion pantallaConexion;

    private PantallaEnfrentamiento pantallaSeleccion;

    private PantallaLobby pantallaLobby;

    private GameFunctionality functionality;



    public Graphics(GameFunctionality functionality){
        this.functionality = functionality;
        this.pantallaSeleccion = new PantallaEnfrentamiento(this);
        this.pantallaInicial = new PantallaInicial(this);
        this.pantallaConexion = new PantallaConexion(this);
        this.pantallaLobby = new PantallaLobby(this);
        this.setContentPane(pantallaInicial);
        this.setSize(pantallaInicial.getSize());
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }


    public void onConectando() {
        this.setContentPane(pantallaConexion);
    }

    public void onConexionExitosa(){
        System.out.println("GRAPHICS dice: Conectado!");
        pantallaConexion.onConexionExitosa();
    }

    public GameFunctionality getFunctionality(){
        return functionality;
    }

    public void onEnviarSeleccion() {
        this.setContentPane(pantallaSeleccion);
    }


    public void onEnviarNombre() {
        System.out.println("GRAPHICS dice: Esperando a que el usuario escriba su nombre.");
    }

    public void onConexionExitosaTorneo() {
        this.setContentPane(pantallaLobby);
    }

    public void onJugadoresEnLobby(String jugadores) {
        pantallaLobby.onJugadoresEnLobby(jugadores);
    }
}

