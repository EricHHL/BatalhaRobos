/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jogo;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 *
 * @author ehhl
 */
public class MyLogger {

    static ArrayList<Consumer> listeners = new ArrayList<>();
    
    public static void listen(Consumer f){
        listeners.add(f);
    }
    
    public static void unlisten(Consumer f){
        listeners.remove(f);
    }
    
    public static void log(String mensagem){
        for (Consumer listener : listeners) {
            listener.accept(mensagem);
        }
    }
}
