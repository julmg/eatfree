package com.example.eatfree.profile.create_edit;

import android.view.View;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class CreateProfileController
//! \author Solène Tessiore
//! Controller de la création de profil
//! Gère le clic sur le bouton Valider de la vue
////////////////////////////////////////////////////////////////////////////////////////////////
public class CreateProfileController implements View.OnClickListener {
    //! référence vers la vue
    private CreateProfileView view;

    //! \brief constructeur
    //! lie la vue et le controller
    public CreateProfileController(CreateProfileView v){
        view = v;
    }


    //! gère le clic sur le bouton valider pour enregistrer  les données saisies par l'utilisateur
    @Override
    public void onClick(View v) {
        if (v.getId() == view.btnValidate.getId()){
            CreateProfileModel.getInstance().Validate(view.username.getText().toString(), view.dateOfBirth.getText().toString(), view.arachides.isChecked(), view.oeuf.isChecked(), view.lait.isChecked(), view.gluten.isChecked(), view.fruitsLatex.isChecked(), view.fruitsRosacees.isChecked(), view.fruitsOleagineux.isChecked() );
        }
    }
}
