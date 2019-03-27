package com.example.speechtests;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.speechtests.DO.Ingredient;
import com.example.speechtests.DO.Recette;
import com.example.speechtests.DO.RecetteSimple;
import com.example.speechtests.HTTP.VolleyResponseListener;
import com.example.speechtests.HTTP.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements RecognitionListener {


    SpeechRecognizer speechRecognizer;
    Intent recognizerIntent;
    String resultats;
    RecettesAdapter recettesAdapter;
    ListView listRecettes;
    AudioManager audioManager;
    Boolean listenning;
    View popupView;
    PopupWindow popupWindow;
    TextView nomRecette, resumeRecette, listIngredients, currentEtape, titleEtape;
    Recette currentRecette;
    Button playSoundEtape,  stopSoundEtape;
    private TextToSpeech textToSpeech;
    int flagLayout, indexEtape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(R.layout.default_layout);
        resultats = "";
        listenning = false;
        indexEtape = 0;
        doPerms();
        initListener();
        initializeTextToSpeech();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustVolume(AudioManager.ADJUST_MUTE, 0);
    }

    private void initializeEventList(){
        listRecettes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecetteSimple recetteSimple = (RecetteSimple) listRecettes.getItemAtPosition(position);
                loadRecetteComplete(recetteSimple);

            }
        });
    }

    private void initializeTextToSpeech(){
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.FRANCE);
                }
            }
        });
    }

    private void initializeEvents(){
        playSoundEtape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(currentEtape.getText().subSequence(0, currentEtape.length()), TextToSpeech.QUEUE_FLUSH,null, null);
            }
        });
        stopSoundEtape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.stop();
            }
        });
    }

    private void initializeWidgets(){
        nomRecette = findViewById(R.id.nomRecette);
        resumeRecette = findViewById(R.id.resumeRecette);
        listIngredients = findViewById(R.id.ingredients_list);
        currentEtape = findViewById(R.id.current_etape);
        titleEtape = findViewById(R.id.title_etapes);
        playSoundEtape = findViewById(R.id.play_etape_sound);
        stopSoundEtape = findViewById(R.id.stop_etape_sound);
    }

    private void initializeList(){
        String ingredients ="Liste des ingrédients \n";
        for (Ingredient ingredient : currentRecette.getIngredients()) {
            ingredients+= ("- "+ingredient.getNom()+" "+ingredient.getQuantite()+" "+ingredient.getUnite()+"\n");
        }
        ingredients = ingredients.substring(0, ingredients.length()-2);
        listIngredients.setText(ingredients);
    }

    private void initializeEtape(int index){
        titleEtape.setText("Etape "+(index+1)+" /"+currentRecette.split_etapes().length);
        currentEtape.setText(currentRecette.getEtapeByIndex(index));
    }

    public void switchEtape(View view) {
        if(view == findViewById(R.id.previous_etape)){
            if(indexEtape!=0){
                indexEtape --;
                initializeEtape(indexEtape);
            }
        } else {
            if(indexEtape < currentRecette.split_etapes().length-1){
                indexEtape++;
                initializeEtape(indexEtape);
            }

        }
    }
    public void initListener(){
        speechRecognizer =  SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplication().getPackageName());
        speechRecognizer.startListening(recognizerIntent);
    }

    private void setLayout(int layout){
        flagLayout = layout;
        setContentView(flagLayout);
    }
    private String findMenuWord(String[] userPhrase){
        for (int i = 0; i<userPhrase.length; i++) {
            String token = userPhrase[i];
            String word = token.toLowerCase().trim();

            if (word.contains(Constantes.VOCAL_INGREDIENT)) {
                return "ingredient";
            } else if (word.contains(Constantes.VOCAL_RECETTE)) {
                return "recette";
            } else if (word.contains(Constantes.VOCAL_SELECTION)) {
                return "choisir";
            } else if (word.contains((Constantes.VOCAL_ETAPE))){
                return "étape";
            }
        }
        return "no menu word found";
    }

    void doPerms()
    {
        int MY_PERMISSIONS_RECORD_AUDIO = 1;
        MainActivity thisActivity = this;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE},
                    MY_PERMISSIONS_RECORD_AUDIO);
        }
    }

    public void reqPostRecette(RecetteSimple recette, String URL) throws JSONException {
        Map<String, String> params = Tools.getParamsRecette(recette);

        VolleyUtils.POST_METHOD(MainActivity.this, URL,params, new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                try {
                    if (response.toString().trim().charAt(0) == '{'){
                        JSONObject jsonRecettes = new JSONObject(response.toString());
                        JSONArray arrayRecette = jsonRecettes.getJSONArray("recettes");
                        JSONObject jsonRecette = (JSONObject) arrayRecette.get(0);
                        currentRecette = new Recette(jsonRecette);
                        initializeWidgets();
                        initializeEvents();
                        modifyWidgets();

                    } else {
                        Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(String message) {
                System.out.println("Error : " + message);
            }
        });
    }

    private void modifyWidgets(){
        nomRecette.setText(currentRecette.getNom());
        resumeRecette.setText(currentRecette.getResume());
        initializeList();
        initializeEtape(0);
    }
    public void reqPost(final String result[], final String URL_POST) throws JSONException {
        Map<String, String> params = Tools.getParams(result);

        VolleyUtils.POST_METHOD(this, URL_POST,params, new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                try {
                    if (response.toString().trim().charAt(0) == '{') {
                        JSONArray jsonArray = new JSONObject(response.toString()).getJSONArray("recettes");
                        ArrayList<RecetteSimple> recettes = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonRecette = jsonArray.getJSONObject(i);
                            recettes.add(new RecetteSimple(jsonRecette));
                        }
                        listRecettes = findViewById(R.id.recettes_list);
                        recettesAdapter = new RecettesAdapter(MainActivity.this, recettes);
                        listRecettes.setAdapter(recettesAdapter);
                        initializeEventList();
                    } else {
                        System.out.println(response.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                System.out.println("Error : " + message);
            }
        });
    }

    public String transformResultatRecognition(Bundle resultats){
        ArrayList<String> arrayResultats = resultats.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String resultat = "";
        for (String words:arrayResultats) {
            resultat+=words+" ";
        }
        return resultat.trim();
    }

    public void callPopup() {
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.pop_up, null);
        int width = 500;
        int height = 500;
        boolean focusable = true;
        popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }

    public void loadRecetteComplete(RecetteSimple recette){
        try {
            setLayout(R.layout.activity_recette);
            reqPostRecette(recette, Constantes.URL_RECETTE_COMPLETE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void lireEtapeX(String[] etape){
        int numberEtape = Tools.extractNumber(etape);
        if(goToEtapeX(numberEtape-1)){

        }
    }

    public boolean goToEtapeX(int numberEtape) {
        if (numberEtape >0 && numberEtape<currentRecette.split_etapes().length){
            indexEtape = numberEtape;
            initializeEtape(indexEtape);
            return true;
        }
        return false;
    }
    @Override
    public void onReadyForSpeech(Bundle params) {
        System.out.println("Prêt");
    }

    @Override
    public void onBeginningOfSpeech() {
        System.out.println("Début");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        System.out.println("Buffer");
    }

    @Override
    public void onEndOfSpeech() {
        System.out.println("Fin du speech");
    }

    @Override
    public void onError(int error) {
        System.out.println("ERROR " + error);
        if (error == 6 || error == 5 || error == 7  || error == 8 ){
            speechRecognizer.destroy();
            speechRecognizer.setRecognitionListener(this);
            speechRecognizer.startListening(recognizerIntent);
        }
    }

    @Override
    public void onResults(Bundle results) {
        String resultat = transformResultatRecognition(results);
        System.out.println(resultat);
        String resultat_split[] = resultat.split(" ");
        if (resultat.contains(Constantes.VOCAL_ACTIVATE) && !listenning){
            System.out.println("ECOUTE");
            listenning = true;
            audioManager.adjustVolume(AudioManager.ADJUST_UNMUTE, 0);
            callPopup();
            speechRecognizer.startListening(recognizerIntent);
            audioManager.adjustVolume(AudioManager.ADJUST_MUTE, 0);
        } else if(listenning) {
            AssetManager assetManager=getAssets();

            try {
                String wordUser = this.findMenuWord(resultat_split).toLowerCase().trim();

                System.out.println("WORD USER : " + wordUser);

                if (!wordUser.equals("no menu word found")) {
                    if (wordUser.equals("choisir")){
                        if ((flagLayout == R.layout.list_recettes)&&!recettesAdapter.isEmpty()){
                            for (int i =0; i<recettesAdapter.getCount(); i++){
                                if (resultat.toLowerCase().trim().contains(recettesAdapter.getItem(i).getNom().toLowerCase().trim())){
                                    RecetteSimple recetteSimple = recettesAdapter.getItem(i);
                                    loadRecetteComplete(recetteSimple);
                                    break;
                                }
                            }
                        }
                    } else if(wordUser.equals("étape")) {
                        if (flagLayout == R.layout.activity_recette) {
                            lireEtapeX(resultat_split);
                        }
                    } else {
                        Vocabulaire vocabulaire = new Vocabulaire(assetManager, wordUser);
                        String motInVoca = vocabulaire.find(resultat_split);
                        System.out.println("MOT DU VOCABULAIRE : " + motInVoca);
                        if (!motInVoca.equals("word not found")){
                            System.out.println(wordUser+"_"+motInVoca);
                            setLayout(R.layout.list_recettes);
                            reqPost(resultat_split, Tools.chooseCorrectUrl(wordUser));
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            listenning = false;
            popupWindow.dismiss();
        }
        speechRecognizer.startListening(recognizerIntent);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}
