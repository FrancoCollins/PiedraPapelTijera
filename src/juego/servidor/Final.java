package juego.servidor;

public class Final extends Enfrentamiento {

    public Final(Jugador p1, Jugador p2) {
        super(p1, p2);
    }

    @Override
    public void run(){
        super.run();

        super.getGanadorFinal();
    }
}
