package servidor;

public class Main {
    public static void main(String[] args) {
        Torneo torneo = new Torneo();

        new Thread(new ServidorJuego(torneo)).start();
        new Thread(new ServidorJuego(torneo)).start();
        new Thread(new ServidorJuego(torneo)).start();
        new Thread(new ServidorJuego(torneo)).start();


    }
}
