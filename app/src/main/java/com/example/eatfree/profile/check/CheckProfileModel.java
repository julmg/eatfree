package com.example.eatfree.profile.check;

import com.example.eatfree.profile.ProfileManager;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class CheckProfileModel
//! \author Solène Tessiore
//! singleton qui permet de gérer les données de l'utilisateur
//! elle stocke le nom, la date de naissance et les allergènes de l'utilisateur
//! cette classe est observée par la vue du profil afin de pouvoir la mettre à jour
////////////////////////////////////////////////////////////////////////////////////////////////
public class CheckProfileModel {
    //! contient l'instance de la classe ProfileManager
    private ProfileManager profileManager;

    //! \brief constructeur
    //! lie CheckProfileModel et l'instance du ProfileManager
    public CheckProfileModel(){
        profileManager = ProfileManager.getInstance();
    }

    //! permet d'aller sur le panel de modification de profil
    void Edit(){
        profileManager.SwapProfilePanels();
    }
}
