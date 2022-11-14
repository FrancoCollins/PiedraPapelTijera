package juego.cliente.graphics;

import juego.cliente.GameFunctionality;

import javax.swing.*;

public class Graphics extends JFrame {

    private Pantalla_ganador pantallaGanadorTorneo;

    private PantallaInicial pantallaInicial;

    private PantallaConexion pantallaConexion;

    private PantallaEnfrentamiento pantallaEnfrentamiento;

    private PantallaLobby pantallaLobby;

    private PantallaCreacionTorneo pantallaCreacionTorneo;

    private PantallaUnirseTorneo pantallaUnirseTorneo;

    private GameFunctionality functionality;
    private PantallaPerdedorTorneo pantallaPerdedorTorneo;


    public Graphics(GameFunctionality functionality){
        this.functionality = functionality;
        this.pantallaPerdedorTorneo = new PantallaPerdedorTorneo(this);
        this.pantallaGanadorTorneo = new Pantalla_ganador(this);
        this.pantallaEnfrentamiento = new PantallaEnfrentamiento(this);
        this.pantallaInicial = new PantallaInicial(this);
        this.pantallaConexion = new PantallaConexion(this);
        this.pantallaLobby = new PantallaLobby(this);
        this.pantallaCreacionTorneo = new PantallaCreacionTorneo(this);
        this.pantallaUnirseTorneo = new PantallaUnirseTorneo(this);
        this.setContentPane(pantallaInicial);
        this.setSize(pantallaInicial.getSize());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void onConectando() {
        this.cambiarPantalla(pantallaConexion);
    }

    public void onConexionExitosa(){
        System.out.println("GRAPHICS dice: Conectado!");
        pantallaConexion.onConexionExitosa();
    }

    public GameFunctionality getFunctionality(){
        return functionality;
    }

    public void onEnviarSeleccion() {
        this.cambiarPantalla(pantallaEnfrentamiento);
    }


    public void onEnviarNombre() {
        System.out.println("GRAPHICS dice: Esperando a que el usuario escriba su nombre.");
        this.cambiarPantalla(pantallaConexion);
    }

    public void onConexionExitosaTorneo() {
        this.cambiarPantalla(pantallaLobby);
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

    public void onClaveTorneo(String clave) {
        functionality.setClaveTorneo(clave);
        pantallaLobby.onClaveTorneo(clave);
    }

    public void setNombreTorneo(String nombreTorneo) {
        pantallaLobby.setNombreTorneo(nombreTorneo);
    }

    public void cambiarPantalla(PantallaBase pantallaSiguiente){
        PantallaBase pantallaActual = (PantallaBase) this.getContentPane();
        if(pantallaActual instanceof PantallaCreacionTorneo){
            this.pantallaCreacionTorneo = new PantallaCreacionTorneo(this);
        } else if (pantallaActual instanceof PantallaLobby) {
            pantallaLobby = new PantallaLobby(this);
        } else if (pantallaActual instanceof PantallaUnirseTorneo) {
            pantallaUnirseTorneo = new PantallaUnirseTorneo(this);
        } else if (pantallaActual instanceof PantallaEnfrentamiento) {
            pantallaEnfrentamiento = new PantallaEnfrentamiento(this);
        }

        this.setContentPane(pantallaSiguiente);
    }

    public Pantalla_ganador getPantallaGanadorTorneo() {
        return pantallaGanadorTorneo;
    }

    public void setPantallaGanadorTorneo(Pantalla_ganador pantallaGanadorTorneo) {
        this.pantallaGanadorTorneo = pantallaGanadorTorneo;
    }

    public void volver() {
        this.setContentPane(pantallaInicial);
    }

    public PantallaPerdedorTorneo getPantallaPerdedorTorneo() {
        return this.pantallaPerdedorTorneo;
    }
}

