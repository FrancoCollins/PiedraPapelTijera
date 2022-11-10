package juego.servidor;

import java.net.InetAddress;
import java.net.Socket;

public class Jugador{
    public String nombreDeUsuario;
    public Torneo torneo;
    public Enfrentamiento enfrentamiento;
    public InetAddress address;
    public Socket socket;
    public int victorias;
    public int derrotas;

    public Jugador(Socket socket, String nombreDeUsuario, Torneo torneo){
        this.socket = socket;
        this.nombreDeUsuario = nombreDeUsuario;
        this.torneo = torneo;
    }

}
