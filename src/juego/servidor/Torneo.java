package juego.servidor;

import juego.Senal;

import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Torneo {

    private boolean privado;
    public final int MAX_PLAYERS;

    private ArrayList<Enfrentamiento> enfrentamientos;

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    private ArrayList<Jugador> jugadores;

    private ArrayList<Jugador> finalistas;

    private Jugador ganador;

    private String nombreTorneo;

    public Torneo(boolean esPrivado, int max_players) {
        MAX_PLAYERS = max_players;
        jugadores = new ArrayList<>();
        enfrentamientos = new ArrayList<>();
        finalistas = new ArrayList<>();
        this.privado = esPrivado;
    }

    public Torneo(boolean esPrivado, String nombreTorneo, int max_players) {
        jugadores = new ArrayList<>();
        enfrentamientos = new ArrayList<>();
        finalistas = new ArrayList<>();
        this.privado = esPrivado;
        this.nombreTorneo = nombreTorneo;
        MAX_PLAYERS = max_players;
    }

    public void enviarPaqueteAJugador(Jugador jugador, String paquete) {
        jugador.writter.println(paquete);
    }

    public void enviarPaqueteAJugadores(Jugador p1, Jugador p2, String paquete) {
        enviarPaqueteAJugador(p1, paquete);
        enviarPaqueteAJugador(p2, paquete);
    }

    public void enviarSenalAJugador(Jugador jugador, int senal) {
        jugador.writter.println(senal);
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
        } catch (NumberFormatException e){
            return Senal.DESCONECTADO;
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
        if (jugadores.size() == MAX_PLAYERS) {
            enviarSenalAJugador(jugador, Senal.LOBBY_LLENO);
            try {
                jugador.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        this.jugadores.add(jugador);
        enviarSenalAJugador(jugador, Senal.CONEXION_EXITOSA_TORNEO);
        enviarJugadoresEnLobby();

        if (jugadores.size() == MAX_PLAYERS) {
            System.out.println("Comienza el torneo!");

            for (Jugador j : jugadores) {
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
                    e.printStackTrace();
                }
                jugadores.remove(jugador);
            }

            System.out.println("El jugador " + jugador.nombreDeUsuario + " dijo: " + senal);
        };
    }

    public void enviarJugadoresEnLobby() {
        for (Jugador p : jugadores) {
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

                for (Jugador jugador : jugadores) {
                    enviarSenalAJugador(jugador, Senal.JUGADORES_EN_LOBBY);
                    enviarPaqueteAJugador(jugador, jugadores.size() + "/" + MAX_PLAYERS);
                }

                if (jugadores.size() == MAX_PLAYERS) {
                    for (int i = 0; i <= MAX_PLAYERS / 2; i += 2)
                        enfrentamientos.add(new Enfrentamiento(jugadores.get(i), jugadores.get(i + 1), this));

                    new Thread(comenzar()).start();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }


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
                e.printStackTrace();
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
            do {
                try {
                    senal = Integer.parseInt(jugador.reader.nextLine());
                } catch (NumberFormatException e ) {
                    senal = Senal.SELECCION_INCORRECTA;
                    enviarSenalAJugador(jugador, senal);
                    e.printStackTrace();
                } catch (NoSuchElementException e){
                    // Si se desconecta
                    senal = Senal.NO;
                }
            } while (senal != Senal.NO && senal != Senal.SI);

            espera.interrupt();
        }

        public int getSenal() {
            return senal;
        }
    }

    public boolean isPrivado() {
        return privado;
    }

    public String getNombreTorneo() {
        return nombreTorneo;
    }

    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public int getMAX_PLAYERS() {
        return MAX_PLAYERS;
    }

    public void setPrivado(boolean privado) {
        this.privado = privado;
    }
}
