package com.example.eatfree.PriseDePhoto;

import android.view.View;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class Ctrl_photo
//! \author Picot Karolane / Monneraye Aurore
//! Contrôleur de la prise de photo
//! gère le click sur le bonton photo
////////////////////////////////////////////////////////////////////////////////////////////////


public class Ctrl_photo implements View.OnClickListener{
    public Vue_photo refVue;
    public Mod_photo refMod;



    @Override
    public void onClick(View v) {
        if(refVue.buttonPhoto.getId()==v.getId()) {
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

