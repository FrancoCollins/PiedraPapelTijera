package juego.servidor;

import juego.Senal;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

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
            boolean continuar = false;
            do {
                try{
                    String resultado_str = jugador.reader.nextLine();
                    int senal = Senal.ERROR;

                    senal = Integer.parseInt(resultado_str);
                    System.out.println("Senal de " + jugador.nombreDeUsuario + " recibida en el servidor: " +senal);


                    continuar = switch (senal) {
                        case Senal.CREAR_TORNEO_PUBLICO               -> manejarCrearTorneo(jugador, false);
                        case Senal.CREAR_TORNEO_PRIVADO               -> manejarCrearTorneo(jugador, true);
                        case Senal.UNIRSE_TORNEO_PUBLICO,
                                Senal.UNIRSE_TORNEO_PRIVADO -> manejarUnirseTorneo(jugador);
                        case Senal.SOLICITAR_LISTA_TORNEOS            -> manejarListaTorneos(jugador.writter);
                        default -> false;

                    };
                } catch (NoSuchElementException e){
                    System.out.println("El jugador con IP " + jugador.socket.getInetAddress().getHostName() + " se ha desconectado.");
                    continuar = false;
                }

            } while (continuar);
        }).start();
    }


    private boolean manejarListaTorneos(PrintStream writer){
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


    private boolean manejarUnirseTorneo(Jugador jugador) throws NoSuchElementException {
        System.out.println("Unirse");
        String clave = jugador.reader.nextLine();

        jugador.writter.println(Senal.ENVIAR_NOMBRE);
        jugador.nombreDeUsuario = jugador.reader.nextLine();

        int resultado = tournamentManager.unirJugadorATorneo(jugador, clave);

        System.out.println("Clave: " + clave);
        System.out.println("Resultado: " + resultado);
        jugador.writter.println(resultado);

        if(resultado == Senal.UNION_EXITOSA_TORNEO){
            jugador.writter.println(Senal.NOMBRE_TORNEO);
            jugador.writter.println(tournamentManager.obtenerTorneo(clave).getNombreTorneo());

        }

        return resultado != Senal.UNION_EXITOSA_TORNEO;
    }

    /**
     * Crea un torneo y devuelve la clave para entrar a ese torneo (Solo si el usuario manda su nombre).
     * No unimos al jugador directamente porque no le hemos preguntado su nombre.
     * @param jugador Jugador
     * @param esPrivado Si el torneo es privado o no
     * @return Clave para unirse al torneo.
     */
    private boolean manejarCrearTorneo(Jugador jugador, boolean esPrivado) throws NoSuchElementException {
        String nombreDelTorneo = jugador.reader.nextLine();
        int cantidadJugadores = Integer.parseInt(jugador.reader.nextLine());
        System.out.println("Datos:" + nombreDelTorneo + " " + cantidadJugadores);

        jugador.writter.println(Senal.ENVIAR_NOMBRE);
        jugador.nombreDeUsuario = jugador.reader.nextLine();

        Torneo torneo = new Torneo(esPrivado, nombreDelTorneo, cantidadJugadores);
        String clave = tournamentManager.agregarTorneo(jugador, torneo);
        tournamentManager.unirJugadorATorneo(jugador, clave);

        if(esPrivado){
            jugador.writter.println(Senal.CLAVE_TORNEO);
            jugador.writter.println(clave);
        }

        jugador.writter.println(Senal.CONEXION_EXITOSA_TORNEO);

        return false;
    }
}
