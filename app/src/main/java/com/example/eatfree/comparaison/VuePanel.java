package com.example.eatfree.comparaison;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.eatfree.R;

import java.util.ArrayList;
import java.util.Map;

public class VuePanel extends LinearLayout {

    //! Affichage des allergènes qui on une correspondance
    public TextView txt_Allergene_correspondant;

    //! Affichage du pouce vert ou rouge
    public ImageView img_pouce;

    //!map des allergènes où il y a correspondance
    private Map<String, ArrayList<String>> mapAllergene_correspondant;

    //! référence au controller
    private ModelePanel modele;

    private ControllerPanel controller;

    //! bouton pour aller à la page d'accueil (prendre une photo)
    public Button btnAccueil;

    //! \brief constructeur de la classe VuePanel
    //! il lie le fichier panel_allergene_check.xml, responsable du visuel, aux fichiers qui gèrent l'affichage des données
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public VuePanel(Context context, ModelePanel newModele) {
        super(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        inflater.inflate(R.layout.panel_allergene_check, this);

        txt_Allergene_correspondant = (TextView)findViewById(R.id.txtAllergene);
        img_pouce = (ImageView)findViewById(R.id.img_pouce);
        btnAccueil = (Button)findViewById(R.id.btnAccueil_panelAllergene);

        modele = newModele;
        controller = new ControllerPanel(this, modele);

        btnAccueil.setOnClickListener(controller);
    }

    public Map<String, ArrayList<String>> GetMapAllergene_correspondant(){
        return mapAllergene_correspondant;
    }

    public void SetMapAllergene_correspondant(Map<String, ArrayList<String>> map) {
        mapAllergene_correspondant = map;
    }

    public ControllerPanel GetControllerPanel() {
        return controller;
    }
}


