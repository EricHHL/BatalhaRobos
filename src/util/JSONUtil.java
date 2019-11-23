/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entidades.Arma;
import entidades.Bomba;
import entidades.Entidade;
import entidades.ItemEspecial;
import entidades.Robo;
import entidades.Virus;
import excecoes.SaveCorrompidoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jogo.Arena;
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

    public static void salvarJogo(File arquivo, Arena arena, int turno) {
        JSONObject salve = new JSONObject();
        salve.put("turno", turno);
        JSONArray entidades = new JSONArray();

        Entidade[][] tabuleiro = arena.getEntidades();
        for (int i = 0; i < tabuleiro.length; i++) {
            for (int j = 0; j < tabuleiro[i].length; j++) {
                if (tabuleiro[i][j] != null) {
                    entidades.add(entidadeParaJson(tabuleiro[i][j]));
                }
            }
        }
        salve.put("entidades", entidades);

        try {
            FileWriter fw = new FileWriter(arquivo);
            fw.write(salve.toJSONString());
            fw.close();

        } catch (IOException ex) {
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static JogoSalvo carregarJogo(File arquivo) throws SaveCorrompidoException {
        ArrayList<Entidade> entidades = new ArrayList<>();
        int turno = 0;
        FileReader fr = null;
        try {
            fr = new FileReader(arquivo);
            JSONObject salve = (JSONObject) parser.parse(fr);
            fr.close();

            turno = ((Long) salve.get("turno")).intValue();
            JSONArray entArr = (JSONArray) salve.get("entidades");
            for (Object entidade : entArr) {
                entidades.add(jsonParaEntidade((JSONObject) entidade));
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JSONUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            throw new SaveCorrompidoException();
        }
        return new JogoSalvo(entidades, turno);
    }

    private static JSONObject entidadeParaJson(Entidade e) {
        JSONObject obj = new JSONObject();
        obj.put("nome", e.getNome());
        obj.put("posX", e.getPosX());
        obj.put("posY", e.getPosY());
        obj.put("imagem", e.getImagem());
        if (e instanceof ItemEspecial) {
            obj.put("dano", ((ItemEspecial) e).getDano());
            if (e instanceof Arma) {
                obj.put("tipo", "arma");
                obj.put("alcance", ((Arma) e).getAlcance());
                obj.put("chanceTiro", ((Arma) e).getChanceTiro());
            }
            if (e instanceof Bomba) {
                obj.put("tipo", "bomba");
            }
            if (e instanceof Virus) {
                obj.put("tipo", "virus");
                obj.put("vidaEmTurnos", ((Virus) e).getVidaEmTurnos());
            }
        }
        if (e instanceof Robo) {
            Robo r = (Robo) e;
            obj.put("tipo", "robo");
            obj.put("jogador", r.getJogador());
            obj.put("vida", r.getVida());
            obj.put("armadura", r.getArmadura());
            obj.put("arma", entidadeParaJson(((Robo) e).getArma()));
            JSONArray viruses = new JSONArray();
            r.getVirus().forEach((virus) -> viruses.add(entidadeParaJson(virus)));
            obj.put("virus", viruses);
        }

        return obj;
    }

    private static Entidade jsonParaEntidade(JSONObject obj) throws SaveCorrompidoException {
        Entidade e = null;
        switch ((String) obj.get("tipo")) {
            case "arma":
                e = new Arma((String) obj.get("imagem"), ((Long) obj.get("alcance")).intValue(), ((Long) obj.get("dano")).intValue(), ((Double) obj.get("chanceTiro")).floatValue());
                break;
            case "bomba":
                e = new Bomba(((Long) obj.get("dano")).intValue());
                break;
            case "virus":
                e = new Virus(((Long) obj.get("dano")).intValue());
                ((Virus) e).setVidaEmTurnos(((Long) obj.get("vidaEmTurnos")).intValue());
                break;
            case "robo":
                Robo r = new Robo((String) obj.get("nome"),
                        ((Long) obj.get("vida")).intValue(),
                        ((Long) obj.get("armadura")).intValue(),
                        (String) obj.get("imagem"));
                r.setArma((Arma) jsonParaEntidade((JSONObject) obj.get("arma")));
                ArrayList<Virus> viruses = new ArrayList<>();
                for (Object virus : ((JSONArray) obj.get("virus"))) {
                    viruses.add((Virus) jsonParaEntidade((JSONObject) virus));
                }
                r.setVirus(viruses);
                r.setJogador(((Long) obj.get("jogador")).intValue());
                e = r;
                break;
            default:
                throw new SaveCorrompidoException();
        }
        e.setNome((String) obj.get("nome"));
        e.setPosX(((Long) obj.get("posX")).intValue());
        e.setPosY(((Long) obj.get("posY")).intValue());
        return e;
    }
}
