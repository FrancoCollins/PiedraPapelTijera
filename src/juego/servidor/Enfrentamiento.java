package juego.servidor;

import juego.Senal;

public class Enfrentamiento extends Thread {

    protected Jugador p1;
    protected Jugador p2;

    protected Jugador ganadorFinal;

    protected Torneo torneo;

    protected int puntajeP1;
    protected int puntajeP2;


    public Enfrentamiento(Jugador p1, Jugador p2, Torneo torneo) {
        this.p1 = p1;
        this.p2 = p2;
        this.torneo = torneo;
        this.ganadorFinal = null;
    }


    public boolean seleccionEsIncorrecta(int senal1, int senal2) {
        if (senal1 != Senal.PIEDRA && senal1 != Senal.PAPEL && senal1 != Senal.TIJERA)
            return true;

        if (senal2 != Senal.PIEDRA && senal2 != Senal.PAPEL && senal2 != Senal.TIJERA)
            return true;

        return false;
    }

    public Jugador ganadorRonda(int senal1, int senal2) {

        // empate
        if (senal1 == senal2)
            return null;

        else if (senal1 == Senal.PIEDRA)
            return senal2 == Senal.PAPEL ? p2 : p1;

        else if (senal1 == Senal.PAPEL)
            return senal2 == Senal.TIJERA ? p2 : p1;

        else
            return senal1 == Senal.TIJERA ? p2 : p1;
    }

    @Override
    public void run() {
        torneo.enviarSenalAJugadores(p1, p2, Senal.NOMBRE_DEL_RIVAL);
        torneo.enviarPaqueteAJugador(p1, p2.nombreDeUsuario);
        torneo.enviarPaqueteAJugador(p2, p1.nombreDeUsuario);

        System.out.println("Comienza la partida!");
        torneo.enviarSenalAJugadores(p1, p2, Senal.COMENZAR_ENFRENTAMIENTO);
        while (puntajeP1 < 3 && puntajeP2 < 3) {
            torneo.enviarSenalAJugadores(p1, p2, Senal.ENVIAR_SELECCION);

            int senal1 = Senal.ERROR, senal2 = Senal.ERROR;

            int[] senales = torneo.recibirAcciones(p1, p2, senal1, senal2);
            senal1 = senales[0];
            senal2 = senales[1];

            System.out.println("Senal 1: " + senal1);
            System.out.println("Senal 2: " + senal2);

            do {
                if (senal1 == Senal.ERROR) {
                    torneo.enviarSenalAJugador(p1, Senal.SELECCION_INCORRECTA);
                    senal1 = torneo.recibirAccionJugador(p1);
                }

                if (senal2 == Senal.ERROR) {
                    torneo.enviarSenalAJugador(p2, Senal.SELECCION_INCORRECTA);
                    senal2 = torneo.recibirAccionJugador(p2);
                }
            } while (seleccionEsIncorrecta(senal1, senal2) || torneo.esError(senal1, senal2));

            Jugador ganador = ganadorRonda(senal1, senal2);



            if (ganador == null) {
                 torneo.enviarSenalAJugadores(p1, p2, Senal.EMPATE);
            } else {
                if (ganador == p1) {
                    torneo.enviarSenalAJugador(p1, Senal.GANADOR_DE_RONDA);
                    torneo.enviarSenalAJugador(p2, Senal.PERDEDOR_DE_RONDA);
                    puntajeP1++;
                } else {
                    torneo.enviarSenalAJugador(p2, Senal.GANADOR_DE_RONDA);
                    torneo.enviarSenalAJugador(p1, Senal.PERDEDOR_DE_RONDA);
                    puntajeP2++;
                }
            }

            enviarPuntajes();

            try {
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



        if (puntajeP1 == 3) {
            ganadorFinal = p1;
            torneo.enviarSenalAJugador(p1, Senal.GANADOR_DE_ENFRENTAMIENTO);
            torneo.enviarSenalAJugador(p2, Senal.PERDEDOR_DE_ENFRENTAMIENTO);
        } else {
            ganadorFinal = p2;
            torneo.enviarSenalAJugador(p2, Senal.GANADOR_DE_ENFRENTAMIENTO);
            torneo.enviarSenalAJugador(p1, Senal.PERDEDOR_DE_ENFRENTAMIENTO);
        }

        torneo.enviarSenalAJugadores(p1, p2, Senal.NOMBRE_GANADOR_DEL_ENFRENTAMIENTO);
        torneo.enviarPaqueteAJugadores(p1, p2, ganadorFinal.nombreDeUsuario);

    }

    public void enviarPuntajes() {
        System.out.println("" + puntajeP1 + "" + Senal.SEPARADOR + "" + puntajeP2);

        torneo.enviarSenalAJugadores(p1, p2, Senal.PAQUETE_PUNTUACION);
        torneo.enviarPaqueteAJugador(p1, "" + puntajeP1 + "" + Senal.SEPARADOR + "" + puntajeP2);
        torneo.enviarPaqueteAJugador(p2, "" + puntajeP2 + "" + Senal.SEPARADOR + "" + puntajeP1);
    }


    public Jugador getP1() {
        return p1;
    }

    public void setP1(Jugador p1) {
        this.p1 = p1;
    }

    public Jugador getP2() {
        return p2;
    }

    public void setP2(Jugador p2) {
        this.p2 = p2;
    }

    public int getPuntajeP1() {
        return puntajeP1;
    }

    public void setPuntajeP1(int puntajeP1) {
        this.puntajeP1 = puntajeP1;
    }

    public int getPuntajeP2() {
        return puntajeP2;
    }

    public void setPuntajeP2(int puntajeP2) {
        this.puntajeP2 = puntajeP2;
    }

    public void setGanadorFinal(Jugador ganador){
        this.ganadorFinal = ganador;
    }

    public Jugador getGanadorFinal(){ return ganadorFinal;}
}

