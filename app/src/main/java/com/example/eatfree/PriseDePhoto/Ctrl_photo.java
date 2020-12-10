package com.example.eatfree.PriseDePhoto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.example.eatfree.MainActivity;

import static androidx.core.app.ActivityCompat.requestPermissions;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class Ctrl_photo
//! \author Picot Karolane / Monneraye Aurore
//! Contrôleur de la prise de photo
//! gère le click sur le bonton photo
////////////////////////////////////////////////////////////////////////////////////////////////


public class Ctrl_photo implements View.OnClickListener{
    public Vue_photo refVue;
    public Mod_photo refMod;
    public MainActivity refActivity;



    @Override
    public void onClick(View v) {
        if(refVue.buttonPhoto.getId()==v.getId()) {
            takePhotoAndVerifyPermissions();
        }
    }

    private void takePhotoAndVerifyPermissions(){
        if(refActivity.checkPermissions(true)){
            refMod.prendrePhoto();
        }
    }

    public void setRefMod(Mod_photo m) {
        refMod = m;
    }

    public void setRefVue(Vue_photo v) {
        refVue = v;
    }

}

