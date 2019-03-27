package com.example.speechtests;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Vocabulaire {
    String listeMots;
    InputStream input;
    InputStreamReader isr;
    BufferedReader reader;

    public Vocabulaire(){}
    public Vocabulaire(AssetManager assetManager, String fileName){
        load(assetManager, fileName);
    }

    public void load(AssetManager assetManager, String fileName){
        try {
            input = assetManager.open(fileName);
            isr = new InputStreamReader(input);
            reader = new BufferedReader(isr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String find(String[] resultat) throws IOException {
        while(reader.ready()) {
            String wordInVoca = reader.readLine();
            for (int i = 0; i<resultat.length; i++){
                String token = resultat[i];

                boolean resultCompar = token.toLowerCase().trim().equals(wordInVoca);
                System.out.println(token.toLowerCase().trim() + " = " + wordInVoca + " " + resultCompar);
                if (resultCompar) {
                    return token.toLowerCase().trim();
                }
            }
        }
        return "word not found";
    }
}
