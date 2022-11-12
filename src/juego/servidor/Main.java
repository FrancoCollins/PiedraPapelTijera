package juego.servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static final int PUERTO = 1043;

    public static void main(String[] args) {
        Torneo torneo = new Torneo();
        Socket socketAlCliente = null;
        InetSocketAddress direccion = new InetSocketAddress(PUERTO);
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(direccion);
            do {
                socketAlCliente = serverSocket.accept();
                new Thread(new ClientConnection(socketAlCliente, torneo)).start();
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
