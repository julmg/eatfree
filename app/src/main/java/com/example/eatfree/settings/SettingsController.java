package com.example.eatfree.settings;

import android.view.View;

import com.example.eatfree.PanelManager;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class SettingsController
//! \author Solène Tessiore
//! Controller de la page de settings
//! Gère le clic sur les boutons de la vue
////////////////////////////////////////////////////////////////////////////////////////////////
public class SettingsController implements View.OnClickListener{
    //! Référence vers SettingsModel
    SettingsModel model;

    //! Référence vers SettingsView
    SettingsView view;

    //! \brief Constructeur
    public SettingsController(SettingsModel m, SettingsView v) {
        model = m;
        view = v;
    }


    //! \brief Gère le clic sur les boutons
    @Override
    public void onClick(View v) {
        if (v.getId() == view.btnProfil.getId()){
            model.ChangePanel(1);
        }
        else if (v.getId() == view.btnAccueil.getId()){
            model.ChangePanel(2);

        }
    }
}
