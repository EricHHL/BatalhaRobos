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
import jogo.MyLogger;

/**
 *
 * @author ehhl
 */
public class Robo extends Entidade {

    final int MAX_VIDA = 20;
    int vida;
    Arma arma;
    Robo oponente;

    ArrayList<Virus> virus;

    public Robo(String imagem) {
        super(imagem);
        vida = MAX_VIDA;
        virus = new ArrayList<>();
    }

    public Entidade executaTurno() throws ExcecaoMorte {
        for (Iterator<Virus> i = virus.iterator(); i.hasNext();) {
            Virus next = i.next();
            if (!next.danifica(this)) {
                i.remove();
            }
        }
        if (arma != null) {
            System.out.println(distanciaDoOponente() + " <= " + arma.getAlcance());
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
        System.out.println(this.posX + "," + this.posY);
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
        this.arena.add(arma, 0);
        this.arena.repaintEntidade(this);
        System.out.println("adicionou arma");
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

}
