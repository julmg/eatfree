package com.example.eatfree.profile.check;

import android.view.View;

import com.example.eatfree.profile.ProfileManager;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class CheckProfileController
//! \author Solène Tessiore
//! gère l'affichage des allergènes et le clic sur le bouton modifier
////////////////////////////////////////////////////////////////////////////////////////////////
public class CheckProfileController implements View.OnClickListener {
    //! référence vers le CheckProfileController
    private CheckProfileModel model;

    //! référence vers le CheckProfileView
    private CheckProfileView view;

    //! \brief constructeur
    //! initialise les références vers CheckProfileController et CheckProfileView
    public CheckProfileController(CheckProfileModel m, CheckProfileView v){
        model = m;
        view = v;
    }

    //! gère l'affichage de la liste des allergies de l'utilisateur
    public void DisplayAllergenes() {
        String allergeneList = "";
        for (ProfileManager.Allergenes allergene : ProfileManager.getInstance().GetAllergenes()) {
            if (allergene != null){
                if (allergene == ProfileManager.Allergenes.Lait) {
                    allergeneList += "Lait de vache\n";
                }
                else if (allergene == ProfileManager.Allergenes.FruitsLatex) {
                    allergeneList += "Fruits du groupe latex\n";
                }
                else if (allergene == ProfileManager.Allergenes.FruitsRosacees) {
                    allergeneList += "Fruits Rosacées\n";
                }
                else if (allergene == ProfileManager.Allergenes.FruitsOleagineux) {
                    allergeneList += "Fruits Oléagineux\n";
                }
                else {
                    allergeneList += allergene.toString() + "\n";
                }
            }
        }
        view.allergenes.setText(allergeneList);
    }

    //! gère le clic sur le bouton modifier et dirige l'utilisateur vers la page de modification du profil
    @Override
    public void onClick(View v) {
        if (v.getId() == view.btnEdit.getId()){
            model.Edit();
        }
    }
}
