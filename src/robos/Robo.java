/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robos;

/**
 *
 * @author ehhl
 */
public class Robo extends Entidade{
    
    public Robo(Arena arena, int x, int y) {
        super(arena, x, y);
    }
    
    public void moveAleatorio(){
        int direcao = (int)(Math.random()*4.0f);
        switch(direcao){
            case 0:
                this.arena.moveEntidade(this, this.posX+1, this.posY);
                break;
            case 1:
                this.arena.moveEntidade(this, this.posX-1, this.posY);
                break;
            case 2:
                this.arena.moveEntidade(this, this.posX, this.posY+1);
                break;
            case 3:
                this.arena.moveEntidade(this, this.posX, this.posY-1);
                break;
        }
    }
    
}
