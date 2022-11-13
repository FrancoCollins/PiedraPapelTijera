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
    public void manage(Jugador jugador, Scanner reader, PrintStream writer) {
        new Thread(() -> {
            boolean continuar = false;
            do {
                String resultado_str = reader.nextLine();
                int senal = Senal.ERROR;

                senal = Integer.parseInt(resultado_str);
                System.out.println("Senal recibida en el servidor: " +senal);


                continuar = switch (senal) {
                    case Senal.CREAR_TORNEO_PUBLICO               -> manejarCrearTorneo(jugador, reader, writer, false);
                    case Senal.CREAR_TORNEO_PRIVADO               -> manejarCrearTorneo(jugador, reader, writer, true);
                    case Senal.UNIRSE_TORNEO,
                            Senal.INGRESAR_CODIGO_PARTIDA_PRIVADA -> manejarUnirseTorneo(jugador, reader, writer);
                    case Senal.SOLICITAR_LISTA_TORNEOS            -> manejarListaTorneos(writer);
                    default -> false;

                };

            } while (continuar);
        }).start();
    }


    private boolean manejarListaTorneos(PrintStream writer) {
        HashMap<String, Torneo> torneosPublicos = tournamentManager.mostrarTorneos();
        writer.println(Senal.LISTA_TORNEOS);
        writer.println(torneosPublicos.size());
        torneosPublicos.forEach((key, value) -> {
            String nombre = value.getNombreTorneo();
            String jugadores = value.getJugadores().size() + " / " + value.getMAX_PLAYERS();

            writer.println(
                    nombre + Senal.SEPARADOR +
                    jugadores + Senal.SEPARADOR +
                    key);
        });

        return true;
    }


    private boolean manejarUnirseTorneo(Jugador jugador, Scanner reader, PrintStream writer) {
        String clave = reader.nextLine();

        int resultado = tournamentManager.unirJugadorATorneo(jugador, clave);

        System.out.println("Clave: " + clave);
        System.out.println("Resultado: " + resultado);
        writer.println(resultado);

        return resultado != Senal.UNION_EXITOSA_TORNEO;
    }

    /**
     * Crea un torneo y devuelve la clave para entrar a ese torneo (Solo si el usuario manda su nombre).
     * No unimos al jugador directamente porque no le hemos preguntado su nombre.
     * @param jugador Jugador
     * @param reader Scanner
     * @param writter PrintStream
     * @param esPrivado Si el torneo es privado o no
     * @return Clave para unirse al torneo.
     */
    private boolean manejarCrearTorneo(Jugador jugador, Scanner reader, PrintStream writter, boolean esPrivado) {
        String nombreDelTorneo = reader.nextLine();
        int cantidadJugadores = Integer.parseInt(reader.nextLine());
        System.out.println("Datos:" + nombreDelTorneo + " " + cantidadJugadores);

        writter.println(Senal.ENVIAR_NOMBRE);
        String nombre = reader.nextLine();

        jugador.nombreDeUsuario = nombre;

        Torneo torneo = new Torneo(esPrivado, nombreDelTorneo, cantidadJugadores);
        String clave = tournamentManager.agregarTorneo(jugador, torneo);
        tournamentManager.unirJugadorATorneo(jugador, clave);

        writter.println(Senal.CLAVE_TORNEO);
        writter.println(clave);

        writter.println(Senal.CONEXION_EXITOSA_TORNEO);

        return true;
    }
}
