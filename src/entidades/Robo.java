/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import excecoes.ExcecaoMorte;
import java.util.ArrayList;
import java.util.Iterator;
import jogo.Arena;
import org.json.simple.JSONObject;

/**
 *
 * @author ehhl
 */
public class Robo extends Entidade {

    private int maxVida = 20;
    private int maxArmadura = 20;
    private int vida;
    private int armadura;
    private Arma arma;
    private Robo oponente;
    private int jogador;

    ArrayList<Virus> virus;

    public Robo(String imagem) {
        super(imagem);
        vida = maxVida;
        armadura = maxArmadura;
        virus = new ArrayList<>();
    }

    public Robo(JSONObject obj) {
        super((String) obj.get("imagem"));
        this.maxVida = ((Long) obj.get("vida")).intValue();
        this.maxArmadura = ((Long) obj.get("armadura")).intValue();
        this.nome = (String) obj.get("nome");
        this.vida = maxVida;
        this.armadura = maxArmadura;
        virus = new ArrayList<>();
    }

    public Robo(String nome, int vida, int armadura, String imagem) {
        super(imagem);
        this.nome = nome;
        this.maxVida = vida;
        this.maxArmadura = armadura;
        this.vida = maxVida;
        this.armadura = maxArmadura;
        virus = new ArrayList<>();
    }

    public Entidade executaTurno() throws ExcecaoMorte {
        for (Iterator<Virus> i = virus.iterator(); i.hasNext();) {
            Virus next = i.next();
            if (!next.danifica(this)) {
                i.remove();
            }
        }
        if (arma != null && distanciaDoOponente() <= arma.getAlcance() && Math.random() < arma.getChanceTiro()) {
            arma.atira(oponente, distanciaDoOponente());
        }

        return this.moveAleatorio();
    }

    int distanciaDoOponente() {
        return (int) Math.sqrt(Math.pow(posX - oponente.getPosX(), 2) + Math.pow(posY - oponente.getPosY(), 2));
    }

    //Escolhe uma das 4 direcoes para se locomover e retorna a entidade coma qual colidiu, desde que nao seja outro Robo
    public Entidade moveAleatorio() {
        int direcao = (int) (Math.random() * 4.0f);
        switch (direcao) {
            case 0:
                return this.arena.moveEntidade(this, this.posX + 1, this.posY);
            case 1:
                return this.arena.moveEntidade(this, this.posX - 1, this.posY);
            case 2:
                return this.arena.moveEntidade(this, this.posX, this.posY + 1);
            case 3:
                return this.arena.moveEntidade(this, this.posX, this.posY - 1);
            default:
                //Não deve acontecer..
                return null;
        }
    }

    public Arma getArma() {
        return arma;
    }

    public void setArma(Arma arma) {
        if (this.arma != null) {
            this.arena.remove(this.arma);
        }

        this.arma = arma;
        if (this.arena != null) {
            this.arena.add(arma, 0);
            this.arena.repaintEntidade(this);
        }
    }

    public void daDano(int dano) throws ExcecaoMorte {
        this.vida -= dano;
        if (this.vida <= 0) {
            throw new ExcecaoMorte(this);
        }
    }

    public void infecta(Virus virus) {
        this.virus.add(virus);
    }

    public Robo getOponente() {
        return oponente;
    }

    public void setOponente(Robo oponente) {
        this.oponente = oponente;
    }

    public int getVida() {
        return vida;
    }

    public int getArmadura() {
        return armadura;
    }

    public int getJogador() {
        return jogador;
    }

    public void setJogador(int jogador) {
        this.jogador = jogador;
    }

    public Robo clone() {
        return new Robo(nome, vida, armadura, imagem);
    }

    public ArrayList<Virus> getVirus() {
        return virus;
    }

    public void setVirus(ArrayList<Virus> virus) {
        this.virus = virus;
    }

    @Override
    public void setArena(Arena arena) {
        this.arena = arena;
        if (this.arma != null) {
            this.arena.add(arma, 0);
            this.arena.repaintEntidade(this);
        }
    }

}
