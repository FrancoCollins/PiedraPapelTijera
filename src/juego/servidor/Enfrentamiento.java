package juego.servidor;

import juego.Acciones;

import java.io.IOException;
import java.io.PrintWriter;

public class Enfrentamiento implements Runnable {

    private Jugador p1;
    private Jugador p2;

    private int puntajeP1;
    private int puntajeP2;

    private Jugador jugadorEnEspera;

    public Enfrentamiento(Jugador p1, Jugador p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.jugadorEnEspera = null;
    }

    public void enviarSenalAJugador(Jugador jugador, int senal) {
        try {
            OutputStream stream = jugador.socket.getOutputStream();
            PrintWriter printWriter = new PrintWriter(stream);

            printWriter.println(Acciones.ENVIAR_SELECCION);
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
            Scanner sc = new Scanner(stream);
            return Integer.parseInt(sc.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Acciones.ERROR;
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


        }
    }
}
