package juego.cliente;

import juego.Senal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteJuego {
	public static final int PUERTO = 1043;
	public static final String IP_SERVER = "10.34.73.105";

	public static String nombre;


	public static void main(String[] args) {
		System.out.println("        APLICACIÓN CLIENTE         ");
		System.out.println("-----------------------------------");
		System.out.println("BIENVENIDO AL JUEGO PIEDRA-PAPEL-TIJERA");

		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);

		GameFunctionality game = new GameFunctionality();
		try {
			Socket socketAlServidor = new Socket();
			socketAlServidor.connect(direccionServidor);

			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			Scanner sc = new Scanner(entrada);

			SignalManager manager = new SignalManager(sc, salida, game);

			manager.start();
		} catch (UnknownHostException e) {
			System.err.println("CLIENTE: No encuentro el servidor en la dirección" + IP_SERVER);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("CLIENTE: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}
	}

}
