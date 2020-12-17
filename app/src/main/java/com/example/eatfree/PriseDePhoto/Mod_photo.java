package com.example.eatfree.PriseDePhoto;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class Mod_photo
//! \author Picot Karolane / Monneraye Aurore
//! Modèle de la prise de photo
//! gère la fonction servant à prendre une photo et la sauvergarder temporairement
////////////////////////////////////////////////////////////////////////////////////////////////

public class Mod_photo {
    //constante
    public static final int RETOUR_PRENDRE_PHOTO = 1;

    //référence
    public Vue_photo refVue;
    public Ctrl_photo refCtrl;
    public Activity refAct;
    //variable
    public String photo_path = null;

    /**
     * @brief  prendrePhoto: méthode qui va avoir acces à l'appareil photo et qui va la mémoriser dans un fichier temporaire l'image prise.
     * on créera donc un intente qui ouvrira une fenetre afin de prendre la photo
     * on controle aussi la gestion de l'intent afin que l'image soit utilisable
     * afin de récupérer plus facilement la photo nous la nommerons par la date et la minute de sa creation
     * on enregistre le chemin complet
     * on créer un URI et on le transfere afin de pouvoir enregistrer temporairement la photp
     */
    public void prendrePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(refAct.getPackageManager()) != null) {
            String time = new SimpleDateFormat("yyyymmdd_HHmmss").format(new Date());
            File photoDir = refAct.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File photoFile = File.createTempFile("photo" + time, ".jpg", photoDir);
                photo_path = photoFile.getAbsolutePath();
                Log.i("path",photo_path);
                Uri photoUri = FileProvider.getUriForFile(refAct,
                        refAct.getApplicationContext().getPackageName() + ".provider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                refAct.startActivityForResult(intent, RETOUR_PRENDRE_PHOTO);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //! \brief reference vers le controller des photos
    public void setRefCtrl(Ctrl_photo ct) {
        refCtrl = ct;
    }

    //! \brief reference vers la vue des photos
    public void setRefVue(Vue_photo v) {
        refVue = v;
    }

    //! \brief Reference vers le modele des photos
    public void setRefAct(Activity a){refAct = a;}

    public String getPhoto_path() {
        Log.i("a",photo_path);
        return photo_path;
    }

}

