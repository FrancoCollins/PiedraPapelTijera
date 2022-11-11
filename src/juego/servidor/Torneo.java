package juego.servidor;

import juego.Senal;

import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

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

        return new int[]{senal1, senal2};
    }

    public boolean esError(int senal1, int senal2) {
        return senal1 == Senal.ERROR || senal2 == Senal.ERROR;
    }

    public synchronized void agregarJugador(Jugador jugador) {
        this.jugadores.add(jugador);
        enviarJugadoresEnLobby();

        if (jugadores.size() == MAX_PLAYERS) {
            System.out.println("Comienza el torneo!");

            for (Jugador j : jugadores){
                enviarSenalAJugador(j, Senal.COMENZAR_TORNEO);
            }

            for (int i = 0; i <= MAX_PLAYERS / 2; i += 2) {
                enfrentamientos.add(new Enfrentamiento(jugadores.get(i), jugadores.get(i + 1), this));
            }
            new Thread(comenzar()).start();
        }


    }

    /**
     * Pregunta a los jugadores si quieren jugar otra vez enviando
     * un paquete y recibiendo la respuesta.
     * Tiene un timeout de 15 segundos.
     *
     * @param jugador
     * @return
     */
    public Runnable preguntarRevancha(Jugador jugador) {
        return () -> {
            enviarSenalAJugador(jugador, Senal.PREGUNTA_REVANCHA);

            HiloDeEspera espera = new HiloDeEspera();
            HiloRevancha revancha = new HiloRevancha(jugador);

            espera.setRevancha(revancha);
            revancha.setEspera(espera);

            revancha.start();
            espera.start();

            try {
                revancha.join();
                espera.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int senal = revancha.getSenal();

            if (senal == Senal.NO) {
                try {
                    jugador.socket.close();
                } catch (IOException e) {

                }
                jugadores.remove(jugador);
            }

            System.out.println("El jugador " + jugador.nombreDeUsuario + " dijo: " + senal);
        };
    }

    public void enviarJugadoresEnLobby(){
        for (Jugador p : jugadores){
            enviarSenalAJugador(p, Senal.JUGADORES_EN_LOBBY);
            enviarPaqueteAJugador(p, jugadores.size() + "/" + MAX_PLAYERS);
        }
    }

    public Runnable comenzar() {
        return () -> {
            for (var enf : enfrentamientos)
                enf.start();

            try {
                for (var enf : enfrentamientos) {
                    enf.join();
                    finalistas.add(enf.getGanadorFinal());
                }

                Final finale = new Final(finalistas.get(0), finalistas.get(1), this);
                finale.start();

                finale.join();
                ganador = finale.getGanadorFinal();

                for (Jugador jugador : jugadores) {
                    enviarSenalAJugador(jugador, Senal.NOMBRE_GANADOR_DEL_TORNEO);
                    enviarPaqueteAJugador(jugador, ganador.nombreDeUsuario);
                    new Thread(preguntarRevancha(jugador)).start();
                }

                // Esperar a que todos hayan contestado
                Thread.sleep(20_000);
                enfrentamientos = new ArrayList<>();
                finalistas = new ArrayList<>();

                if (jugadores.size() == MAX_PLAYERS) {
                    for (int i = 0; i <= MAX_PLAYERS / 2; i += 2)
                        enfrentamientos.add(new Enfrentamiento(jugadores.get(i), jugadores.get(i + 1), this));

                    new Thread(comenzar()).start();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

                ;
    }

    ;

    class HiloDeEspera extends Thread {

        private HiloRevancha revancha;

        public void setRevancha(HiloRevancha revancha) {
            this.revancha = revancha;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(10_000);
                revancha.interrupt();
            } catch (InterruptedException e) {
            }
        }
    }

    class HiloRevancha extends Thread {
        private HiloDeEspera espera;
        private Jugador jugador;
        private int senal;

        public HiloRevancha(Jugador jugador) {
            this.jugador = jugador;
            this.senal = Senal.NO;
        }

        public void setEspera(HiloDeEspera espera) {
            this.espera = espera;
        }

        @Override
        public void run() {
            try {
                InputStream stream = jugador.socket.getInputStream();
                Scanner sc = new Scanner(stream);
                do {
                    try {
                        senal = Integer.parseInt(sc.nextLine());
                    } catch (NumberFormatException e) {
                        senal = Senal.SELECCION_INCORRECTA;
                        enviarSenalAJugador(jugador, senal);
                    }
                } while (senal != Senal.NO && senal != Senal.SI);

                espera.interrupt();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchElementException e) {
                System.out.println(jugador.nombreDeUsuario + " se ha desconectado");
            }
        }

        public int getSenal() {
            return senal;
        }
    }
}
