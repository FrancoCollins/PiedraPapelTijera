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
        try {
            Jugador jugador = new Jugador(clientSocket);


            int senalConectarse = Integer.parseInt(jugador.reader.nextLine());
            if (senalConectarse == Senal.CONECTARSE)
                jugador.writter.println(Senal.CONEXION_EXITOSA);
            else {
                clientSocket.close();
                return;
            }

            System.out.println("SERVIDOR: Jugador con IP " + jugador.socket.getInetAddress().getHostName() + " se ha conectado.");
            signalManager.manage(jugador);

        } catch (IOException e) {
            System.err.println("SERVIDOR: Error de entrada/salida");
            e.printStackTrace();
        } catch (NoSuchElementException e){
            System.out.println("SERVIDOR: Cliente desconectado.");
        } catch (Exception e) {
            System.err.println("SERVIDOR: Error -> " + e);
            e.printStackTrace();
        }
    }
}