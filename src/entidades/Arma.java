/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import excecoes.ExcecaoMorte;
import jogo.Arena;
import jogo.MyLogger;

/**
 *
 * @author ehhl
 */
public class Arma extends ItemEspecial {

    int alcance;
    float chanceTiro;

    public Arma(String imagem) {
        super(imagem);
    }

    public Arma(String imagem, int alcance, int dano, float chanceTiro) {
        super(imagem);
        this.alcance = alcance;
        this.dano = dano;
        this.chanceTiro = chanceTiro;
        this.nome = "Arma";
    }

    public int getAlcance() {
        return alcance;
    }

    public void setAlcance(int alcance) {
        this.alcance = alcance;
    }

    public float getChanceTiro() {
        return chanceTiro;
    }

    public void setChanceTiro(float chanceTiro) {
        this.chanceTiro = chanceTiro;
    }

    void atira(Robo robo, int distancia) throws ExcecaoMorte {
        int dano = 1 + (int) ((this.dano / distancia) * Math.random());

        MyLogger.log(robo.getNome() + " levou " + dano + " de dano de: " + nome);
        robo.daDano(dano);
    }

    public String getChancePorcento() {
        return ((int) (chanceTiro * 100)) + "%";
    }

}
