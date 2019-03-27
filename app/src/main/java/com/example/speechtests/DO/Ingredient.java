package com.example.speechtests.DO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import org.json.JSONObject;

public class Ingredient implements Parcelable {
    private String nom;
    private String calorie;
    private String quantite;
    private String unite;



    public Ingredient(String p_nom, String p_calorie, String p_quantite, String p_unite) {
        nom = p_nom;
        calorie = p_calorie;
        quantite = p_quantite;
        unite = p_unite;
    }

    public Ingredient(JSONObject json) {
        Gson g = new Gson();
        Ingredient newIngredient = g.fromJson(json.toString(), Ingredient.class);
        nom = newIngredient.getNom();
        calorie = newIngredient.getCalorie();
        quantite = newIngredient.getQuantite();
        unite = newIngredient.getUnite();
    }

    public Ingredient(Parcel in) {
        nom=in.readString();
        calorie=in.readString();
        quantite=in.readString();
        unite=in.readString();
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(calorie);
        dest.writeString(quantite);
        dest.writeString(unite);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "nom='" + nom + '\'' +
                ", calorie='" + calorie + '\'' +
                ", quantite='" + quantite + '\'' +
                ", unite='" + unite + '\'' +
                '}';
    }

    public String getNom() {
        return nom;
    }

    public String getCalorie() {
        return calorie;
    }

    public String getQuantite() {
        return quantite;
    }

    public String getUnite() {
        return unite;
    }

}
