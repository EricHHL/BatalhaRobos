/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import excecoes.ExcecaoMorte;
import jogo.MyLogger;
import org.json.simple.JSONObject;

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

    public Arma(JSONObject obj) {
        super((String) obj.get("imagem"));
        this.nome = (String) obj.get("nome");
        this.dano = ((Long) obj.get("dano")).intValue();
        this.alcance = ((Long) obj.get("alcance")).intValue();
        this.chanceTiro = ((Double) obj.get("chance")).floatValue();
    }

    private Arma(String imagem, String nome, int dano, int alcance, float chanceTiro) {
        super(imagem);
        this.nome = nome;
        this.dano = dano;
        this.alcance = alcance;
        this.chanceTiro = chanceTiro;
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
        //Super fórmula secreta de dano
        int dano = (int) Math.max(((int) ((this.dano * (distancia / alcance)) * (1 + Math.random())) - (robo.getArmadura() * Math.random())), 1);

        MyLogger.log(robo.getNome() + " levou " + dano + " de dano de: " + nome);
        robo.daDano(dano);
    }

    public String getChancePorcento() {
        return ((int) (chanceTiro * 100)) + "%";
    }

    public Arma clone() {
        return new Arma(imagem, nome, dano, alcance, chanceTiro);
    }

}
