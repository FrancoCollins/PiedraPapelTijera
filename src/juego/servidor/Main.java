package juego.servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static final int PUERTO = 1043;

    public static void main(String[] args) {
        TournamentManager manejadorTorneos = new TournamentManager();
        SignalManager signalManager = new SignalManager(manejadorTorneos);
        ClientConnection clientConnection = new ClientConnection(manejadorTorneos, signalManager);
        Socket socketAlCliente = null;
        InetSocketAddress direccion = new InetSocketAddress(PUERTO);
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(direccion);
            do {
                socketAlCliente = serverSocket.accept();
                clientConnection.conectarCliente(socketAlCliente);
            } while (true);
        } catch (IOException e) {
            System.err.println("SERVIDOR: Error de entrada/salida");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("SERVIDOR: Error -> " + e);
            e.printStackTrace();
        }
    }
}
