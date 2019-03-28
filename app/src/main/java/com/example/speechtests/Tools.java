package com.example.speechtests;

import com.example.speechtests.DO.RecetteSimple;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Tools {


    public static Map<String,String> getParams(String result[]) throws JSONException {
        Map<String,String> finalParams = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("api_nick", "laurent");
        params.put("api_passwd", "api_vocacook_acces_0");
        params.put("phrase", result);

        JSONObject jsonParam = new JSONObject(params);

        finalParams.put("jsonRequest", jsonParam.toString());
        return finalParams;
    }

    public static Map<String,String> getParamsRecette(RecetteSimple currentRecetteSimple) {
        Map<String, String> params = new HashMap<>();
        params.put("api_nick", "laurent");
        params.put("api_passwd", "api_vocacook_acces_0");
        params.put("nomRecette", currentRecetteSimple.getNom());
        params.put("exact", "true");
        return params;
    }

    public static String chooseCorrectUrl(String wordUser){
        System.out.println("WORD USER : " + wordUser);
        switch (wordUser){
            case "recette" :
                return Constantes.URL_RECETTE;
            case "ingredient" :
                return Constantes.URL_INGREDIENT;
        }
        return Constantes.URL_NOTHING;
    }

    public static int extractNumber(String[] etape){
        int chiffre;
        for (String text: etape) {
            try{
                chiffre = Integer.parseInt(text);
                return chiffre;
            }  catch (NumberFormatException e) {

                e.printStackTrace();
            }
        }
        return -100;
    }

    public static boolean equals(String firstWord, String secondWord){
        if (firstWord.toLowerCase().trim().equals(secondWord.toLowerCase().trim())){
            return true;
        } else {
            return false;
        }
    }

    public static boolean contains(String firstWord, String secondWord){
        if (firstWord.toLowerCase().trim().contains(secondWord.toLowerCase().trim())){
            return true;
        } else {
            return false;
        }
    }
    public static String removeDoublons(String in){
        String[] in_split = in.split(" ");
        String out = "";
        for (int i = 0; i < in_split.length; i++) {
            if(!contains(out,in_split[i])){
                out+=" "+in_split[i];
            }
        }
        return out.trim();
    }
    public static String removeOccurences(String in,String toRemove){
        String[] in_split = in.split(" ");
        String out = "";
        for (int i = 0; i < in_split.length; i++) {
            if(!contains(in_split[i],toRemove)){
                out+=" "+in_split[i];
            }
        }
        return out.trim();
    }
    public static double wordByWordCompare(String compared,String comparer){
        String[] comparer_split = comparer.trim().split(" ");
        int count = 0;
        for (int i = 0; i < comparer_split.length; i++) {
            if(contains(compared,comparer_split[i])){
                count++;
            }
        }
        return   ((double)count)/((double)comparer_split.length);
    }
    public static int indexOfValue(HashMap<Integer,Double> map,double value){
        for (int i = 0; i < map.size(); i++) {
            if(map.get(i) == value){
                return i;
            }
        }
        return -1;
    }
}

