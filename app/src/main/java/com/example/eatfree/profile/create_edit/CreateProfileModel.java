package com.example.eatfree.profile.create_edit;

import com.example.eatfree.profile.ProfileManager;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class CreateProfileModel
//! \author Solène Tessiore
//! singleton qui est le modèle de la création de profil
//! il gère la modification et l'enregistement des information à propos de l'utilisateur
////////////////////////////////////////////////////////////////////////////////////////////////
public class CreateProfileModel {
    //! contient l'instance du ProfileManager
    private ProfileManager profileManager;

    //! instance du singleton CreateProfileModel
    private static CreateProfileModel instance;

    //! \brief constructeur
    //! récupère l'instance de ProfilManager
    public CreateProfileModel(){
        profileManager = ProfileManager.getInstance();
    }

    //! récupère l'instance de CreateProfilModel et la créé si elle est inexistante
    public static CreateProfileModel getInstance(){
        if (instance == null){
            instance = new CreateProfileModel();
        }
        return instance;
    }

    //! enregistre les données entrée par l'utilisateur dans le ProfileManager
    //! sauvegarde le profil de l'utilisateur
    //! redirige l'utilisateur vers sa page de profil
    public void Validate(String name, String date, boolean arachides, boolean oeuf, boolean lait, boolean gluten, boolean fruitsLatex, boolean fruitsRosacees, boolean fruitsOleagineux ){
        int index = 0;
        profileManager.SetName(name);
        profileManager.SetBirth(date);

        for(int i = 0; i < profileManager.GetAllergenes().length; i++){
            profileManager.SetAllergenes(null, i);
            profileManager.SetBoolAllergenes(false, i);
        }

        if(arachides){
            profileManager.SetAllergenes(ProfileManager.Allergenes.Arachides, index);
            profileManager.SetBoolAllergenes(true, 0);
            index += 1;
        }
        if(oeuf){
            profileManager.SetAllergenes(ProfileManager.Allergenes.Oeufs, index);
            profileManager.SetBoolAllergenes(true, 1);
            index += 1;
        }
        if(lait){
            profileManager.SetAllergenes(ProfileManager.Allergenes.Lait, index);
            profileManager.SetBoolAllergenes(true, 2);
            index += 1;
        }
        if(gluten){
            profileManager.SetAllergenes(ProfileManager.Allergenes.Gluten, index);
            profileManager.SetBoolAllergenes(true, 3);
            index += 1;
        }

        if(fruitsLatex){
            profileManager.SetAllergenes(ProfileManager.Allergenes.FruitsLatex, index);
            profileManager.SetBoolAllergenes(true, 4);
            index += 1;
        }
        if(fruitsRosacees){
            profileManager.SetAllergenes(ProfileManager.Allergenes.FruitsRosacees, index);
            profileManager.SetBoolAllergenes(true, 5);
            index += 1;
        }
        if(fruitsOleagineux){
            profileManager.SetAllergenes(ProfileManager.Allergenes.FruitsOleagineux, index);
            profileManager.SetBoolAllergenes(true, 6);
        }

        profileManager.GetActivity().Save();
        profileManager.SwapProfilePanels();
    }
}
