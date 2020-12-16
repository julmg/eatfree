package com.example.eatfree;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.eatfree.PriseDePhoto.ManagerPhoto;
import com.example.eatfree.profile.ProfileManager;
import com.example.eatfree.result.ResultController;
import com.example.eatfree.result.ResultModel;
import com.example.eatfree.result.ResultView;
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

    private ResultView mResultView;
    private ResultController mResultController;
    private ResultModel mResultModel;

    public static PanelManager getInstance()
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

        mResultView = new ResultView(context);
        mResultController = new ResultController();
        mResultModel = new ResultModel();
        mResultView.setRefController(mResultController);
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
                mResultView.setActivated(false);
                break;
            case 2:
                activity.setContentView(ManagerPhoto.getInstance(null).viewPhoto);
                ProfileManager.getInstance().GetProfileView().setActivated(false);
                ManagerPhoto.getInstance(null).viewPhoto.setActivated(true);
                settingsView.setActivated(false);
                mResultView.setActivated(false);
                break;
            case 3:
                activity.setContentView(settingsView);
                ProfileManager.getInstance().GetProfileView().setActivated(false);
                ManagerPhoto.getInstance(null).viewPhoto.setActivated(false);
                settingsView.setActivated(true);
                mResultView.setActivated(false);
                break;
            case 4:
                activity.setContentView(mResultView);
                ProfileManager.getInstance().GetProfileView().setActivated(false);
                ManagerPhoto.getInstance(null).viewPhoto.setActivated(false);
                settingsView.setActivated(false);
                mResultView.setActivated(true);
                break;
        }
    }
}
