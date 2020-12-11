package com.example.eatfree.comparaison;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

import com.example.eatfree.R;

import java.util.Observable;
import java.util.Observer;

public class VuePanel extends LinearLayout implements Observer {



    //! \brief constructeur de la classe VuePanel
    //! il lie le fichier panel_allergene_check.xml, responsable du visuel, aux fichiers qui gèrent l'affichage des données
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public VuePanel(Context context) {
        super(context);

        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        inflater.inflate(R.layout.panel_allergene_check, this);

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}


