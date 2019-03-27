package com.example.speechtests.DO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recette implements Parcelable {
    private String nom;
    private String etapes;
    private String resume;
    private ArrayList<Ingredient> ingredients;

    public Recette(String p_nom, String p_etapes, String p_resume, ArrayList<Ingredient> p_ingredients) {
        nom = p_nom;
        etapes = p_etapes;
        resume = p_resume;
        ingredients = p_ingredients;
    }

    public Recette(JSONObject json) throws JSONException {
        ingredients = new ArrayList<>();
        Gson g = new Gson();
        Recette recette= g.fromJson(json.toString(), Recette.class);
        nom = recette.getNom();
        etapes = recette.getEtapes();
        resume = recette.getResume();

        JSONArray jsonIngredients = json.getJSONArray("ingredients");
        for (int i =0; i<jsonIngredients.length(); i++){
            Ingredient ingredient = new Ingredient((JSONObject)jsonIngredients.get(i));
            ingredients.add(ingredient);
        }
    }

    public Recette(Parcel in) {
        nom=in.readString();
        etapes=in.readString();
        resume = in.readString();
        ingredients = in.readArrayList(null);
    }

    public static final Parcelable.Creator<Recette> CREATOR = new Parcelable.Creator<Recette>() {
        @Override
        public Recette createFromParcel(Parcel in) {
            return new Recette(in);
        }

        @Override
        public Recette[] newArray(int size) {
            return new Recette[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(etapes);
        dest.writeString(resume);
        dest.writeList(ingredients);
    }

    @Override
    public String toString() {
        return "Recette{" +
                "nom='" + nom + '\'' +
                ", etapes='" + etapes + '\'' +
                ", resume='" + resume + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }

    public String getNom() {
        return nom;
    }

    public String getEtapes() {
        return etapes;
    }

    public String getEtapeByIndex(int index) {
        return split_etapes()[index];
    }
    public String getResume() {
        return resume;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String[] split_etapes(){
        return etapes.split("//");
    }
}
