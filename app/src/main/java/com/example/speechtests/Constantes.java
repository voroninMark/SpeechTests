package com.example.speechtests;

public class Constantes {

    final public static String VOCAL_ACTIVATE ="chef";
    final public static String VOCAL_INGREDIENT = "ingrédient";
    final public static String VOCAL_RECETTE ="recette";
    final public static String VOCAL_SELECTION="chois";
    final public static String URL_INGREDIENT = "http://vps507765.ovh.net/api_vocacook/requests/getRecetteByIngredients.php";
    final public static String URL_RECETTE = "http://vps507765.ovh.net/api_vocacook/requests/getRecetteByPhrase.php";
    final public static String URL_NOTHING = "http://vps507765.ovh.net/api_vocacook/others/exceptionWords";
    final public static String URL_RECETTE_COMPLETE="http://vps507765.ovh.net/api_vocacook/requests/getRecetteByName.php";
    final public static String VOCAL_ETAPE = "étape";
    final public static String VOCAL_PRECEDENT = "précédent";
    final public static double MINIMUM_MATCHING = 0.1;
}
