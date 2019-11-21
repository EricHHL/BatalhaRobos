/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import javax.swing.JLabel;
import jogo.Arena;

/**
 *
 * @author ehhl
 */
public abstract class Entidade extends JLabel {

    protected String nome;
    protected String imagem;
    protected int posX;
    protected int posY;
    protected Arena arena;

    public Entidade(String imagem) {
        this.posX = 0;
        this.posY = 0;
        this.imagem = imagem;

    }

    public void inicializa(){
        
        this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/"+this.imagem)));
    }
    
    public int getPosX() {
        return posX;
    }

    public void setPosX(int x) {
        this.posX = x;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int y) {
        this.posY = y;
    }
    
    public void setXY(int x, int y){
        this.posX = x;
        this.posY = y;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
