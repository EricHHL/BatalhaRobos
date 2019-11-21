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
public abstract class ItemEspecial extends Entidade {

    int dano;
    
    public ItemEspecial(String imagem) {
        super(imagem);
    }

    public int getDano() {
        return dano;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }


}
