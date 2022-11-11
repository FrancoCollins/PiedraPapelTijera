package juego.servidor;

import juego.Acciones;

import java.io.*;
import java.security.KeyPair;
import java.util.Scanner;

public class Enfrentamiento extends Thread {

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

    public void enviarPaqueteAJugador(Jugador jugador, String paquete) {
        try {
            OutputStream stream = jugador.socket.getOutputStream();
            PrintStream pr = new PrintStream(stream);
            pr.println(paquete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarPaqueteAJugadores(String paquete) {
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

    public int[] recibirAcciones(int senal1, int senal2) {
        senal1 = recibirAccionJugador(p1);
        senal2 = recibirAccionJugador(p2);

        return new int[] {senal1, senal2};
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

            int senal1 = Acciones.ERROR, senal2 = Acciones.ERROR;

            int[] senales = recibirAcciones(senal1, senal2);
            senal1 = senales[0];
            senal2 = senales[1];

            System.out.println("Senal 1: " + senal1);
            System.out.println("Senal 2: " + senal2);

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



            if (ganador == null) {
                 enviarSenalAJugadores(Acciones.EMPATE);
            } else {
                if (ganador == p1) {
                    enviarSenalAJugador(p1, Acciones.GANADOR_DE_RONDA);
                    enviarSenalAJugador(p2, Acciones.PERDEDOR_DE_RONDA);
                    puntajeP1++;
                } else {
                    enviarSenalAJugador(p2, Acciones.GANADOR_DE_RONDA);
                    enviarSenalAJugador(p1, Acciones.PERDEDOR_DE_RONDA);
                    puntajeP2++;
                }
            }

            System.out.println("" + puntajeP1 + "" + Acciones.SEPARADOR + "" + puntajeP2);

            enviarPaqueteAJugador(p1, "" + puntajeP1 + "" + Acciones.SEPARADOR + "" + puntajeP2);
            enviarPaqueteAJugador(p2, "" + puntajeP2 + "" + Acciones.SEPARADOR + "" + puntajeP1);

        }



        if (puntajeP1 == 3) {
            ganadorFinal = p1;
            enviarSenalAJugador(p1, Acciones.GANADOR_DE_ENFRENTAMIENTO);
            enviarSenalAJugador(p2, Acciones.PERDEDOR_DE_ENFRENTAMIENTO);
        } else {
            ganadorFinal = p2;
            enviarSenalAJugador(p2, Acciones.GANADOR_DE_ENFRENTAMIENTO);
            enviarSenalAJugador(p1, Acciones.PERDEDOR_DE_ENFRENTAMIENTO);
        }

        enviarPaqueteAJugadores(ganadorFinal.nombreDeUsuario);

    }

    public Jugador getGanadorFinal(){ return ganadorFinal;}
}

