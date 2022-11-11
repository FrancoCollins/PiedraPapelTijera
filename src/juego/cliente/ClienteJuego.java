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
import java.util.StringTokenizer;

public class ClienteJuego {
	public static final int PUERTO = 1043;
	public static final String IP_SERVER = "localhost";

	public static String nombre;


	public static void main(String[] args) {
		System.out.println("        APLICACIÓN CLIENTE         ");
		System.out.println("-----------------------------------");

		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, PUERTO);

		try (Scanner sc = new Scanner(System.in)) {
			Socket socketAlServidor = new Socket();
			socketAlServidor.connect(direccionServidor);

			PrintStream salida = new PrintStream(socketAlServidor.getOutputStream());
			InputStreamReader entrada = new InputStreamReader(socketAlServidor.getInputStream());
			BufferedReader bf = new BufferedReader(entrada);

			if(Integer.parseInt(bf.readLine()) != Acciones.CONEXION_EXITOSA){
				System.out.println("Conexión fallida.");
				return;
			}

			System.out.println("BIENVENIDO AL JUEGO PIEDRA-PAPEL-TIJERA");
			System.out.print("Introduce tu nombre de usuario: ");
			nombre = sc.nextLine();
			salida.println(nombre);

			boolean continuarTorneo = true;
			boolean continuarPartida = true;

			while(continuarTorneo && continuarPartida){
				if(Integer.parseInt(bf.readLine()) != Acciones.ENVIAR_SELECCION){
					System.out.println("Error de sincronización cliente-servidor.");
					return;
				}
				int eleccion = menuEleccion(sc);
				salida.println(eleccion);


				int resultado = Integer.parseInt(bf.readLine());

				System.out.println("Resultado: " + resultado);

				switch (resultado) {
					case Acciones.GANADOR_DE_RONDA:
						System.out.println("¡Has ganado la ronda!");
						break;
					case Acciones.PERDEDOR_DE_RONDA:
						System.out.println("¡Has perdido la ronda!");
						break;
					case Acciones.GANADOR_DE_ENFRENTAMIENTO:
						System.out.println("¡Has ganado el enfrentamiento!");
						continuarPartida = false;
						break;
					case Acciones.PERDEDOR_DE_ENFRENTAMIENTO:
						System.out.println("Has perdido la partida :(");
						continuarPartida = false;
						continuarTorneo = false;
						break;
					case Acciones.GANADOR_DE_TORNEO:
						System.out.println("¡Has ganado el torneo!");
						continuarTorneo = false;
						continuarPartida = false;
						break;
				}

				String linea = bf.readLine();
				String[] puntajes_str = linea.split("\\|");
				int[] puntajes = new int[puntajes_str.length];

				for (int i = 0; i < puntajes.length; i++)
					puntajes[i] = Integer.parseInt(puntajes_str[i]);

				System.out.println("Tu puntaje: " + puntajes[0]);
				System.out.println("Puntaje del rival: " + puntajes[1]);



				if (puntajes[0] == 3){
					if(Integer.parseInt(bf.readLine()) != Acciones.GANADOR_DE_ENFRENTAMIENTO){
						System.out.println("Error.");
						return;
					}
					System.out.println("¡Has ganado el enfrentamiento!");
					String ganador = bf.readLine();
					System.out.println(ganador + ", eres el mejor!");
				}

				else if (puntajes[1] == 3){
					if(Integer.parseInt(bf.readLine()) != Acciones.PERDEDOR_DE_ENFRENTAMIENTO){
						System.out.println("Error.");
						return;
					}
					System.out.println("Has perdido el enfrentamiento.");
					String ganador = bf.readLine();
					System.out.println("El ganador es: " + ganador + '.');
				}
			}

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

	private static int menuEleccion(Scanner sc) {
		System.out.println("Elija una opción:");
		System.out.println("1- Piedra");
		System.out.println("2- Papel");
		System.out.println("3- Tijera");
		System.out.print("Elige: ");
		String eleccion = sc.nextLine();
		int n = 0;

		try {
			while(!eleccion.matches("[0-9]") || Integer.parseInt(eleccion) > 3) {
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
				return Acciones.PIEDRA;
			case 2:
				return Acciones.PAPEL;
			case 3:
				return Acciones.TIJERA;
		}
		return Acciones.ERROR;
	}

}
