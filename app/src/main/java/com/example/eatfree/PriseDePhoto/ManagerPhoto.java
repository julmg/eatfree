package com.example.eatfree.PriseDePhoto;

import android.app.Activity;
import android.content.Context;

import com.example.eatfree.MainActivity;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class ManagerPhoto
//! \author Picot Karolane
//! manager de la prise de photo
//! singelton qui créer toutes les connections entre mon MCV
//! setPnl affiche la vue de l'acceuil
////////////////////////////////////////////////////////////////////////////////////////////////

public class ManagerPhoto {
    private static ManagerPhoto instance= null;

    private int pnl;

    public Vue_photo viewPhoto;
    private Ctrl_photo ctrlPhoto;
    public Mod_photo mdlPhoto;
    public Activity refAct;
    public MainActivity act;

    public ManagerPhoto(Context context) {
        ctrlPhoto = new Ctrl_photo();
        viewPhoto = new Vue_photo(context);
        mdlPhoto = new Mod_photo();


        viewPhoto.setRefCtr(ctrlPhoto);
        ctrlPhoto.setRefVue(viewPhoto);
        ctrlPhoto.setRefMod(mdlPhoto);
        mdlPhoto.setRefVue(viewPhoto);

        ctrlPhoto.refActivity=(MainActivity) context;

        refAct= (Activity)context;

        mdlPhoto.setRefAct(refAct);

    }

    public static ManagerPhoto getInstance( Context context) {
        if (instance == null) {
            instance = new ManagerPhoto(context);
        }
        return instance;
    }

}


