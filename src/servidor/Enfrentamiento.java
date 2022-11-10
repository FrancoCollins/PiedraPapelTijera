package servidor;

import java.io.IOException;
import java.io.PrintWriter;

public class Enfrentamiento implements Runnable{

    private Jugador p1;
    private Jugador p2;

    private int puntajeP1;
    private int puntajeP2;

    private Jugador jugadorEnEspera;

    public Enfrentamiento(Jugador p1, Jugador p2){
        this.p1 = p1;
        this.p2 = p2;
        this.jugadorEnEspera = null;
    }

    public boolean empiezaElPrimero(){
        return Math.random() > 0.4;
    }

    @Override
    public void run(){
        jugadorEnEspera = empiezaElPrimero() ? p2 : p1;


        while(puntajeP1 < 3 && puntajeP2 < 3){
            try{
                var stream = p1.socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(stream);

                printWriter.print("");


            } catch (IOException e){

            }


        }
    }
}
