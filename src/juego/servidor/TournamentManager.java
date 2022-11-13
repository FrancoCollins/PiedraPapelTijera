package juego.servidor;

import juego.Senal;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class TournamentManager {

    private HashMap<String, Torneo> torneoHashMap;

    public TournamentManager() {
        torneoHashMap = new HashMap<>();
    }

    public String agregarTorneo(Jugador creador, Torneo torneo) {
        boolean incorrecto = true;
        String clave;
        do {
            clave = cadenaAleatoria(10);
            if (torneoHashMap.get(clave) == null) {
                torneoHashMap.put(clave, torneo);
                System.out.println("Torneo creado con clave: " + clave);
                incorrecto = false;
            }
        } while (incorrecto);
        return clave;
    }

    public Torneo obtenerTorneo(String clave) {
        return torneoHashMap.get(clave);
    }

    public synchronized HashMap<String, Torneo> mostrarTorneos() {
        HashMap<String, Torneo> torneosPublicos = new HashMap<>();
        torneoHashMap.forEach((key, value) -> {
            if (!value.isPrivado())
                torneosPublicos.put(key, value);
        });
        return torneosPublicos;
    }

    public synchronized int unirJugadorATorneo(Jugador jugador, String clave) {
        if (torneoHashMap.get(clave) == null)
            return Senal.TORNEO_INEXISTENTE;

        if (torneoHashMap.get(clave).getJugadores().size() == torneoHashMap.get(clave).getMAX_PLAYERS())
            return Senal.TORNEO_LLENO;
        else {
            torneoHashMap.get(clave).agregarJugador(jugador);
            return Senal.UNION_EXITOSA_TORNEO;
        }
    }


    private String cadenaAleatoria(int longitud) {
        // El banco de caracteres
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        // La cadena en donde iremos agregando un carácter aleatorio
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
            char caracterAleatorio = banco.charAt(indiceAleatorio);
            cadena += caracterAleatorio;
        }
        return cadena;
    }

    private int numeroAleatorioEnRango(int minimo, int maximo) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }


}
