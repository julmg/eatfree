package com.example.eatfree.profile.check;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.eatfree.profile.ProfileManager;
import com.example.eatfree.R;

import java.util.Observable;
import java.util.Observer;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class ProfileManager
//! \author Solène Tessiore
//! vue de la page de profil
////////////////////////////////////////////////////////////////////////////////////////////////
public class CheckProfileView extends LinearLayout implements Observer {
    //! affichage du nom de l'utilisateur
    public TextView username;

    //! affichage de la date de naissance de l'utilisateur
    public TextView dateOfBirth;

    //! affichage de la liste des allergies de l'utilisateur
    public TextView allergenes;

    //! bouton de modification du profil
    public Button btnEdit;

    //! référence vers le CheckProfileModel
    private CheckProfileModel model;

    //! référence vers le CheckProfileController
    private CheckProfileController controller;

    //! \brief constructeur de la classe CheckProfileView
    //! il lie le fichier check_profile.xml, responsable du visuel, aux fichiers qui gèrent l'affichage des données
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public CheckProfileView(Context context) {
        super(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        inflater.inflate(R.layout.check_profile, this);

        username = (TextView)findViewById(R.id.seeusername);
        dateOfBirth = (TextView)findViewById(R.id.seedateOfBirth);
        allergenes = (TextView)findViewById(R.id.textAllergies);
        btnEdit = (Button)findViewById(R.id.edit);

        username.setText(ProfileManager.getInstance().GetName());
        dateOfBirth.setText(ProfileManager.getInstance().GetBirth());

        model = new CheckProfileModel();
        controller = new CheckProfileController(model, this);

        controller.DisplayAllergenes();
        btnEdit.setOnClickListener(controller);

        ProfileManager.getInstance().addObserver(this);
    }

    //! modifie l'affichage des données utilisateur lorsque celles ci sont modifiées
    //! cette fonction lie l'observer CheckProfileView et l'observable ProfileManager
    //! en cas de modifications dans ProfileManager, les informations de la vue se mettent à jour
    @Override
    public void update(Observable o, Object arg) {
        username.setText(ProfileManager.getInstance().GetName());
        dateOfBirth.setText(ProfileManager.getInstance().GetBirth());
        controller.DisplayAllergenes();
    }
}
