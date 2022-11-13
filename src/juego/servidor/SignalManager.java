package juego.servidor;

import juego.Senal;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SignalManager {

    private ArrayList<Jugador> jugadoresLobby;

    private ArrayList<Jugador> jugadoresEnPartida;

    private TournamentManager tournamentManager;

    public SignalManager(TournamentManager manager) {
        this.tournamentManager = manager;
    }


    /**
     * Empieza un nuevo hilo para este jugador que se encarga
     * de manejar las posibles peticiones que tenga el cliente
     * hasta que se conecte a un torneo o se desconecte.
     *
     * @param jugador jugador a manejar
     */
    public void manage(Jugador jugador) {
        new Thread(() -> {

            Scanner reader = null;
            PrintStream writer = null;

            try {
                reader = new Scanner(jugador.socket.getInputStream());
                writer = new PrintStream(jugador.socket.getOutputStream());
            } catch (Exception e) {
                // Cliente desconectado
                return;
            }

            do {
                String resultado_str = reader.nextLine();
                int senal = Senal.ERROR;

                senal = Integer.parseInt(resultado_str);


                switch (senal) {
                    case Senal.CREAR_TORNEO_PUBLICO:            manejarCrearTorneo(jugador, reader, false);     break;
                    case Senal.CREAR_TORNEO_PRIVADO:            manejarCrearTorneo(jugador, reader, true);      break;
                    case Senal.UNIRSE_TORNEO:
                    case Senal.INGRESAR_CODIGO_PARTIDA_PRIVADA: manejarUnirseTorneo(jugador, reader, writer);           break;
                    case Senal.SOLICITAR_LISTA_TORNEOS:         manejarListaTorneos(writer);                            break;
                }
            } while (true);
        }).start();
    }


    private void manejarListaTorneos(PrintStream writer) {
        HashMap<String, Torneo> torneosPublicos = tournamentManager.mostrarTorneos();
        writer.println(Senal.LISTA_TORNEOS);
        writer.println(torneosPublicos.size());
        torneosPublicos.forEach((key, value) -> {
            writer.println(value.getNombreTorneo()+Senal.SEPARADOR+value.getJugadores().size()+Senal.SEPARADOR+key+Senal.SEPARADOR+value.getMAX_PLAYERS());
        });
    }


    private void manejarUnirseTorneo(Jugador jugador, Scanner reader, PrintStream writer) {
        String clave = reader.nextLine();
        int resultado = tournamentManager.unirJugadorATorneo(jugador, clave);
        writer.println(resultado);
    }

    private void manejarCrearTorneo(Jugador jugador, Scanner reader, boolean esPrivado) {
        String nombreDelTorneo = reader.nextLine();
        int cantidadJugadores = Integer.parseInt(reader.nextLine());
        Torneo torneo = new Torneo(esPrivado, nombreDelTorneo, cantidadJugadores);
        tournamentManager.agregarTorneo(jugador, torneo);
    }
}
