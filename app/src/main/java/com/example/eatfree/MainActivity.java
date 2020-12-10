package com.example.eatfree;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.eatfree.PriseDePhoto.ManagerPhoto;
import com.example.eatfree.profile.ProfileManager;
import com.example.eatfree.photo.PhotoModel;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST = 1;
    private boolean permissionsGranted;

    //! stocke les données sauvegardées de l'utilisateur
    public SharedPreferences preferences;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProfileManager profileManager = new ProfileManager(this, this);
        ManagerPhoto manager= ManagerPhoto.getInstance(this);
        if(IsSaved()==true){
            manager.viewPhoto.setActivated(true);
            setContentView(manager.viewPhoto);
        }
        else
            manager.viewPhoto.setActivated(false);

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
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)
        {
            permissionsGranted = true;
        } else if(isFirstTry){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST);
        } else {
            permissionsGranted = false;
        }
        return permissionsGranted;
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
            Bitmap image = BitmapFactory.decodeFile(manager.mdlPhoto.photo_path);
            manager.viewPhoto.affichePhoto.setImageBitmap(image);
            PhotoModel annalyseAllergène = new PhotoModel(this);
           try {
               Map<String, ArrayList<String>> result = annalyseAllergène.findAllergenesWithBarcodeOFF(image);
               Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
           }
           catch (Exception e){
               Toast.makeText(this, "aucun code barre détecté", Toast.LENGTH_SHORT).show();
               //annalyseAllergène.findAllergenesWithOCR(image);
           }
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

                    permissionsGranted = true;
                } else {

                    permissionsGranted = false;
                }
            }


        }
    }
}