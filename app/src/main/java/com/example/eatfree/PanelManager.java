package com.example.eatfree;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.eatfree.PriseDePhoto.ManagerPhoto;
import com.example.eatfree.profile.ProfileManager;
import com.example.eatfree.settings.SettingsView;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class CreateProfileController
//! \author Maxime Poirier
//! Controller de la page de settings
//! Gère le clic sur les boutons de la vue
////////////////////////////////////////////////////////////////////////////////////////////////
public class PanelManager {
    private static PanelManager instance = null;

    SettingsView settingsView;

    MainActivity activity;

    public static  PanelManager getInstance()
    {
         return instance;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    PanelManager(MainActivity act, Context context)
    {
        activity = act;
        instance = this;
        settingsView = new SettingsView(context);
        settingsView.setActivated(false);
    }

    //! \brief Echange les panels en fonction du bouton sur lequel on clique
    public void SwapPanel(int panel)
    {
        switch (panel)
        {
            case 1:
                activity.setContentView(ProfileManager.getInstance().GetProfileView());
                ProfileManager.getInstance().GetProfileView().setActivated(true);
                ManagerPhoto.getInstance(null).viewPhoto.setActivated(false);
                settingsView.setActivated(false);
                break;
            case 2:
                activity.setContentView(ManagerPhoto.getInstance(null).viewPhoto);
                ProfileManager.getInstance().GetProfileView().setActivated(false);
                ManagerPhoto.getInstance(null).viewPhoto.setActivated(true);
                settingsView.setActivated(false);
                break;
            case 3:
                activity.setContentView(settingsView);
                ProfileManager.getInstance().GetProfileView().setActivated(false);
                ManagerPhoto.getInstance(null).viewPhoto.setActivated(false);
                settingsView.setActivated(true);
                break;
        }
    }
}
