package com.example.eatfree.comparaison;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.eatfree.R;

import java.util.Observable;
import java.util.Observer;

public class VuePanel extends LinearLayout implements Observer {

    //! Affichage des allergènes qui on une correspondance
    public TextView txt_Allergene_correspondant;

    //! Affichage du pouce vert ou rouge
    public ImageView img_pouce;

    //! \brief constructeur de la classe VuePanel
    //! il lie le fichier panel_allergene_check.xml, responsable du visuel, aux fichiers qui gèrent l'affichage des données
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public VuePanel(Context context) {
        super(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        inflater.inflate(R.layout.panel_allergene_check, this);

        txt_Allergene_correspondant = (TextView)findViewById(R.id.txtAllergene);
        img_pouce = (ImageView)findViewById(R.id.img_pouce);

        /*
        * set le txt_Allergene_correspondant avec ce que retourne le controlleur de batiste
        * */

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}


