package com.example.eatfree.settings;

import com.example.eatfree.PanelManager;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class SettingsModel
//! \author Solène Tessiore
//! Model de la page de settings
//! Gère le changement de panel
////////////////////////////////////////////////////////////////////////////////////////////////
public class SettingsModel {
    //! \brief Renvoie vers la méthode SwapPanel du PanelManager
    public void ChangePanel(int panel) {
        PanelManager.getInstance().SwapPanel(panel);
    }
}
