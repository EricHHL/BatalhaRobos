/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robos;

import javax.swing.JPanel;

/**
 *
 * @author ehhl
 */
public class Arena extends JPanel {

    int tam = 10;
    Entidade[][] entidades = new Entidade[tam][tam];

    public Arena() {
    }

    void novaEntidade(Entidade entidade, int x, int y) {
        if (x < 0 || x > tam || y < 0 || y > tam) {
            System.out.println("Nova entidade sendo criada na posicao invalida [" + x + "," + y + "]");
            return;
        }
        
        if (this.entidades[x][y] != null) {
            System.out.println("Nova entidade sendo criada em posicao ocupada[" + x + "," + y + "]");
        }
        
        this.entidades[x][y] = entidade;
        this.add(entidade);
        this.repaintEntidade(entidade);
    }

    public void moveEntidade(Entidade entidade, int x, int y){
        this.entidades[entidade.getPosX()][entidade.getPosY()] = null;
        this.entidades[x][y] = entidade;
        entidade.setPosX(x);
        entidade.setPosY(y);
        this.repaintEntidade(entidade);
    }
    
    void repaintEntidade(Entidade entidade) {
        entidade.setBounds(entidade.getPosX()*48, entidade.getPosY()*48, 48, 48);
        entidade.repaint();
        this.repaint();
    }
}
