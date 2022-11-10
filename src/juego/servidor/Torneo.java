package juego.servidor;

import java.util.ArrayList;

public class Torneo {

    public static final int MAX_PLAYERS = 4;

    private ArrayList<Enfrentamiento> enfrentamientos;
    private ArrayList<Jugador> jugadores;
    private Jugador ganador;

    public Torneo() {
        jugadores = new ArrayList<>();
        enfrentamientos = new ArrayList<>();
    }

    public synchronized void agregarJugador(Jugador jugador) {
        this.jugadores.add(jugador);

        if (jugadores.size() == MAX_PLAYERS) {
            System.out.println("Comienza el torneo");
            for (int i = 0; i <= MAX_PLAYERS / 2; i += 2) {
                enfrentamientos.add(new Enfrentamiento(jugadores.get(i), jugadores.get(i + 1)));
            }
            new Thread(comenzar()).start();
        }

    }

    public Runnable comenzar() {
        return () -> {
            System.out.println(enfrentamientos.size());
            for (var enf : enfrentamientos) {
                System.out.println("Start");
                new Thread(enf).start();
            }
        };
    }
}
