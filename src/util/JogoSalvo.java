/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entidades.Entidade;
import java.util.ArrayList;

/**
 *
 * @author ehhl
 */
public class JogoSalvo {

    private ArrayList<Entidade> entidades;
    private int turno;

    public JogoSalvo(ArrayList<Entidade> entidades, int turno) {
        this.entidades = entidades;
        this.turno = turno;
    }

    public ArrayList<Entidade> getEntidades() {
        return entidades;
    }

    public int getTurno() {
        return turno;
    }

    
}
