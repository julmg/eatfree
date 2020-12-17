package com.example.eatfree.PriseDePhoto;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.core.app.ActivityCompat;

import com.example.eatfree.MainActivity;
import com.example.eatfree.PanelManager;

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
        else if (v.getId() == refVue.btnProfil.getId()){
            PanelManager.getInstance().SwapPanel(1);
        }
        else if (v.getId() == refVue.btnSettings.getId()){
            PanelManager.getInstance().SwapPanel(3);

        }
    }
    //! \brief verifie s'il y a acces a l'appareil photo et prends la photo
    private void takePhotoAndVerifyPermissions(){
        if(refActivity.checkPermissions(true)){
            refMod.prendrePhoto();
        }
    }

    //! \brief renvoie une reference sur le modele correspondant
    public void setRefMod(Mod_photo m) {
        refMod = m;
    }
    //! \brief renvoie une reference sur la vue correspondante

    public void setRefVue(Vue_photo v) {
        refVue = v;
    }

}

