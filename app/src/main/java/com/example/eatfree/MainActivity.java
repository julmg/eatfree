package com.example.eatfree;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.eatfree.PriseDePhoto.ManagerPhoto;
import com.example.eatfree.PriseDePhoto.Mod_photo;

import com.example.eatfree.profile.ProfileManager;
import com.example.eatfree.photoUtils.PhotoUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/*!
 * \mainpage Présentation de l'application EATFREE

* \section  sec1 Présentation
 * L’application permet de créer son profil avec les allergènes/intolérances associées. Une fois ce profil créé, le client peut en magasin ouvrir l’application et avec la caméra
 *  et scanner la liste des ingrédients. Grâce à la base de données, l’application repère les ingrédients spécifiés « allergène » dans le profil. Si les ingrédients sont adaptés
 * au profil, un pouce vert apparait et indique que le consommateur peut manger ce produit, si malheureusement la liste comporte un des ingrédients allergènes pour le consommateur,
 *  un pouce rouge apparait, indiquant qu’il ne peut pas consommer ce produit.
 *
 *
 *\section  sec2 Le public visé
 * Les personnes ayant des intolérances alimentaires : arachides, œuf, lait de vache, gluten, fruits.
 *  Consommateur en âge de faire les courses seul : à partir de 18 ans.
 *
 * \section  sec3 Le contexte
 * Les intolérances alimentaires touchent de plus en plus de personnes. L’emballage et le numérique peuvent aider ces personnes à contrôler leur alimentation en leur indiquant si oui ou non
 *  ils peuvent manger le produit via la liste des ingrédients.
 *
 *
 *\section  sec4 Les systèmes, les sous-systèmes ou les équipements
 *  Application disponible sur smartphones récents (- de 5 ans) qui fonctionnent sur les systèmes d’exploitation les plus utilisés : Android de Google, Windows de Microsoft et iOS de Apple.
 *
 *\section  sec5 Securité
 * Les données personnelles privées ne pourront pas etre utiliser en dehors de l’utilisation de l’application
 *
 *Pour protéger les concepteurs de l’application, acceptation de l’utilisateur d'une décharge de responsabilité
 * en cas d’erreur de la part du logiciel et de problème de santé induit chez le consommateur.
 *
 *\subsection  sec fonctionnalitées disponible sur l'application
 * \li Création d’un profil personnalisé par l’utilisateur afin de renseigner ses allergies
 * \li Base de données avec tous les noms des potentiels allergènes
 * \li Détection visuelle (caméra) des ingrédients présents dans le produit
 * \li Analyse et comparaison entre le profil client et le produit
 * \li Affichage simplifié de la réponse pour le consommateur (feu vert/feu rouge ou pouce vert/pouce rouge)
 *
 * \subsection sec Étudiants sur le projet
 *
 * \li Étudiants de l'ESEPAC sur ce projet : Emilie CHASSONNAUD, Tiphaine COUSINARD, Mathilde SAISON-SCHREVERE, Lucie THOMAS, Garance THUIZAT
 * \li Étudiants de l'IUT du Puy-en-Velay sur ce projet : Aurore MONNERAYE, Emma PROVOT, Solène TESSIORE, Maxime POIRIER, Nathan LEFEBVRE, Baptiste MADELAINE, Karolane PICOT, Julian LECOCQ--MAGE
 */

public class MainActivity extends AppCompatActivity {

    //! Code de requète de demande de permissions
    public static final int MY_PERMISSIONS_REQUEST = 1;

    //! Indique si les permissions ont été autorisées par l'utilisateur
    private boolean mPermissionsGranted;

    //! Correspond aux fenêtres de progression de reconnaissance photo
    private ProgressDialog mProgressDialogOCR, mProgressDialogOFF;

    //! Image prise en photo
    private Bitmap mImage;

    //! stocke les données sauvegardées de l'utilisateur
    public SharedPreferences preferences;

    private LinearLayout resultTable;
    private ImageView resultPouce;
    private Button resultButton;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProfileManager profileManager = new ProfileManager(this, this);
        PanelManager panelManager = new PanelManager(this, this);
        if(IsSaved()){
            ManagerPhoto.getInstance(this).viewPhoto.setActivated(true);
            setContentView(ManagerPhoto.getInstance(this).viewPhoto);
        }
        else {
            ManagerPhoto.getInstance(this).viewPhoto.setActivated(false);
        }
        //exécution en mode strict
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    //! sauvegarde de manière permanente les données de l'utilisateur
    public void Save(){
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean("isSaved", true);
        editor.putString("username", ProfileManager.getInstance().GetName());
        editor.putString("birth", ProfileManager.getInstance().GetBirth());
        editor.putBoolean("arachides", ProfileManager.getInstance().GetBoolAllergenes()[0]);
        editor.putBoolean("oeufs", ProfileManager.getInstance().GetBoolAllergenes()[1]);
        editor.putBoolean("lait", ProfileManager.getInstance().GetBoolAllergenes()[2]);
        editor.putBoolean("gluten", ProfileManager.getInstance().GetBoolAllergenes()[3]);
        editor.putBoolean("fruitsLatex", ProfileManager.getInstance().GetBoolAllergenes()[4]);
        editor.putBoolean("fruitsRosacees", ProfileManager.getInstance().GetBoolAllergenes()[5]);
        editor.putBoolean("fruitsOleagineux", ProfileManager.getInstance().GetBoolAllergenes()[6]);

        editor.apply();
    }

    //! vérifie si l'utilisateur a déjà enregistrer des données
    //! pour vérifier si c'est sa première utilisation ou non
    public boolean IsSaved(){
        preferences = getPreferences(MODE_PRIVATE);
        return preferences.getBoolean("isSaved", false);
    }

    /**
     * Vérifie si les permissions sont acquises ou non, et les demande si elles ne le sont pas
     * @param isFirstTry si c'est le premier appel de cette fonction
     * @return un booléen correspondant à si les permissions ont été autorisées par l'utilisateur
     */
    public boolean checkPermissions(boolean isFirstTry){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)
        {
            mPermissionsGranted = true;
        } else if(isFirstTry){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST);
        } else {
            mPermissionsGranted = false;
        }
        return mPermissionsGranted;
    }

    /**
     * @brief renvoie le résultat de la prise de photo pour l'afficher sur la vue grâce au fichier temporaire (path)
     * @param requestCode code de requète
     * @param resultCode code de résultat
     * @param data données de l'activity
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ManagerPhoto manager = ManagerPhoto.getInstance(this);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Mod_photo.RETOUR_PRENDRE_PHOTO && resultCode == RESULT_OK) {
            mImage = BitmapFactory.decodeFile(manager.mdlPhoto.photo_path);
            manager.viewPhoto.affichePhoto.setImageBitmap(mImage);
            photoRecognition();
        }
    }

    /**
     * @brief Appelé lorsque l'utilisateur a fini d'accepter/refuser les permissions
     * @param requestCode code de requête
     * @param permissions permissions requises
     * @param grantResults résultat des requêtes
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                mPermissionsGranted = grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            }
        }
    }

    /**
     * @brief Lance le processus de reconnaissance d'image
     * @author Julian Lecocq--Mage
     */
    private void photoRecognition() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    new Thread(this::photoRecognitionOFF).start();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    new Thread(this::photoRecognitionOCR).start();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Quel support a été pris en photo ?")
                .setPositiveButton("Code-barres", dialogClickListener)
                .setNegativeButton("Liste d'ingrédients", dialogClickListener)
                .show();
    }

    /**
     * @brief Lance la reconnaissance d'image par code-barre (gestion UI et appel fonction utilitaire)
     * @author Julian Lecocq--Mage
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void photoRecognitionOFF() {
        runOnUiThread(() -> {
            if (mProgressDialogOFF == null) {
                mProgressDialogOFF = ProgressDialog.show(MainActivity.this, "Détection du code barre",
                        "Veuillez patienter...", true);
            } else {
                mProgressDialogOFF.show();
            }
        });
        final Map<String, ArrayList<String>> result;
        boolean success=false;
        try {
            result = PhotoUtils.findAllergenesWithBarcodeOFF(mImage);
        } catch (Exception e) {
            runOnUiThread(() -> {
                mProgressDialogOFF.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Code barre non reconnu ou introuvable dans la base de données OpenFoodFacts.\n")
                        .setPositiveButton("Ok", null)
                        .show();
            });
            return;
        }
        runOnUiThread(() -> {
            mProgressDialogOFF.dismiss();
            photoRecognitionResult(result);
        });
    }

    /**
     * @brief Lance la reconnaissance d'image par reconnaissance de caractères (gestion UI et appel fonction utilitaire)
     * @author Julian Lecocq--Mage
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void photoRecognitionOCR()  {
        runOnUiThread(() -> {
            if (mProgressDialogOCR == null) {
                mProgressDialogOCR = ProgressDialog.show(MainActivity.this, "Détection des caractères",
                        "Veuillez patienter, cette opération peut prendre jusqu'à une minute", true);
            } else {
                mProgressDialogOCR.show();
            }
        });
        ExecutorService executor = Executors.newFixedThreadPool(1);
        OCRTask task = new OCRTask();
        List<Future<Map<String, ArrayList<String>>>> futureResult = null;
        Map<String, ArrayList<String>> result = null;
        try{
            futureResult = executor.invokeAll(Collections.singletonList(task));
            result = futureResult.get(0).get(60, TimeUnit.SECONDS);
        }catch(TimeoutException e){
            mProgressDialogOCR.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Texte non reconnu (temps d'attente max dépassé)")
                    .setCancelable(false)
                    .setPositiveButton("ok",null);
            AlertDialog alert = builder.create();
            alert.show();
            futureResult.get(0).cancel(true);
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        futureResult.get(0).cancel(true);
        Map<String, ArrayList<String>> finalResult = result;
        runOnUiThread(() -> {
            mProgressDialogOCR.dismiss();
            if (finalResult == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Texte non reconnu")
                        .setPositiveButton("Ok", null).show();
            } else {
                photoRecognitionResult(finalResult);
            }
        });

    }


    /**
     * @brief Classe callable représentant l'appel de la fonction utilitaire d'OCR sur un autre thread, car la tâche est longue
     * @author Julian Lecocq--Mage
     */
    class OCRTask implements Callable<Map<String, ArrayList<String>>>
    {
        @Override
        public Map<String, ArrayList<String>> call() {
            final Map<String, ArrayList<String>> result;
            try {
                result = PhotoUtils.findAllergenesWithOCR(mImage,MainActivity.this);
            } catch (Exception e){
                return null;
            }
            return result;
        }
    }


    /**
     * @brief Affichage du résultat du traitement de l'image
     * @param result map des allergènes
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void photoRecognitionResult(Map<String, ArrayList<String>> result) {
        Map<String, ArrayList<String>> resulttrie = ResultUtils.triResult(result);

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Résultat");
        dialog.setContentView(R.layout.panel_allergene_check);

        resultTable = (LinearLayout) dialog.findViewById(R.id.tableAllergenes);
        resultPouce = (ImageView) dialog.findViewById(R.id.img_pouce);
        resultButton = (Button) dialog.findViewById(R.id.btnOkResultat);

        if(resulttrie.isEmpty()){
            resultPouce.setImageResource(R.drawable.pouce_vert);
        } else {
            resultPouce.setImageResource(R.drawable.pouce_rouge);
        }

        setAllergeneMap(resulttrie);

        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * Permet de remplir la fenêtre de résltats avec les résultats
     * @param resultMap la map de résultats
     */
    public void setAllergeneMap(Map<String, ArrayList<String>> resultMap){
        resultTable.removeAllViews();
        if(resultMap.isEmpty()){
            resultPouce.setImageResource(R.drawable.pouce_vert);
            addMessageRow("C'est bon ! A priori, il n'y a pas d'allergènes dans ce produit.");
        } else {
            resultPouce.setImageResource(R.drawable.pouce_rouge);
            addMessageRow("Attention, des allergènes ont été trouvés :");
            for (Map.Entry<String, ArrayList<String>> entry : resultMap.entrySet()) {
                addAllergeneRow(entry.getKey(),entry.getValue());
            }
        }
    }

    /**
     * Ajout d'une ligne correspondant à un allergène dans la fenêtre de résultats
     * @param allergene l'allergène
     * @param termes les termes s'y rapportant
     */
    public void addAllergeneRow(String allergene, ArrayList<String> termes){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;

        String stringtermes = android.text.TextUtils.join(", ", termes);
        LinearLayout linearLayout = new LinearLayout(MainActivity.this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        TextView allergeneTV = new TextView(MainActivity.this);
        allergeneTV.setGravity(Gravity.CENTER);
        allergeneTV.setText(allergene);
        allergeneTV.setLayoutParams(params);
        allergeneTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        allergeneTV.setSingleLine(false);
        linearLayout.addView(allergeneTV);

        TextView termesTV = new TextView(MainActivity.this);
        termesTV.setGravity(Gravity.CENTER);
        termesTV.setText(stringtermes);
        termesTV.setLayoutParams(params);
        allergeneTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        allergeneTV.setSingleLine(false);
        linearLayout.addView(termesTV);

        resultTable.addView(linearLayout);
    }

    /**
     * Ajout d'une première ligne correspondant au message juste en dessous du pouce
     * @param message le message à afficher
     */
    @SuppressLint("SetTextI18n")
    public void addMessageRow(String message){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;

        LinearLayout linearLayout = new LinearLayout(MainActivity.this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        TextView messageTV = new TextView(MainActivity.this);
        messageTV.setGravity(Gravity.CENTER);
        messageTV.setText(message);
        messageTV.setLayoutParams(params);
        messageTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        linearLayout.addView(messageTV);

        resultTable.addView(linearLayout);
    }

}