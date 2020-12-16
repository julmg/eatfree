package com.example.eatfree.comparaison;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.eatfree.R;
import com.example.eatfree.photoUtils.PhotoUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class VuePanel extends LinearLayout {

    //! Affichage des allergènes qui on une correspondance
    public TextView txt_Allergene_correspondant;

    //! Affichage du pouce vert ou rouge
    public ImageView img_pouce;

    //!map des allergènes où il y a correspondance
    private static Map<String, ArrayList<String>> mapAllergene_correspondant;

    //! référence au controller (A modifier en fonction de ce que fait Batiste)
    private ControllerPanel controller;

    //! bouton pour aller à la page d'accueil (prendre une photo)
    public Button btnAccueil;

    //! \brief constructeur de la classe VuePanel
    //! il lie le fichier panel_allergene_check.xml, responsable du visuel, aux fichiers qui gèrent l'affichage des données
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public VuePanel(Context context, ControllerPanel newController) {
        super(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        inflater.inflate(R.layout.panel_allergene_check, this);

        txt_Allergene_correspondant = (TextView)findViewById(R.id.txtAllergene);
        img_pouce = (ImageView)findViewById(R.id.img_pouce);
        btnAccueil = (Button)findViewById(R.id.btnAccueil_panelAllergene);

        controller = newController;

        //récupère la map trié
        mapAllergene_correspondant = controller.GetMap();
        // créer une list de string pour y mettre les valeurs de la map (Allergène)
        ArrayList<String> result = new ArrayList(mapAllergene_correspondant.values());
        String listAllergène_afficher = "";
        // parcourt de cette list
        for(String s: result){
            listAllergène_afficher = listAllergène_afficher + " - " + s;
        }
        txt_Allergene_correspondant.setText(listAllergène_afficher);

        // change l'image du pouce en fonction de la map, si elle est null ou non
        if(mapAllergene_correspondant == null){
            img_pouce.setImageResource(R.drawable.pouce_vert);
        }
        else{
            img_pouce.setImageResource(R.drawable.pouce_rouge);
        }



    }
}


