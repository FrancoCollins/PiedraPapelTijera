package juego.servidor;

import juego.Senal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ClientConection extends Thread {

    public static int numJugadores = 0;

    public Torneo torneo;
    private Socket clientSocket;

    public ClientConection(Socket clientSocket, Torneo torneo) {
        this.torneo = torneo;
        this.clientSocket = clientSocket;

    }

    @Override
    public void run() {
        InputStreamReader entrada = null;
        PrintStream salida = null;

        try {
            entrada = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader bf = new BufferedReader(entrada);
            salida = new PrintStream(clientSocket.getOutputStream());

            salida.println(Senal.CONEXION_EXITOSA);

            String nombreJugador = bf.readLine();

            System.out.println("SERVIDOR: Jugador " + nombreJugador + " se ha unido.");


            numJugadores++;

            Jugador jugador = new Jugador(clientSocket, nombreJugador, torneo);
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