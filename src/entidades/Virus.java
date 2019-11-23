/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import excecoes.ExcecaoMorte;
import jogo.MyLogger;

/**
 *
 * @author ehhl
 */
public class Virus extends ItemEspecial {

    final int MAX_VIDA_EM_TURNOS = 3;
    int vidaEmTurnos;

    public Virus(String imagem) {
        super(imagem);
    }

    public Virus(int dano) {
        super("virus.png");
        this.dano = dano;
        this.vidaEmTurnos = MAX_VIDA_EM_TURNOS;
    }

    public boolean danifica(Robo robo) throws ExcecaoMorte {
        int dano = ((int) ((float) (this.dano / MAX_VIDA_EM_TURNOS)) * vidaEmTurnos);
        robo.daDano(dano);

        MyLogger.log(robo.getNome() + " tomou " + dano + " de dano do virus");
        vidaEmTurnos--;
        return vidaEmTurnos > 0;
    }

    public int getVidaEmTurnos() {
        return vidaEmTurnos;
    }

    public void setVidaEmTurnos(int vidaEmTurnos) {
        this.vidaEmTurnos = vidaEmTurnos;
    }
    

}
