package com.example.eatfree.profile.create_edit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.eatfree.R;
import com.example.eatfree.profile.ProfileManager;

import java.util.Observable;
import java.util.Observer;

////////////////////////////////////////////////////////////////////////////////////////////////
//! \class CreateProfileView
//! \author Solène Tessiore
//! gère la vue de la création de profil
////////////////////////////////////////////////////////////////////////////////////////////////
public class CreateProfileView extends LinearLayout{
    //! champ de texte dans lequel l'utilisateur entre son nom
    public EditText username;

    //! champ de texte dans lequel l'utilisateur entre sa date de naissance
    public EditText dateOfBirth;

    //! case à cocher si l'utilisateur est allergique aux arachides
    public CheckBox arachides;

    //! case à cocher si l'utilisateur est allergique aux oeuf
    public CheckBox oeuf;

    //! case à cocher si l'utilisateur est allergique aux lait
    public CheckBox lait;

    //! case à cocher si l'utilisateur est allergique aux gluten
    public CheckBox gluten;

    //! case à cocher si l'utilisateur est allergique aux fruitsLatex
    public CheckBox fruitsLatex;

    //! case à cocher si l'utilisateur est allergique aux fruitsRosacees
    public CheckBox fruitsRosacees;

    //! case à cocher si l'utilisateur est allergique aux fruitsOleagineux
    public CheckBox fruitsOleagineux;

    //! bouton de validation
    public Button btnValidate;

    //! contient l'instance du CreateProfileModel
    CreateProfileModel model;

    //! référence vers le CreateProfileController
    CreateProfileController controller;

    //! \brief constructeur de la classe CreateProfileView
    //! il lie le fichier create_profile.xml, responsable du visuel, aux fichiers qui gèrent les données saisies
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public CreateProfileView(Context context) {
        super(context);
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        inflater.inflate(R.layout.create_profile, this);

        username = (EditText)findViewById(R.id.username);
        dateOfBirth = (EditText)findViewById(R.id.dateOfBirth);
        arachides = (CheckBox)findViewById(R.id.arachidesCheck);
        oeuf = (CheckBox)findViewById(R.id.oeufsCheck);
        lait = (CheckBox)findViewById(R.id.laitVacheCheck);
        gluten = (CheckBox)findViewById(R.id.glutenCheck);
        fruitsLatex = (CheckBox)findViewById(R.id.fruitsLatexCheck);
        fruitsRosacees = (CheckBox)findViewById(R.id.fruitsRosaceesCheck);
        fruitsOleagineux = (CheckBox)findViewById(R.id.fruitsOleagineuxCheck);
        btnValidate = (Button)findViewById(R.id.valider);

        ProfileManager profileManager = ProfileManager.getInstance();

        if (profileManager.GetName() != null){
            username.setText(profileManager.GetName());
        }
        if (profileManager.GetBirth() != null){
            dateOfBirth.setText(profileManager.GetBirth());
        }
        if (profileManager.GetAllergenes()[0] != null){
            arachides.setChecked(true);
        }
        if (profileManager.GetAllergenes()[1] != null){
            oeuf.setChecked(true);
        }
        if (profileManager.GetAllergenes()[2] != null){
            lait.setChecked(true);
        }
        if (profileManager.GetAllergenes()[3] != null){
            gluten.setChecked(true);
        }
        if (profileManager.GetAllergenes()[4] != null){
            fruitsLatex.setChecked(true);
        }
        if (profileManager.GetAllergenes()[5] != null){
            fruitsRosacees.setChecked(true);
        }
        if (profileManager.GetAllergenes()[6] != null){
            fruitsOleagineux.setChecked(true);
        }

        model = CreateProfileModel.getInstance();
        controller = new CreateProfileController(this);

        btnValidate.setOnClickListener(controller);
    }
}
