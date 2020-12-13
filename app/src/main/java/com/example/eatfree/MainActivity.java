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
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.eatfree.PriseDePhoto.ManagerPhoto;
import com.example.eatfree.profile.ProfileManager;
import com.example.eatfree.photo.PhotoModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST = 1;
    private boolean mPermissionsGranted;
    private ProgressDialog mProgressDialogOCR, mProgressDialogOFF;
    private Bitmap mImage;
    private PhotoModel mPhotoModel;

    //! stocke les données sauvegardées de l'utilisateur
    public SharedPreferences preferences;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProfileManager profileManager = new ProfileManager(this, this);
        ManagerPhoto manager= ManagerPhoto.getInstance(this);
        PanelManager panelManager = new PanelManager(this, this);
        if(IsSaved()){
            manager.viewPhoto.setActivated(true);
            setContentView(manager.viewPhoto);
        }
        else
            manager.viewPhoto.setActivated(false);

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
     * @brief revois le résultat de la prise de photo pour l'afficher sur la vue grâce au fichier temporaire (path)
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ManagerPhoto manager= ManagerPhoto.getInstance(this);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == manager.mdlPhoto.RETOUR_PRENDRE_PHOTO && resultCode == RESULT_OK) {
            mImage = BitmapFactory.decodeFile(manager.mdlPhoto.photo_path);
            manager.viewPhoto.affichePhoto.setImageBitmap(mImage);
            mPhotoModel = new PhotoModel(this);
            photoRecognition();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mPermissionsGranted = true;
                } else {

                    mPermissionsGranted = false;
                }
            }


        }
    }

    private void photoRecognition() {

        if (mProgressDialogOFF == null) {
            mProgressDialogOFF = ProgressDialog.show(this, "Détection du code barre",
                    "Veuillez patienter...", true);
        } else {
            mProgressDialogOFF.show();
        }
        new Thread(new Runnable() {
            public void run() {
                final Map<String, ArrayList<String>> result;
                boolean success=false;
                try {
                    result = mPhotoModel.findAllergenesWithBarcodeOFF(mImage);
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialogOFF.dismiss();
                            Log.w("DEBUG","ERREUR : "+e.toString());
                            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:

                                        new Thread(new Runnable() {


                                            public void run() {
                                                photoRecognitionOCR();
                                            }
                                        }).start();
                                        if (mProgressDialogOCR == null) {
                                            mProgressDialogOCR = ProgressDialog.show(MainActivity.this, "Détection des caractères",
                                                    "Veuillez patienter, cette opération peut prendre jusqu'à une minute", true);
                                        } else {
                                            mProgressDialogOCR.show();
                                        }
                                        break;
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage("Code barre non reconnu. Voulez-vous effectuer une reconnaissance de caractères ?").setPositiveButton("Oui", dialogClickListener)
                                    .setNegativeButton("Non", dialogClickListener).show();
                        }
                    });
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialogOFF.dismiss();
                        photoRecognitionResult(result);
                    }
                });
            }
        }).start();
    }

    private void photoRecognitionOCR()  {

        ExecutorService executor = Executors.newFixedThreadPool(1);
        OCRTask task = new OCRTask();
        List<Future<Map<String, ArrayList<String>>>> futureResult = null;
        Map<String, ArrayList<String>> result = null;
        try{
            futureResult = executor.invokeAll(Arrays.asList(task));


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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialogOCR.dismiss();
                if (finalResult == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Texte non reconnu")
                            .setCancelable(false)
                            .setPositiveButton("ok", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    photoRecognitionResult(finalResult);
                }
            }
        });

    }

    private void photoRecognitionResult(Map<String, ArrayList<String>> result) {
        Log.i("DEBUG","Résultat : "+result.entrySet().toArray().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(Arrays.toString(result.entrySet().toArray()))
                .setCancelable(false)
                .setPositiveButton("ok",null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    class OCRTask implements Callable<Map<String, ArrayList<String>>>
    {
        @Override
        public Map<String, ArrayList<String>> call() throws Exception {
            final Map<String, ArrayList<String>> result;
            try {
                result = mPhotoModel.findAllergenesWithOCR(mImage);
            } catch (Exception e){
                Log.e("DEBUG",e.toString());
                return null;
            }
            return result;

        }
    }

}