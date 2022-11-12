package juego.servidor;

import juego.Senal;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ClientConnection {

    private TournamentManager tournamentManager;

    private SignalManager signalManager;

    public ClientConnection(TournamentManager tournamentManager, SignalManager signalManager){
        this.tournamentManager = tournamentManager;
        this.signalManager = signalManager;
    }


    public void conectarCliente(Socket clientSocket) {
        new Thread( () -> {
            InputStreamReader entrada = null;
            PrintStream salida = null;

            try {
                entrada = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader bf = new BufferedReader(entrada);
                salida = new PrintStream(clientSocket.getOutputStream());


                int senalConectarse = Integer.parseInt(bf.readLine());
                if (senalConectarse == Senal.CONECTARSE)
                    salida.println(Senal.CONEXION_EXITOSA);
                else {
                    clientSocket.close();
                    return;
                }

                String nombreJugador = bf.readLine();

                System.out.println("SERVIDOR: Jugador " + nombreJugador + " se ha unido.");

                Jugador jugador = new Jugador(clientSocket, nombreJugador);

                signalManager.manage(jugador);

            } catch (IOException e) {
                System.err.println("SERVIDOR: Error de entrada/salida");
                e.printStackTrace();
            } catch (NoSuchElementException e){
                // Cliente desconectado
            } catch (Exception e) {
                System.err.println("SERVIDOR: Error -> " + e);
                e.printStackTrace();
            }
        }).start();
    }
}