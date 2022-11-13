package juego.cliente;

import juego.Senal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SignalManager extends Thread {

    private Scanner reader;

    private PrintStream writer;

    private GameFunctionality game;

    public SignalManager(Scanner reader, PrintStream writer, GameFunctionality game) {
        this.reader = reader;
        this.writer = writer;
        this.game = game;
    }

    @Override
    public void run() {

        do {
            try {
                String resultado_str = reader.nextLine();
                int senal = Senal.ERROR;

                senal = Integer.parseInt(resultado_str);


                switch (senal) {
                    case Senal.ENVIAR_NOMBRE:                       manejarEnviarNombre();              break;
                    case Senal.CONEXION_EXITOSA:                    manejarConexionExitosa();           break;
                    case Senal.ENVIAR_SELECCION:                    manejarEnviarSeleccion();           break;
                    case Senal.GANADOR_DE_RONDA:                    manejarRondaGanada();               break;
                    case Senal.PERDEDOR_DE_RONDA:                   manejarRondaPerdida();              break;
                    case Senal.EMPATE:                              manejarEmpate();                    break;
                    case Senal.GANADOR_DE_ENFRENTAMIENTO:           manejarEnfrentamientoGanado();      break;
                    case Senal.PERDEDOR_DE_ENFRENTAMIENTO:          manejarEnfrentamientoPerdido();     break;
                    case Senal.GANADOR_DE_TORNEO:                   manejarTorneoGanado();              break;
                    case Senal.PAQUETE_PUNTUACION:                  manejarObtenerPuntaje();            break;
                    case Senal.COMENZAR_PARTIDA_FINAL:              manejarComenzarPartidaFinal();      break;
                    case Senal.FINAL_DE_TORNEO:                     manejarFinalDeTorneo();             break;
                    case Senal.NOMBRE_GANADOR_DEL_ENFRENTAMIENTO:   manejarNombreGanadorEnf();          break;
                    case Senal.NOMBRE_GANADOR_DEL_TORNEO:           manejarNombreGanadorTor();          break;
                    case Senal.PREGUNTA_REVANCHA:                   manejarPreguntaRevancha();          break;
                    case Senal.JUGADORES_EN_LOBBY:                  manejarJugadoresEnLobby();          break;
                    case Senal.COMENZAR_TORNEO:                     manejarComenzarTorneo();            break;
                    case Senal.COMENZAR_ENFRENTAMIENTO:             manejarComenzarEnfrentamiento();    break;
                    case Senal.CONEXION_EXITOSA_TORNEO:             manejarConexionExitosaTorneo();     break;
                    case Senal.NOMBRE_DEL_RIVAL:                    manejarNombreDelRival();            break;
                    case Senal.CLAVE_TORNEO:                        manejarClaveTorneo();               break;
                    case Senal.LISTA_TORNEOS:                       manejarListaTorneos();              break;
                    case Senal.LOBBY_LLENO:                         manejarLobbyLleno();                break;
                    case Senal.ERROR:                               manejarError();                     break;
                }

            } catch (NoSuchElementException e) {
                System.out.println("Desconectado del servidor.");
                e.printStackTrace();
                break;
            }

        } while (true);
    }

    private void manejarUnionExitosaTorneo() {
        game.getGraphics().onConexionExitosaTorneo();
    }

    private void manejarClaveTorneo() {
        String clave = reader.nextLine();
        game.getGraphics().getFunctionality().setClaveTorneo(clave);
    }

    public void enviarSenal(int senal) {
        writer.println(senal);
    }

    public void enviarPaquete(String paquete) {
        writer.println(paquete);
    }

    public void enviarSenalDeConexion() {
        game.getGraphics().onConectando();
        writer.println(Senal.CONECTARSE);
    }

    public void manejarConexionExitosa() {
        System.out.println("Te has conectado!");
        game.getGraphics().onConexionExitosa();
    }

    public void manejarRondaGanada() {
        System.out.println("¡Has ganado la ronda!");
        game.getGraphics().getPantallaEnfrentamiento().onRondaGanada();
    }

    public void manejarRondaPerdida() {
        game.getGraphics().getPantallaEnfrentamiento().onRondaPerdida();
    }

    public void manejarEnfrentamientoGanado() {
        game.getGraphics().getPantallaEnfrentamiento().onEnfrentamientoGanado();
        game.actualizarContinuarPartida(false);
    }

    public void manejarEnfrentamientoPerdido() {
        game.getGraphics().getPantallaEnfrentamiento().onEnfrentamientoPerdido();
        game.actualizarContinuarPartida(false);
        game.actualizarContinuarTorneo(false);
    }

    public void manejarTorneoGanado() {
        System.out.println("¡Has ganado el torneo!");
        game.actualizarContinuarPartida(false);
        game.actualizarContinuarTorneo(false);
    }

    public void manejarObtenerPuntaje() {
        // Obtener paquete puntaje

        String paquete = reader.nextLine();
        String[] puntajes_str = paquete.split("\\|");
        int[] puntajes = new int[puntajes_str.length];

        for (int i = 0; i < puntajes.length; i++)
            puntajes[i] = Integer.parseInt(puntajes_str[i]);

        game.getGraphics().getPantallaEnfrentamiento().onObtenerPuntaje(puntajes);
    }

    public void manejarEnviarNombre() {
        game.getGraphics().onEnviarNombre();
    }

    public void manejarFinalDeTorneo() {
        System.out.println("Terminó el torneo señores.");
        game.actualizarContinuarTorneo(false);
    }

    public void manejarComenzarPartidaFinal() {
        System.out.println("Has llegado a la final!");
        game.getGraphics().getPantallaEnfrentamiento().onEmpezarFinal();
    }

    public void manejarNombreGanadorEnf() {
        String ganador = reader.nextLine();
        System.out.println("El ganador del enfrentamiento es: " + ganador);
    }

    public void manejarNombreGanadorTor() {
        String ganador = reader.nextLine();
        System.out.println("El ganador del torneo es: " + ganador);
    }

    // Envía paquete
    public void manejarPreguntaRevancha() {

        Scanner sc = new Scanner(System.in);
        int senal = Senal.ERROR;
        do {
            System.out.println("El torneo ha terminado. Deseas volver a jugar?");
            System.out.print("Selección (S/n): ");

            try {
                String str = sc.nextLine();

                if (str.equalsIgnoreCase("S"))
                    senal = Senal.SI;
                else if (str.equalsIgnoreCase("N"))
                    senal = Senal.NO;

            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduzca una opción válida.");
            }
        } while (senal == Senal.ERROR);

        writer.println(senal);
    }

    public void manejarError() {
        System.out.println("Hubo un error.");
    }

    public void manejarJugadoresEnLobby() {
        String jugadores = reader.nextLine();
        game.getGraphics().onJugadoresEnLobby(jugadores);
        System.out.println("Jugadores en lobby: " + jugadores);
    }

    public void manejarComenzarEnfrentamiento() {
        System.out.println("Comienza el enfrentamiento!");
    }

    public void manejarComenzarTorneo() {
        System.out.println("Comienza el torneo!");
    }

    // Envía paquete
    public void manejarEnviarSeleccion() {
        game.getGraphics().onEnviarSeleccion();
    }

    private void manejarConexionExitosaTorneo() {
        game.getGraphics().onConexionExitosaTorneo();
    }

    private void manejarLobbyLleno() {
        System.out.println("El lobby se encuentra lleno en este momento, espere unos minutos para volver a ingresar");
    }

    private void manejarNombreDelRival() {
        String nombre = reader.nextLine();
        game.getGraphics().getPantallaEnfrentamiento().onNombreDelRival(nombre);
    }


    private void manejarListaTorneos() {
        int torneos = Integer.parseInt(reader.nextLine());
        System.out.println("Numero de torneos: " + torneos);

        String[] header = {"Nombre", "Cantidad de jugadores", "Clave"};
        DefaultTableModel modelo = new DefaultTableModel(header, 0);

        for (int i = 0; i < torneos; i++) {
            String datos = reader.nextLine();
            String[] datosSplit = datos.split("\\|");
            modelo.addRow(datosSplit);
            System.out.println("Datos recibidos: " + datos);
        }

        JTable table = new JTable(modelo);
        table.getColumnModel().getColumn(2).setMaxWidth(0); // Ocultar columna de clave
        game.getGraphics().getPantallaUnirseTorneo().onListaTorneoRecibida(table);

    }


    private void manejarEmpate() {
        game.getGraphics().getPantallaEnfrentamiento().onEmpate();
    }


}
