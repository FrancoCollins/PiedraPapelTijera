package servidor;

import java.util.ArrayList;

public class Torneo {

    public static final int MAX_PLAYERS = 4;

    private ArrayList<Enfrentamiento> enfrentamientos;
    private ArrayList<Jugador> jugadores;
    private Jugador ganador;

    public Torneo(){

    }

    public synchronized void agregarJugador(Jugador jugador){
        this.jugadores.add(jugador);

        if(jugadores.size() == MAX_PLAYERS)
            new Thread(comenzar()).start();

    }

    public Runnable comenzar(){
        return () -> {
            for (var enf : enfrentamientos)
                new Thread(enf).start();
        };
    }
}
