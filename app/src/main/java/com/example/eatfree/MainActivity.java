package com.example.eatfree;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
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
import android.provider.Settings;

import com.example.eatfree.PriseDePhoto.ManagerPhoto;
import com.example.eatfree.PriseDePhoto.Mod_photo;
//import com.example.eatfree.comparaison.ModelePanel;
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

import javax.xml.transform.Result;

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

    //! référence vers le ControllerPanel
    //public ModelePanel modelePanel;

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
     * @brief Affichage du résultat du traitement de l'image
     * @param result map des allergènes
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void photoRecognitionResult(Map<String, ArrayList<String>> result) {
        ResultManager.getInstance().triAndSetResult(result);
        PanelManager.getInstance().SwapPanel(4);
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

}