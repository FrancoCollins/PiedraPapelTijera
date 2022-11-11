package juego.cliente;

public class GameFunctionality {

    private boolean continuarPartida;
    private boolean continuarTorneo;



    public void actualizarContinuarPartida(boolean continuar){
        this.continuarPartida = continuar;
    }

    public void actualizarContinuarTorneo(boolean continuar) {
        this.continuarTorneo = continuar;
    }

}
