/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import entidades.Arma;
import entidades.Entidade;
import entidades.Robo;
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

    public void novaEntidade(Entidade entidade) {
        int x = entidade.getPosX();
        int y = entidade.getPosY();
        if (x < 0 || x > tam || y < 0 || y > tam) {
            //System.out.println("Nova entidade sendo criada na posicao invalida [" + x + "," + y + "]");;
            return;
        }

        if (this.entidades[x][y] != null) {
            //System.out.println("Nova entidade sendo criada em posicao ocupada[" + x + "," + y + "]");
            return;
        }

        entidade.setArena(this);
        entidade.inicializa();

        this.entidades[x][y] = entidade;
        this.add(entidade);
        this.repaintEntidade(entidade);
    }

    public Entidade moveEntidade(Entidade entidade, int x, int y) {

        //Punir o jogador por algo que ele não tem controle é injusto e frustrante
        //Teleporta pro outro lado da arena ao inves disso
        if (x < 0) {
            x = tam - 1;
        }
        if (x >= tam) {
            x = 0;
        }
        if (y < 0) {
            y = tam - 1;
        }
        if (y >= tam) {
            y = 0;
        }

        this.entidades[entidade.getPosX()][entidade.getPosY()] = null;
        Entidade retorno = null;
        if (this.entidades[x][y] != null) {
            retorno = this.entidades[x][y];
            if (this.entidades[x][y] instanceof Robo) {
                while (true) {
                    x = (int) (Math.random() * tam);
                    y = (int) (Math.random() * tam);
                    if (this.entidades[x][y] == null) {
                        this.moveEntidade(entidade, x, y);
                        break;
                    }
                }
                return retorno;
            } else {
                this.remove(retorno);
                this.entidades[x][y] = null;
            }
        }

        this.entidades[x][y] = entidade;

        entidade.setPosX(x);
        entidade.setPosY(y);

        this.repaintEntidade(entidade);
        return retorno;
    }

    public void repaintEntidade(Entidade entidade) {
        entidade.setBounds(entidade.getPosX() * 48, entidade.getPosY() * 48, 48, 48);
        entidade.repaint();

        if (entidade instanceof Robo) {
            Arma arma = ((Robo) entidade).getArma();
            if (arma != null) {
                arma.setBounds(entidade.getPosX() * 48, entidade.getPosY() * 48, 42, 40);
                arma.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                arma.repaint();
            }
        }
    }

    public int[] getPosicaoAleatoriaVazia() {
        int x, y;
        do {
            x = (int) (Math.random() * tam);
            y = (int) (Math.random() * tam);
        } while (entidades[x][y] != null);

        return new int[]{x, y};

    }

    void reset() {
        removeAll();
        revalidate();
        entidades = new Entidade[tam][tam];
    }

    public Entidade[][] getEntidades() {
        return entidades;
    }

    public void setEntidades(Entidade[][] entidades) {
        this.entidades = entidades;
    }
    
}
