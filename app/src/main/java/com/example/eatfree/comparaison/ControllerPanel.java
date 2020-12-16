package com.example.eatfree.comparaison;

import android.os.Build;
import androidx.annotation.RequiresApi;

import com.example.eatfree.profile.ProfileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class ControllerPanel {
    //Pour pouvoir acceder a l'instance et connaitre les allergenes de l'utilisateur.
    public ProfileManager profileManager;

    //! Map qui sera trié
    private Map<String, ArrayList<String>>tmp = new HashMap<>();


    public ControllerPanel(){
        this.profileManager = ProfileManager.getInstance();
    }

    //methode pour trier la liste des allergenes qui prend en parametre la liste des ingredients du produit scanne.
    public Map triMap(Map<String, ArrayList<String>> list){

        tmp = list;

        //Pour simplifier la procedure car les noms de la liste des allergenes utilisateur ne correspondent pas a ceux des ingredients.
        //On recreer donc une liste pour les allergies utilisateur.
        ArrayList<String> listbis = new ArrayList<>();


        for(int i = 0; i<this.profileManager.GetBoolAllergenes().length; i++){
            if(this.profileManager.GetBoolAllergenes()[i]){
                switch (i){
                    case 0:
                        listbis.add("Arachides");
                        break;
                    case 1:
                        listbis.add("Œuf");
                        break;
                    case 2:
                        listbis.add("Protéine de lait de vache");
                        break;
                    case 3:
                        listbis.add("Gluten");
                        break;
                    case 4:
                        listbis.add("Fruits du groupe latex");
                        break;
                    case 5:
                        listbis.add("Fruits rosacées");
                        break;
                    case 6:
                        listbis.add("Fruits secs oléagineux");
                        break;
                }
            }
        }

        //On parcours ici la liste des allergies ingredients
        for (Map.Entry mapentry : list.entrySet()){
            boolean aSupp = true;

            //On parcours ici la liste allergies utilisateur.
            for(String s: listbis){
                System.out.println("key: "+ mapentry.getKey().toString());
                System.out.println("list: "+ s);

                //si ils concordent on passe la variable a false pour ne pas qu'il se fasse supprimer
                if(mapentry.getKey().toString().equals(s) ){
                    aSupp = false;
                }
            }
            //si la variable est en true on supprime de la liste les allergenes qui ne correspondent pas a l'utilisateur.
            if(aSupp){
                tmp.remove(mapentry.getKey());
            }
        }
        //On renvoie la nouvelle map avec seulement celle qui interesse l'utilisateur.
        return tmp;
    }

    //! getteur qui retourne la map trié
    public Map GetMap(){
        if(tmp != null){
            return tmp;
        }
        return null;
    }

}
