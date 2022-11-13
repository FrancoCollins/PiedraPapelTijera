package juego.cliente.graphics;

import juego.cliente.GameFunctionality;

import javax.swing.*;

public class Graphics extends JFrame {

    private PantallaInicial pantallaInicial;

    private PantallaConexion pantallaConexion;

    private PantallaEnfrentamiento pantallaEnfrentamiento;

    private PantallaLobby pantallaLobby;

    private PantallaCreacionTorneo pantallaCreacionTorneo;

    private PantallaUnirseTorneo pantallaUnirseTorneo;

    private GameFunctionality functionality;



    public Graphics(GameFunctionality functionality){
        this.functionality = functionality;
        this.pantallaEnfrentamiento = new PantallaEnfrentamiento(this);
        this.pantallaInicial = new PantallaInicial(this);
        this.pantallaConexion = new PantallaConexion(this);
        this.pantallaLobby = new PantallaLobby(this);
        this.pantallaCreacionTorneo = new PantallaCreacionTorneo(this);
        this.pantallaUnirseTorneo = new PantallaUnirseTorneo(this);
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
        this.setContentPane(pantallaEnfrentamiento);
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


    public PantallaInicial getPantallaInicial() {
        return pantallaInicial;
    }

    public void setPantallaInicial(PantallaInicial pantallaInicial) {
        this.pantallaInicial = pantallaInicial;
    }

    public PantallaConexion getPantallaConexion() {
        return pantallaConexion;
    }

    public void setPantallaConexion(PantallaConexion pantallaConexion) {
        this.pantallaConexion = pantallaConexion;
    }

    public PantallaEnfrentamiento getPantallaEnfrentamiento() {
        return pantallaEnfrentamiento;
    }

    public void setPantallaEnfrentamiento(PantallaEnfrentamiento pantallaEnfrentamiento) {
        this.pantallaEnfrentamiento = pantallaEnfrentamiento;
    }

    public PantallaLobby getPantallaLobby() {
        return pantallaLobby;
    }

    public void setPantallaLobby(PantallaLobby pantallaLobby) {
        this.pantallaLobby = pantallaLobby;
    }

    public PantallaCreacionTorneo getPantallaCreacionTorneo() { return this.pantallaCreacionTorneo;}

    public PantallaUnirseTorneo getPantallaUnirseTorneo() {
        return pantallaUnirseTorneo;
    }
}

