package com.example.eatfree.result;

import android.view.View;
import com.example.eatfree.R;
import com.example.eatfree.PanelManager;
import com.example.eatfree.ResultManager;

public class ResultController implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAccueil_panelAllergene) {
            PanelManager.getInstance().SwapPanel(2);
        }
    }
}