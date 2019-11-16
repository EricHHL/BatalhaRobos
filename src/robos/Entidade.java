/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robos;

import javax.swing.JLabel;

/**
 *
 * @author ehhl
 */
public class Entidade extends JLabel {

    protected int posX;
    protected int posY;
    protected Arena arena;

    public Entidade(Arena arena, int x, int y) {
        this.arena = arena;
        this.posX = x;
        this.posY = y;

        this.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/robo.png"))); // NOI18N
        arena.novaEntidade(this, x, y);
        arena.repaint();
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

}
