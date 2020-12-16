package com.example.eatfree.comparaison;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.eatfree.PanelManager;
import com.example.eatfree.profile.create_edit.CreateProfileModel;

public class ControllerPanel implements View.OnClickListener {
    VuePanel view;
    ModelePanel model;

    ControllerPanel(VuePanel vue, ModelePanel m) {
        view = vue;
        model = m;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void SetResult(){
        model.SetAllergenesView(view);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == view.btnAccueil.getId()){
            PanelManager.getInstance().SwapPanel(2);
        }
    }
}
