package juego.servidor;

import juego.Senal;

public class Final extends Enfrentamiento {

    public Final(Jugador p1, Jugador p2, Torneo torneo) {
        super(p1, p2, torneo);
    }

    @Override
    public void run(){
        System.out.println("Comienza la final!");
        torneo.enviarSenalAJugadores(p1, p2, Senal.COMENZAR_FINAL);
        while (getPuntajeP1() < 3 && getPuntajeP2() < 3) {
            torneo.enviarSenalAJugadores(p1, p2, Senal.ENVIAR_SELECCION);

            int senal1 = Senal.ERROR, senal2 = Senal.ERROR;

            int[] senales = torneo.recibirAcciones(p1, p2, senal1, senal2);
            senal1 = senales[0];
            senal2 = senales[1];

            System.out.println("Senal 1: " + senal1);
            System.out.println("Senal 2: " + senal2);

            do {
                if (senal1 == Senal.ERROR) {
                    torneo.enviarSenalAJugador(getP1(), Senal.SELECCION_INCORRECTA);
                    senal1 = torneo.recibirAccionJugador(getP1());
                }

                if (senal2 == Senal.ERROR) {
                    torneo.enviarSenalAJugador(getP2(), Senal.SELECCION_INCORRECTA);
                    senal2 = torneo.recibirAccionJugador(getP2());
                }
            } while (seleccionEsIncorrecta(senal1, senal2) || torneo.esError(senal1, senal2));

            Jugador ganador = ganadorRonda(senal1, senal2);



            if (ganador == null) {
                torneo.enviarSenalAJugadores(p1, p2, Senal.EMPATE);
            } else {
                if (ganador == getP1()) {
                    torneo.enviarSenalAJugador(getP1(), Senal.GANADOR_DE_RONDA);
                    torneo.enviarSenalAJugador(getP2(), Senal.PERDEDOR_DE_RONDA);
                    setPuntajeP1(getPuntajeP1() + 1);
                } else {
                    torneo.enviarSenalAJugador(getP2(), Senal.GANADOR_DE_RONDA);
                    torneo.enviarSenalAJugador(getP1(), Senal.PERDEDOR_DE_RONDA);
                    setPuntajeP2(getPuntajeP2() + 1);
                }
            }


            torneo.enviarPaqueteAJugador(getP1(), "" + getPuntajeP1() + "" + Senal.SEPARADOR + "" + getPuntajeP2());
            torneo.enviarPaqueteAJugador(getP2(), "" + getPuntajeP2() + "" + Senal.SEPARADOR + "" + getPuntajeP1());

        }



        if (getPuntajeP1() == 3) {
            setGanadorFinal(getP1());
            torneo.enviarSenalAJugador(getP1(), Senal.GANADOR_DE_ENFRENTAMIENTO);
            torneo.enviarSenalAJugador(getP1(), Senal.GANADOR_DE_TORNEO);
            torneo.enviarSenalAJugador(getP2(), Senal.PERDEDOR_DE_ENFRENTAMIENTO);
        } else {
            setGanadorFinal(getP2());
            torneo.enviarSenalAJugador(getP2(), Senal.GANADOR_DE_ENFRENTAMIENTO);
            torneo.enviarSenalAJugador(getP2(), Senal.GANADOR_DE_TORNEO);
            torneo.enviarSenalAJugador(getP1(), Senal.PERDEDOR_DE_ENFRENTAMIENTO);
        }

        torneo.enviarPaqueteAJugadores(p1, p2, getGanadorFinal().nombreDeUsuario);

    }
}
