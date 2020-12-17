package com.example.eatfree.settings;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.example.eatfree.R;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class SettingsView
//! \author Solène Tessiore
//! Vue de la page de settings
////////////////////////////////////////////////////////////////////////////////////////////////
public class SettingsView extends LinearLayout {

    //brief bouton qui renvoie a la page de profil
    public Button btnProfil;

    //brief bouton qui renvoie a la page d'accueil
    public Button btnAccueil;

    SettingsModel model;
    SettingsController controller;

    //! \brief Constructeur
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public SettingsView(Context context) {
        super(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        inflater.inflate(R.layout.settings, this);

        btnProfil = (Button)findViewById(R.id.btnProfil);
        btnAccueil = (Button)findViewById(R.id.btnAccueil);

        model = new SettingsModel();
        controller = new SettingsController(model, this);

        btnProfil.setOnClickListener(controller);
        btnAccueil.setOnClickListener(controller);
    }
}
