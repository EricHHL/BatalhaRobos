/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entidades.Arma;
import entidades.Robo;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ehhl
 */
public class JSONUtil {

    private static JSONParser parser = new JSONParser();

    public static ArrayList<Robo> getRobos() {
        ArrayList<Robo> robos = new ArrayList<>();
        try {
            ArrayList<JSONObject> arr;

            arr = (JSONArray) parser.parse(new FileReader("robos.json"));
            arr.forEach((objRobo) -> {
                robos.add(new Robo(objRobo));
            });
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return robos;
    }

    public static ArrayList<Arma> getArmas(boolean apenasIniciais) {
        ArrayList<Arma> armas = new ArrayList<>();
        try {
            ArrayList<JSONObject> arr;

            arr = (JSONArray) parser.parse(new FileReader("armas.json"));
            arr.forEach((objArma) -> {
                if (!apenasIniciais || (boolean) objArma.get("inicial")) {
                    armas.add(new Arma(objArma));
                }
            });
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return armas;
    }
}
