package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorJuego implements Runnable {

    public static final int PUERTO = 2017;

    public Torneo torneo;

    public ServidorJuego(Torneo torneo){
        this.torneo = torneo;
    }

    @Override
    public void run() {
        InputStreamReader entrada = null;
        PrintStream salida = null;
        Socket socketAlCliente = null;
        InetSocketAddress direccion = new InetSocketAddress(PUERTO);

        try (ServerSocket serverSocket = new ServerSocket()){

            serverSocket.bind(direccion);
            int numJugadores = 0;
                socketAlCliente = serverSocket.accept();
                entrada = new InputStreamReader(socketAlCliente.getInputStream());
                BufferedReader bf = new BufferedReader(entrada);


                String nombreJugador = bf.readLine();

                System.out.println("SERVIDOR: Jugador " + nombreJugador + " se ha unido.");

                salida = new PrintStream(socketAlCliente.getOutputStream());
                salida.println("Te has conectado.");
                salida.println("Jugadores en el lobby: " + numJugadores);

                numJugadores++;

                Jugador jugador = new Jugador(socketAlCliente, nombreJugador, torneo);
                torneo.agregarJugador(jugador);
        } catch (IOException e) {
            System.err.println("SERVIDOR: Error de entrada/salida");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("SERVIDOR: Error -> " + e);
            e.printStackTrace();
        }
    }
}