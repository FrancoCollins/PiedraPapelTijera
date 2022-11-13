package juego.servidor;

import juego.Senal;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientConnection {

    private TournamentManager tournamentManager;

    private SignalManager signalManager;

    public ClientConnection(TournamentManager tournamentManager, SignalManager signalManager){
        this.tournamentManager = tournamentManager;
        this.signalManager = signalManager;
    }


    public void conectarCliente(Socket clientSocket) {
        Scanner entrada = null;
        PrintStream salida = null;

        try {
            entrada = new Scanner(new InputStreamReader(clientSocket.getInputStream()));
            salida = new PrintStream(clientSocket.getOutputStream());


            int senalConectarse = Integer.parseInt(entrada.nextLine());
            if (senalConectarse == Senal.CONECTARSE)
                salida.println(Senal.CONEXION_EXITOSA);
            else {
                clientSocket.close();
                return;
            }

            Jugador jugador = new Jugador(clientSocket);
            System.out.println("SERVIDOR: Jugador con IP " + jugador.socket.getInetAddress().getHostName() + " se ha conectado.");
            signalManager.manage(jugador, entrada, salida);

        } catch (IOException e) {
            System.err.println("SERVIDOR: Error de entrada/salida");
            e.printStackTrace();
        } catch (NoSuchElementException e){
            System.out.println("SERVIDOR: Cliente desconectado.");
            e.printStackTrace();
            // Cliente desconectado
        } catch (Exception e) {
            System.err.println("SERVIDOR: Error -> " + e);
            e.printStackTrace();
        }
    }
}