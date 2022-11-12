package juego.cliente;

import juego.Senal;
import juego.cliente.graphics.Graphics;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class GameFunctionality {

    Graphics graphics;

    SignalManager signalManager;

    private boolean continuarPartida;
    private boolean continuarTorneo;



    public void actualizarContinuarPartida(boolean continuar){
        this.continuarPartida = continuar;
    }

    public void actualizarContinuarTorneo(boolean continuar) {
        this.continuarTorneo = continuar;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public SignalManager getSignalManager() {
        return signalManager;
    }

    public void setSignalManager(SignalManager signalManager) {
        this.signalManager = signalManager;
    }
}
