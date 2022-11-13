package juego.servidor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Jugador{
    public String nombreDeUsuario;
    public Torneo torneo;
    public Socket socket;
    public Scanner reader;
    public PrintStream writter;

    public int victorias;
    public int derrotas;

    public Jugador(Socket socket) throws IOException{
        this.socket = socket;
        this.reader = new Scanner(socket.getInputStream());
        this.writter = new PrintStream(socket.getOutputStream());
    }

}
