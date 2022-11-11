package juego.servidor;

import juego.Senal;

import java.io.*;
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

    public void enviarPaqueteAJugador(Jugador jugador, String paquete) {
        try {
            OutputStream stream = jugador.socket.getOutputStream();
            PrintStream pr = new PrintStream(stream);
            pr.println(paquete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarPaqueteAJugadores(Jugador p1, Jugador p2, String paquete) {
        enviarPaqueteAJugador(p1, paquete);
        enviarPaqueteAJugador(p2, paquete);
    }

    public void enviarSenalAJugador(Jugador jugador, int senal) {
        try {
            OutputStream stream = jugador.socket.getOutputStream();
            PrintStream pr = new PrintStream(stream);
            pr.println(senal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarSenalAJugadores(Jugador p1, Jugador p2, int senal) {
        enviarSenalAJugador(p1, senal);
        enviarSenalAJugador(p2, senal);
    }

    public int recibirAccionJugador(Jugador jugador) {
        try {

            InputStream stream = jugador.socket.getInputStream();
            BufferedReader sc = new BufferedReader(new InputStreamReader(stream));
            return Integer.parseInt(sc.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Senal.ERROR;
    }

    public int[] recibirAcciones(Jugador p1, Jugador p2, int senal1, int senal2) {
        senal1 = recibirAccionJugador(p1);
        senal2 = recibirAccionJugador(p2);

        return new int[] {senal1, senal2};
    }

    public boolean esError(int senal1, int senal2) {
        return senal1 == Senal.ERROR || senal2 == Senal.ERROR;
    }

    public synchronized void agregarJugador(Jugador jugador, ClientConection conection) {
        this.jugadores.add(jugador);

        if (jugadores.size() == MAX_PLAYERS) {
            System.out.println("Comienza el torneo!");
            for (int i = 0; i <= MAX_PLAYERS / 2; i += 2) {
                enfrentamientos.add(new Enfrentamiento(jugadores.get(i), jugadores.get(i + 1), this));
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

                Final finale = new Final(finalistas.get(0), finalistas.get(1), this);
                finale.start();

                finale.join();
                ganador = finale.getGanadorFinal();

                for (Jugador jugador : jugadores){
                    enviarSenalAJugador(jugador, Senal.NOMBRE_GANADOR_DEL_TORNEO);
                    enviarPaqueteAJugador(jugador, ganador.nombreDeUsuario);
                }

                // Esperar a que todos hayan contestado
                Thread.sleep(10_000);
                enfrentamientos = new ArrayList<>();
                finalistas = new ArrayList<>();

                for (int i = 0; i <= MAX_PLAYERS/2; i += 2) {
                    if(i != jugadores.size())
                        enfrentamientos.add(new Enfrentamiento(jugadores.get(i), jugadores.get(i + 1), this));
                }
                if(jugadores.size() == MAX_PLAYERS)
                    new Thread(comenzar()).start();






            } catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        };
    }

    public void agregarFinalista(Jugador jugador){
        this.finalistas.add(jugador);
    }
}
