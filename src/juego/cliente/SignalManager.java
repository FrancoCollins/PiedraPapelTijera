package juego.cliente;

import jdk.jfr.Label;
import juego.Senal;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.AccessibleObject;
import java.util.Scanner;

public class SignalManager extends Thread {

    private Scanner reader;

    private PrintStream writer;

    private GameFunctionality game;

    public SignalManager(Scanner reader, PrintStream writer, GameFunctionality game){
        this.reader = reader;
        this.writer = writer;
        this.game = game;
    }

    public void manejarConexionExitosa(){
        System.out.println("Te has conectado!");
        System.out.println("Esperando al resto de jugadores...");
    }

    public void manejarRondaGanada(){
        System.out.println("¡Has ganado la ronda!");
    }

    public void manejarRondaPerdida(){
        System.out.println("Has perdido la ronda.");
    }

    public void manejarEnfrentamientoGanado(){
        System.out.println("¡Has ganado el enfrentamiento!");
        game.actualizarContinuarPartida(false);
    }

    public void manejarEnfrentamientoPerdido(){
        System.out.println("Has perdido el enfrentamiento :(");
        game.actualizarContinuarPartida(false);
        game.actualizarContinuarTorneo(false);
    }

    public void manejarTorneoGanado(){
        System.out.println("¡Has ganado el torneo!");
        game.actualizarContinuarPartida(false);
        game.actualizarContinuarTorneo(false);
    }

    public void manejarObtenerPuntaje(){
        // Obtener paquete puntaje

        String paquete = reader.nextLine();
        String[] puntajes_str = paquete.split("\\|");
        int[] puntajes = new int[puntajes_str.length];

        for (int i = 0; i < puntajes.length; i++)
            puntajes[i] = Integer.parseInt(puntajes_str[i]);

        System.out.println("Tu puntaje: " + puntajes[0]);
        System.out.println("Puntaje del rival: " + puntajes[1]);
    }

    public void manejarEnviarNombre(){
        System.out.println("BIENVENIDO AL JUEGO PIEDRA-PAPEL-TIJERA");
        System.out.print("Introduce tu nombre de usuario: ");
        Scanner sc = new Scanner(System.in);
        String nombre = sc.nextLine();
        writer.println(nombre);
    }

    public void manejarFinalDeTorneo(){
        System.out.println("Terminó el torneo señores.");
        game.actualizarContinuarTorneo(false);
    }

    public void manejarComenzarFinal(){
        System.out.println("Has llegado a la final!");
    }

    public void manejarNombreGanadorEnf(){
        String ganador = reader.nextLine();
        System.out.println("El ganador del enfrentamiento es: " + ganador);
    }

    public void manejarNombreGanadorTor(){
        String ganador = reader.nextLine();
        System.out.println("El ganador del torneo es: " + ganador);
    }

    // Envía paquete
    public void manejarPreguntaRevancha(){

        Scanner sc = new Scanner(System.in);
        int senal = Senal.ERROR;
        do{
            System.out.println("El torneo ha terminado. Deseas volver a jugar?");
            System.out.print("Selección (S/n): ");

            try {
                String str = sc.nextLine();

                if(str.equalsIgnoreCase("S"))
                    senal = Senal.SI;
                else if (str.equalsIgnoreCase("N"))
                    senal = Senal.NO;

            }catch (NumberFormatException e){
                System.out.println("Por favor, introduzca una opción válida.");
            }
        }while(senal == Senal.ERROR);

        writer.println(senal);
    }

    public void manejarError(){
        System.out.println("Hubo un error.");
    }

    // Envía paquete
    public void manejarEnviarSeleccion(){
        System.out.println("Elija una opción:");
        System.out.println("1- Piedra");
        System.out.println("2- Papel");
        System.out.println("3- Tijera");
        System.out.print("Elige: ");

        Scanner scanner = new Scanner(System.in);
        String eleccion_str = scanner.nextLine();
        int eleccion = 0;

        try {
            while(!eleccion_str.matches("[0-9]") || Integer.parseInt(eleccion_str) > 3) {
                System.out.print("Debes introducir un numero: ");
                eleccion_str = scanner.nextLine();
                if(Integer.parseInt(eleccion_str) > 3) {
                    System.out.println("Debes introducir un numero del menu");
                }
            }
        } catch (Exception e) {

        }
        eleccion = Integer.parseInt(eleccion_str);

        eleccion = switch (eleccion) {
            case 1 -> Senal.PIEDRA;
            case 2 -> Senal.PAPEL;
            case 3 -> Senal.TIJERA;
            default -> Senal.ERROR;
        };

        // Envía datos
        writer.println(eleccion);

    }

    @Override
    public void run(){

        do{
            String resultado_str = reader.nextLine();
            int senal = Senal.ERROR;

            try{
                senal = Integer.parseInt(resultado_str);
            } catch (NumberFormatException e){

            }

            switch (senal) {
                case Senal.CONEXION_EXITOSA:            manejarConexionExitosa();
                case Senal.ENVIAR_NOMBRE:               manejarEnviarNombre();          break;
                case Senal.ENVIAR_SELECCION:            manejarEnviarSeleccion();       break;
                case Senal.GANADOR_DE_RONDA:            manejarRondaGanada();           break;
                case Senal.PERDEDOR_DE_RONDA:           manejarRondaPerdida();          break;
                case Senal.GANADOR_DE_ENFRENTAMIENTO:   manejarEnfrentamientoGanado();  break;
                case Senal.PERDEDOR_DE_ENFRENTAMIENTO:  manejarEnfrentamientoPerdido(); break;
                case Senal.GANADOR_DE_TORNEO:           manejarTorneoGanado();          break;
                case Senal.PAQUETE_PUNTUACION:          manejarObtenerPuntaje();        break;
                case Senal.COMENZAR_FINAL:              manejarComenzarFinal();         break;
                case Senal.FINAL_DE_TORNEO:             manejarFinalDeTorneo();         break;
                case Senal.NOMBRE_GANADOR_DEL_ENFRENTAMIENTO: manejarNombreGanadorEnf(); break;
                case Senal.NOMBRE_GANADOR_DEL_TORNEO: manejarNombreGanadorTor(); break;
                case Senal.PREGUNTA_REVANCHA:           manejarPreguntaRevancha();      break;
                case Senal.ERROR:                       manejarError();                 break;
            }
        }while (true);
    }
}
