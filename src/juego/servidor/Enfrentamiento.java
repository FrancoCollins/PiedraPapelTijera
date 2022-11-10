package juego.servidor;

import juego.Acciones;

import java.io.*;
import java.util.Scanner;

public class Enfrentamiento implements Runnable {

    private Jugador p1;
    private Jugador p2;

    private Jugador ganadorFinal;

    private int puntajeP1;
    private int puntajeP2;


    public Enfrentamiento(Jugador p1, Jugador p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.ganadorFinal = null;
    }

    public void enviarSenalAJugador(Jugador jugador, int senal) {
        try {
            OutputStream stream = jugador.socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(stream);

            printWriter.println(senal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarSenalAJugadores(int senal) {
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

        return Acciones.ERROR;
    }

    public void recibirAcciones(Integer senal1, Integer senal2) {
        senal1 = recibirAccionJugador(p1);
        senal2 = recibirAccionJugador(p2);
    }

    public boolean esError(int senal1, int senal2) {
        return senal1 == Acciones.ERROR || senal2 == Acciones.ERROR;
    }

    public boolean seleccionEsIncorrecta(int senal1, int senal2) {
        if (senal1 != Acciones.PIEDRA && senal1 != Acciones.PAPEL && senal1 != Acciones.TIJERA)
            return true;

        if (senal2 != Acciones.PIEDRA && senal2 != Acciones.PAPEL && senal2 != Acciones.TIJERA)
            return true;

        return false;
    }

    public Jugador ganadorRonda(int senal1, int senal2) {

        // empate
        if (senal1 == senal2)
            return null;

        else if (senal1 == Acciones.PIEDRA)
            return senal2 == Acciones.PAPEL ? p2 : p1;

        else if (senal1 == Acciones.PAPEL)
            return senal2 == Acciones.TIJERA ? p2 : p1;

        else
            return senal1 == Acciones.TIJERA ? p2 : p1;
    }

    @Override
    public void run() {
        System.out.println("Comienza la partida!");
        while (puntajeP1 < 3 && puntajeP2 < 3) {
            enviarSenalAJugadores(Acciones.ENVIAR_SELECCION);

            Integer senal1 = Acciones.ERROR, senal2 = Acciones.ERROR;
            recibirAcciones(senal1, senal2);

            do {
                if (senal1 == Acciones.ERROR) {
                    enviarSenalAJugador(p1, Acciones.SELECCION_INCORRECTA);
                    senal1 = recibirAccionJugador(p1);
                }

                if (senal2 == Acciones.ERROR) {
                    enviarSenalAJugador(p2, Acciones.SELECCION_INCORRECTA);
                    senal2 = recibirAccionJugador(p2);
                }
            } while (seleccionEsIncorrecta(senal1, senal2) || esError(senal1, senal2));

            Jugador ganador = ganadorRonda(senal1, senal2);

            int senalResultado;


            if (ganador == null) {
                senalResultado = Acciones.EMPATE;
            } else {
                if (ganador == p1) {
                    enviarSenalAJugador(p1, Acciones.GANADOR_DE_RONDA);
                    enviarSenalAJugador(p2, Acciones.PERDEDOR_DE_RONDA);
                    puntajeP1++;
                    enviarSenalAJugadores(Acciones.FINAL_DE_RONDA);
                } else {
                    enviarSenalAJugador(p2, Acciones.GANADOR_DE_RONDA);
                    enviarSenalAJugador(p2, Acciones.PERDEDOR_DE_RONDA);
                    puntajeP2++;
                    enviarSenalAJugadores(Acciones.FINAL_DE_RONDA);
                }
            }
        }

        // Termina el enfrentamiento
        enviarSenalAJugadores(Acciones.FINAL_DE_ENFRENTAMIENTO);

        if (puntajeP1 == 3) {
            ganadorFinal = p1;
            enviarSenalAJugador(p1, Acciones.GANADOR_DE_ENFRENTAMIENTO);
            enviarSenalAJugador(p2, Acciones.PERDEDOR_DE_ENFRENTAMIENTO);
        } else {
            ganadorFinal = p2;
            enviarSenalAJugador(p2, Acciones.GANADOR_DE_ENFRENTAMIENTO);
            enviarSenalAJugador(p1, Acciones.PERDEDOR_DE_ENFRENTAMIENTO);
        }
    }

    public Jugador getGanadorFinal(){ return ganadorFinal;}
}

