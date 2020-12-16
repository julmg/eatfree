package com.example.eatfree.result;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.eatfree.R;
import com.example.eatfree.ResultManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.xml.transform.Result;

public class ResultView extends LinearLayout implements Observer {

    //! Affichage des allergènes qui on une correspondance
    public TextView txt_Allergene_correspondant;

    //! Affichage du pouce vert ou rouge
    public ImageView img_pouce;

    //!map des allergènes où il y a correspondance
    private Map<String, ArrayList<String>> mapAllergene_correspondant;

    //! référence au controller
    private ResultController refController;

    private LinearLayout table;

    //! bouton pour aller à la page d'accueil (prendre une photo)
    public Button btnAccueil;
    
    private Context mContext;

    //! \brief constructeur de la classe VuePanel
    //! il lie le fichier panel_allergene_check.xml, responsable du visuel, aux fichiers qui gèrent l'affichage des données
    public ResultView(Context context) {
        super(context);

        mContext = context;
        
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        inflater.inflate(R.layout.panel_allergene_check, this);

        table = (LinearLayout) findViewById(R.id.tableAllergenes);
        img_pouce = (ImageView) findViewById(R.id.img_pouce);
        btnAccueil = (Button) findViewById(R.id.btnAccueil_panelAllergene);

        ResultManager.getInstance().addObserver(this);
    }

    public void setRefController(ResultController resultController){
        refController = resultController;
        btnAccueil.setOnClickListener(refController);
    }

    public void setAllergeneMap(Map<String, ArrayList<String>> resultMap){
        table.removeAllViews();
        if(resultMap.isEmpty()){
            img_pouce.setImageResource(R.drawable.pouce_vert);
            addMessageRow("C'est bon ! A priori, il n'y a pas d'allergènes dans ce produit.");
        } else {
            img_pouce.setImageResource(R.drawable.pouce_rouge);
            addMessageRow("Attention, des allergènes ont été trouvés :");
            for (Map.Entry<String, ArrayList<String>> entry : resultMap.entrySet()) {
                addAllergeneRow(entry.getKey(),entry.getValue());
            }
        }
    }

    public void addAllergeneRow(String allergene, ArrayList<String> termes){
        LayoutParams params = new LinearLayout.LayoutParams(
                0, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;

        String stringtermes = android.text.TextUtils.join(", ", termes);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        TextView allergeneTV = new TextView(mContext);
        allergeneTV.setGravity(Gravity.CENTER);
        allergeneTV.setText(allergene);
        allergeneTV.setLayoutParams(params);
        allergeneTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        allergeneTV.setSingleLine(false);
        linearLayout.addView(allergeneTV);

        TextView termesTV = new TextView(mContext);
        termesTV.setGravity(Gravity.CENTER);
        termesTV.setText(stringtermes);
        termesTV.setLayoutParams(params);
        allergeneTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        allergeneTV.setSingleLine(false);
        linearLayout.addView(termesTV);

        table.addView(linearLayout);
    }

    @SuppressLint("SetTextI18n")
    public void addMessageRow(String message){
        LayoutParams params = new LinearLayout.LayoutParams(
                0, LayoutParams.MATCH_PARENT);
        params.weight = 1.0f;

        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);

        TextView messageTV = new TextView(mContext);
        messageTV.setGravity(Gravity.CENTER);
        messageTV.setText(message);
        messageTV.setLayoutParams(params);
        messageTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);

        linearLayout.addView(messageTV);

        table.addView(linearLayout);
    }

    @Override
    public void update(Observable o, Object arg) {
        setAllergeneMap(ResultManager.getInstance().getResult());
    }
}