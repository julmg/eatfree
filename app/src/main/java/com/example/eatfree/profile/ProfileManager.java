package com.example.eatfree.profile;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.eatfree.MainActivity;
import com.example.eatfree.profile.check.CheckProfileView;
import com.example.eatfree.profile.create_edit.CreateProfileModel;
import com.example.eatfree.profile.create_edit.CreateProfileView;

import java.util.Observable;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class ProfileManager
//! \author Solène Tessiore
//! singleton qui permet de gérer les données de l'utilisateur
//! elle stocke le nom, la date de naissance et les allergènes de l'utilisateur
//! cette classe est observée par la vue du profil afin de pouvoir la mettre à jour
////////////////////////////////////////////////////////////////////////////////////////////////
public class ProfileManager extends Observable {
    //! énumération des familles d'allergènes
    public enum Allergenes {Arachides, Lait, Oeufs, Gluten, FruitsLatex, FruitsRosacees, FruitsOleagineux};

    //! instance du singleton ProfileManager
    private static ProfileManager instance = null;

    //! stocke le nom de l'utilisateur
    private String username;

    //! stocke la date de naissance de l'utilisateur
    private String birth;

    //! stocke les allergènes de l'utilisateur
    private Allergenes [] allergenes;

    //! stocke les allergènes de l'utilisateur sous forme de booléens
    private boolean [] boolAllergenes;

    //! référence vers CreateProfileView
    CreateProfileView createProfileView;

    //! stocke l'instance de la classe CreateProfileModel
    CreateProfileModel createProfileModel;

    //! référence vers CheckProfileView
    CheckProfileView checkProfileView;

    //! référence ver le MainActivity
    MainActivity activity;

    //! permet de récupérer l'intance du ProfileManager
    public static ProfileManager getInstance(){ return instance; }

    //! \brief constructeur qui initialise les données et qui gère les cas où l'utilisateur a déjà entré ses données ou non
    //! si il n'existe pas de données sauvegardées, on créé les pages et on dirige l'utilisateur vers la page de création de profil
    //! sinon on récupère les données sauvegardées et on dirige l'utilisateur sur la page de profil
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ProfileManager(MainActivity act, Context context){
        activity = act;
        instance = this;
        createProfileModel = CreateProfileModel.getInstance();

        allergenes = new Allergenes[]{null, null, null, null, null, null, null};
        boolAllergenes = new boolean[]{false, false, false, false, false, false, false};

        if (!activity.IsSaved()){
            username = null;
            birth = null;

            createProfileView = new CreateProfileView(context);
            checkProfileView = new CheckProfileView(context);

            createProfileView.setActivated(true);
            checkProfileView.setActivated(false);

            activity.setContentView(createProfileView);
        }
        else {
            String name = activity.preferences.getString("username", "");
            String birth = activity.preferences.getString("birth", "");
            boolean arachides = activity.preferences.getBoolean("arachides", false);
            boolean oeufs = activity.preferences.getBoolean("oeufs", false);
            boolean lait = activity.preferences.getBoolean("lait", false);
            boolean gluten = activity.preferences.getBoolean("gluten", false);
            boolean fruitsLatex = activity.preferences.getBoolean("fruitsLatex", false);
            boolean fruitsRosacees = activity.preferences.getBoolean("fruitsRosacees", false);
            boolean fruitsOleagineux = activity.preferences.getBoolean("fruitsOleagineux", false);
            createProfileModel.Validate(name, birth, arachides, oeufs, lait, gluten, fruitsLatex, fruitsRosacees, fruitsOleagineux);

            createProfileView = new CreateProfileView(context);
            checkProfileView = new CheckProfileView(context);

            createProfileView.setActivated(false);
            checkProfileView.setActivated(true);
        }
    }

    //! change la valeur du nom de l'utilisateur
    public void SetName(String name){
        username = name;
        setChanged();
        notifyObservers();
    }

    //! récupère le nom de l'utilisateur
    public String GetName(){
        return username;
    }

    //! change la valeur de la date de naissance de l'utilisateur
    public void SetBirth(String born){
        birth = born;
        setChanged();
        notifyObservers();
    }

    //! récupère la date de naissance de l'utilisateur
    public String GetBirth(){
        return birth;
    }

    //! change les valeurs des allergènes de l'utilisateur
    public void SetAllergenes(Allergenes allergene, int index){
        allergenes[index] = allergene;
        setChanged();
        notifyObservers();
    }

    //! récupère les allergènes de l'utilisateur
    public Allergenes[] GetAllergenes(){
        return allergenes;
    }

    //! change les valeurs des allergènes de l'utilisateur sous forme de booléens
    public void SetBoolAllergenes(boolean bool, int index){
        boolAllergenes[index] = bool;
    }

    //! récupère les allergènes de l'utilisateur sous forme de booléens
    public boolean[] GetBoolAllergenes(){
        return boolAllergenes;
    }

    //! récupère la référence vers le MainActivity
    public MainActivity GetActivity() {
        return activity;
    }

    //! récupère la référence vers le CheckProfileView
    public CheckProfileView GetProfileView(){
        return checkProfileView;
    }

    //! permet de gérer les changements entre les panels, passer de la création de profil à la page de profil ou inversement
    public void SwapProfilePanels(){
        if (createProfileView != null && createProfileView.isActivated()) {
            checkProfileView.setActivated(true);
            createProfileView.setActivated(false);
            activity.setContentView(checkProfileView);
        }
        else if (checkProfileView != null && checkProfileView.isActivated()) {
            checkProfileView.setActivated(false);
            createProfileView.setActivated(true);
            activity.setContentView(createProfileView);
        }
    }
}
