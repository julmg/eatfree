package com.example.eatfree.PriseDePhoto;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.eatfree.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class Vue_photo
//! \author Picot Karolane / Monneraye Aurore
//! Vue de la prise de photo
//! gère la partie graphique de la prise de photo (affecte le bouton, l'emplacement de la photo)
////////////////////////////////////////////////////////////////////////////////////////////////

public class Vue_photo extends LinearLayout {


    //variable
    public FloatingActionButton buttonPhoto;
    public ImageView affichePhoto;

    //référence
    public Mod_photo refMod;
    public Ctrl_photo refCtr;

    public Vue_photo(Context context) {
        super(context);

        LayoutInflater inflater= ((Activity)context).getLayoutInflater();
        inflater.inflate(R.layout.activity_main, this);

        buttonPhoto = (FloatingActionButton) findViewById(R.id.btn_photo);
        affichePhoto = (ImageView) findViewById(R.id.imgAffichePhoto);
    }

    public void setRefCtr(Ctrl_photo ct) {
        refCtr = ct;
        buttonPhoto.setOnClickListener(refCtr);
    }

    public void setRefMod(Mod_photo m){
        refMod=m;
    }
}

