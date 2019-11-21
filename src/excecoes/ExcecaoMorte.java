/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excecoes;

import entidades.Robo;

/**
 *
 * @author ehhl
 */
public class ExcecaoMorte extends Exception {

    Robo morto;

    public ExcecaoMorte(Robo morto) {
        this.morto = morto;
    }

    public Robo getMorto() {
        return morto;
    }

    public void setMorto(Robo morto) {
        this.morto = morto;
    }
    
}
