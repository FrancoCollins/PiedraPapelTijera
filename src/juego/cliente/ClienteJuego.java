package juego.cliente;

import juego.Acciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClienteJuego {
	public static final int PUERTO = 1042;
	public static final String IP_SERVER = "10.34.86.15";

	public static String nombre;

	public static void main(String[] args) {
		System.out.println("        APLICACI�N CLIENTE         ");
		System.out.println("-----------------------------------");

		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);

		try (Scanner sc = new Scanner(System.in);) {
			Socket socketAlServidor = new Socket();
			socketAlServidor.connect(direccionServidor);

			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			BufferedReader bf = new BufferedReader(entrada);
			System.out.println("BIENVENIDO AL JUEGO PIEDRA-PAPEL-TIJERA");
			System.out.print("Introduce el nombre");
			nombre = sc.nextLine();
			salida.println(nombre);
			
			boolean continuar = true;
			if(bf.readLine().equals("e")) {
				
			}
			do {
				
				String eleccion = menuEleccion(sc);
				salida.println(eleccion);
				
				int resultado = Integer.parseInt(bf.readLine());

				if (resultado == Acciones.GANADOR_DE_RONDA) {
	                System.out.println("¡Has ganado la ronda!");
	            } else if (resultado == Acciones.GANADOR_DE_ENFRENTAMIENTO) {
	                System.out.println("¡Has ganado la partida!");
	            } else if (resultado == Acciones.GANADOR_DE_TORNEO) {
	                System.out.println("¡Has ganado el torneo!");
	                continuar = false;
	            }
				
				System.out.println("Eleccion del rival: " + bf.readLine());
			} while (continuar);
		} catch (UnknownHostException e) {
			System.err.println("CLIENTE: No encuentro el servidor en la direcci�n" + IP_SERVER);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("CLIENTE: Error de entrada/salida");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}

		System.out.println("CLIENTE: Fin del programa");
	}

	private static String menuEleccion(Scanner sc) {
		System.out.println("Elija una opción:");
		System.out.println("1- Piedra");
		System.out.println("2- Papel");
		System.out.println("3- Tijera");
		System.out.print("Elige: ");
		String eleccion = sc.nextLine();
		int n = 0;
		
		try {
			while(!eleccion.matches(".[0-9].") || Integer.parseInt(eleccion) > 3) {
				System.out.print("Debes introducir un numero: ");
				eleccion = sc.nextLine();
				if(Integer.parseInt(eleccion) > 3) {
					System.out.println("Debes introducir un numero del menu");
				}
			}
		} catch (Exception e) {
			
		}
		n = Integer.parseInt(eleccion);
		
		switch (n) {
		case 1:
			return "piedra";
		case 2:
			return "papel";
		case 3:
			return "tijera";
		}
		return "";
	}

}
