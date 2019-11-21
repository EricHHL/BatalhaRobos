/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import jogo.Arena;

/**
 *
 * @author ehhl
 */
public class Bomba extends ItemEspecial{
    
    public Bomba(String imagem) {
        super(imagem);
    }
    
    public Bomba(int dano){
        super("bomba.png");
        this.dano = dano;
    }
    
}
