package juego.servidor;

import java.util.ArrayList;

public class Torneo {

    public static final int MAX_PLAYERS = 4;

    private ArrayList<Enfrentamiento> enfrentamientos;
    private ArrayList<Jugador> jugadores;

    private ArrayList<Jugador> finalistas;

    private Jugador ganador;

    public Torneo() {
        jugadores = new ArrayList<>();
        enfrentamientos = new ArrayList<>();
        finalistas = new ArrayList<>();
    }

    public synchronized void agregarJugador(Jugador jugador) {
        this.jugadores.add(jugador);

        if (jugadores.size() == MAX_PLAYERS) {
            System.out.println("Comienza el torneo!");
            for (int i = 0; i <= MAX_PLAYERS / 2; i += 2) {
                enfrentamientos.add(new Enfrentamiento(jugadores.get(i), jugadores.get(i + 1)));
            }
            new Thread(comenzar()).start();
        }

    }

    public Runnable comenzar() {
        return () -> {
            for (var enf : enfrentamientos)
                enf.start();

            try{
                for (var enf : enfrentamientos){
                    enf.join();
                    finalistas.add(enf.getGanadorFinal());
                }

                new Enfrentamiento(finalistas.get(0), finalistas.get(0)).start();

            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        };
    }

    public void agregarFinalista(Jugador jugador){
        this.finalistas.add(jugador);
    }
}
