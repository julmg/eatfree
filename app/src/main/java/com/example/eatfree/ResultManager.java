package com.example.eatfree;

import com.example.eatfree.profile.ProfileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class ResultManager extends Observable {
    private static ResultManager _instance = null;

    private Map<String, ArrayList<String>> mResultMap;

    public static ResultManager getInstance() {
        if(_instance==null){
            _instance = new ResultManager();
        }
        return _instance;
    }

    private ResultManager(){
        mResultMap = new HashMap<>();
    }

    public void setResult(Map<String, ArrayList<String>> result){
        mResultMap=result;
        setChanged();
        notifyObservers();
    }

    public Map<String, ArrayList<String>> getResult(){
        return mResultMap;
    }

    public void triAndSetResult(Map<String, ArrayList<String>> result){
        setResult(triResult(result));
    }

    //methode pour trier la liste des allergenes qui prend en parametre la liste des ingredients du produit scanne.
    public Map<String, ArrayList<String>> triResult(Map<String, ArrayList<String>> nonTrieResult){

        Map<String, ArrayList<String>> trieResult = new HashMap<>();

        boolean[] booleanAllergenes = ProfileManager.getInstance().GetBoolAllergenes();

        for (Map.Entry<String, ArrayList<String>> entry : nonTrieResult.entrySet()) {
            switch (entry.getKey()) {
                case "Arachides":
                    if (booleanAllergenes[0]) {
                        trieResult.put(entry.getKey(), entry.getValue());
                    }
                    break;
                case "Œuf":
                    if (booleanAllergenes[1]) {
                        trieResult.put(entry.getKey(), entry.getValue());
                    }
                    break;
                case "Protéine de lait de vache":
                    if (booleanAllergenes[2]) {
                        trieResult.put(entry.getKey(), entry.getValue());
                    }
                    break;
                case "Gluten":
                    if (booleanAllergenes[3]) {
                        trieResult.put(entry.getKey(), entry.getValue());
                    }
                    break;
                case "Fruits du groupe latex":
                    if (booleanAllergenes[4]) {
                        trieResult.put(entry.getKey(), entry.getValue());
                    }
                    break;
                case "Fruits rosacées":
                    if (booleanAllergenes[5]) {
                        trieResult.put(entry.getKey(), entry.getValue());
                    }
                    break;
                case "Fruits secs oléagineux":
                    if (booleanAllergenes[6]) {
                        trieResult.put(entry.getKey(), entry.getValue());
                    }
                    break;
            }
        }
        return trieResult;
    }
}
