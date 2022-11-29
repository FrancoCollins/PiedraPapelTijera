package juego.cliente;


import juego.Senal;
import juego.cliente.graphics.Graphics;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
	public static final int PUERTO = 1043;
	public static final String IP_SERVER = "10.34.83.45";

	public static String nombre;


	public static void main(String[] args) {
		System.out.println("        APLICACIÓN CLIENTE         ");
		System.out.println("-----------------------------------");
		System.out.println("BIENVENIDO AL JUEGO PIEDRA-PAPEL-TIJERA");

		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);

		GameFunctionality game = new GameFunctionality();
		Graphics gf = new Graphics(game);
		game.setGraphics(gf);

		try {
			Socket socketAlServidor = new Socket();
			socketAlServidor.connect(direccionServidor);

			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			Scanner entrada = new Scanner(new InputStreamReader(socketAlServidor.getInputStream()));

			SignalManager manager = new SignalManager(entrada, salida, game);
			game.setSignalManager(manager);

			manager.enviarSenal(Senal.CONECTARSE);
			System.out.println("Enviada senal de conectarse");


			manager.start();
		} catch (UnknownHostException e) {
			System.err.println("CLIENTE: No encuentro el servidor en la dirección" + IP_SERVER);
		} catch (ConnectException e){
			System.out.println("CLIENTE: Connection timed out.");
		} catch (IOException e) {
			System.err.println("CLIENTE: Error de entrada/salida");
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}
	}
}
