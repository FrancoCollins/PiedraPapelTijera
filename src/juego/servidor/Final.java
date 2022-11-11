package juego.servidor;

import juego.Senal;

public class Final extends Enfrentamiento {

    public Final(Jugador p1, Jugador p2) {
        super(p1, p2);
    }

    @Override
    public void run(){
        System.out.println("Comienza la final!");
        enviarSenalAJugadores(Senal.COMENZAR_FINAL);
        while (getPuntajeP1() < 3 && getPuntajeP2() < 3) {
            enviarSenalAJugadores(Senal.ENVIAR_SELECCION);

            int senal1 = Senal.ERROR, senal2 = Senal.ERROR;

            int[] senales = recibirAcciones(senal1, senal2);
            senal1 = senales[0];
            senal2 = senales[1];

            System.out.println("Senal 1: " + senal1);
            System.out.println("Senal 2: " + senal2);

            do {
                if (senal1 == Senal.ERROR) {
                    enviarSenalAJugador(getP1(), Senal.SELECCION_INCORRECTA);
                    senal1 = recibirAccionJugador(getP1());
                }

                if (senal2 == Senal.ERROR) {
                    enviarSenalAJugador(getP2(), Senal.SELECCION_INCORRECTA);
                    senal2 = recibirAccionJugador(getP2());
                }
            } while (seleccionEsIncorrecta(senal1, senal2) || esError(senal1, senal2));

            Jugador ganador = ganadorRonda(senal1, senal2);



            if (ganador == null) {
                enviarSenalAJugadores(Senal.EMPATE);
            } else {
                if (ganador == getP1()) {
                    enviarSenalAJugador(getP1(), Senal.GANADOR_DE_RONDA);
                    enviarSenalAJugador(getP2(), Senal.PERDEDOR_DE_RONDA);
                    setPuntajeP1(getPuntajeP1() + 1);
                } else {
                    enviarSenalAJugador(getP2(), Senal.GANADOR_DE_RONDA);
                    enviarSenalAJugador(getP1(), Senal.PERDEDOR_DE_RONDA);
                    setPuntajeP2(getPuntajeP2() + 1);
                }
            }


            enviarPaqueteAJugador(getP1(), "" + getPuntajeP1() + "" + Senal.SEPARADOR + "" + getPuntajeP2());
            enviarPaqueteAJugador(getP2(), "" + getPuntajeP2() + "" + Senal.SEPARADOR + "" + getPuntajeP1());

        }



        if (getPuntajeP1() == 3) {
            setGanadorFinal(getP1());
            enviarSenalAJugador(getP1(), Senal.GANADOR_DE_ENFRENTAMIENTO);
            enviarSenalAJugador(getP1(), Senal.GANADOR_DE_TORNEO);
            enviarSenalAJugador(getP2(), Senal.PERDEDOR_DE_ENFRENTAMIENTO);
        } else {
            setGanadorFinal(getP2());
            enviarSenalAJugador(getP2(), Senal.GANADOR_DE_ENFRENTAMIENTO);
            enviarSenalAJugador(getP2(), Senal.GANADOR_DE_TORNEO);
            enviarSenalAJugador(getP1(), Senal.PERDEDOR_DE_ENFRENTAMIENTO);
        }

        enviarPaqueteAJugadores(getGanadorFinal().nombreDeUsuario);

    }
}
