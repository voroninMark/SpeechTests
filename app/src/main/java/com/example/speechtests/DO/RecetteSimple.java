package com.example.speechtests.DO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class RecetteSimple implements Parcelable {
    private String nom;
    private String resume;
    private String match;
    private ArrayList<String> ingredientsPresents;
    private ArrayList<String> ingredientsManquants;

    public RecetteSimple(String p_nom, String p_resume, String p_match,  ArrayList<String> p_ingredientsPresents, ArrayList<String> p_ingredientsManquants) {
        nom = p_nom;
        resume = p_resume;
        match = p_match;
        ingredientsPresents = p_ingredientsPresents;
        ingredientsManquants = p_ingredientsManquants;
    }

    public RecetteSimple(JSONObject json) {
        Gson g = new Gson();
        RecetteSimple newRecette = g.fromJson(json.toString(), RecetteSimple.class);
        this.nom = newRecette.getNom();
        this.resume = newRecette.getResume();
        this.match = newRecette.getMatch();
        this.ingredientsPresents = newRecette.getIngredientsPresents();
        this.ingredientsManquants = newRecette.getIngredientsManquants();
    }

    public RecetteSimple(Parcel in) {
        nom=in.readString();
        resume=in.readString();
        match=in.readString();
        ingredientsPresents = in.readArrayList(null);
        ingredientsManquants = in.readArrayList(null);
    }

    public static final Creator<RecetteSimple> CREATOR = new Creator<RecetteSimple>() {
        @Override
        public RecetteSimple createFromParcel(Parcel in) {
            return new RecetteSimple(in);
        }

        @Override
        public RecetteSimple[] newArray(int size) {
            return new RecetteSimple[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(resume);
        dest.writeString(match);
        dest.writeList(ingredientsPresents);
        dest.writeList(ingredientsManquants);
    }

    @Override
    public String toString() {
        return "RecetteSimple{" +
                "nom='" + nom + '\'' +
                ", resume='" + resume + '\'' +
                ", match='" + match + '\'' +
                ", ingredientsPresents=" + ingredientsPresents +
                ", ingredientsManquants=" + ingredientsManquants +
                '}';
    }

    public String getNom() {
        return nom;
    }

    public String getResume() {
        return resume;
    }

    public String getMatch() {
        return match;
    }

    public ArrayList<String> getIngredientsPresents() {
        return ingredientsPresents;
    }

    public ArrayList<String> getIngredientsManquants() {
        return ingredientsManquants;
    }
}
