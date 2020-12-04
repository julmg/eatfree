package com.example.eatfree.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class PhotoModel {

    private Context mAppContext;


    private Map<String, String[]> mAllergenes = new HashMap<String, String[]>() {{
        put("Arachides", new String[]{"arachide", "cacahuete"});
        put("Œuf", new String[]{"œuf", "oeuf", "ovalbumine","ovomucoide","ovomucoide","lecithine d'oeuf","lecithine d'œuf","lysozyme"});
        put("Protéine de lait de vache", new String[]{"albumine", "babeurre", "beurre","caramel","caseine","caseinate","creme","ferments lactiques",
        "fromage","galactose","globuline","graisse butyrique","lactoferrine","lactalbumine","lactoglobuline","lactose","lactosérum","lait","yaourt","proteines animales"});
        put("Gluten", new String[]{"gluten", "ble", "seigle","orge","avoine","epeautre","kamut","triticale"});
        put("Fruits du groupe latex", new String[]{"latex","banane", "avocat", "chataigne","kiwi","mangue","fraise","soja"});
        put("Fruits rosacées", new String[]{"abricot", "cerise", "fraise","framboise","noisette","peche","poire","pomme","prune"});
        put("Fruits secs oléagineux", new String[]{"oleagineux","aneth", "carotte", "celeri","fenouil","persil"});
    }};


    public PhotoModel(Context context){
        mAppContext = context;
    }


    public Map<String,ArrayList<String>> findAllergenesByOCR(Bitmap bmp){
        TesseractOCR tocr = new TesseractOCR(mAppContext, "fra");
        String output = tocr.getOCRResult(bmp);

        return findAllergenesInText(stripAccents(output.toLowerCase()));
    }


    public Map<String,ArrayList<String>> findAllergenesByBarcodeOFF(Bitmap bmp) {
        String ingredients = BarcodeScan.getIngredientsFromOFF(BarcodeScan.getDoubleBarcode(bmp));
        return findAllergenesInText(stripAccents(ingredients.toLowerCase()));

    }

    //Recherche de mots clés d'allergènes dans le string de sortie de l'OCR
    private Map<String,ArrayList<String>> findAllergenesInText(String text){
        Map<String,ArrayList<String>> foundAllergenes = new HashMap<>();
        for (Map.Entry<String, String[]> entry : mAllergenes.entrySet()) {
            String allergen = entry.getKey();
            String[] words = entry.getValue();
            ArrayList<String> allergeneWords = new ArrayList<>();
            for(String word : words){
                if(text.matches("(?s).*\\b"+word+"\\b.*") || text.matches("(?s).*\\b"+word+"s\\b.*")){
                    if(!(word.equals("pomme") && text.contains("pomme de terre"))){ //cas pomme de terre
                        allergeneWords.add(word);
                    }
                }
                if(word.equals("soja")){
                    if(text.matches("(?s).*\\bsole\\b.*") || text.matches("(?s).*\\bsole\\b.*")){ //cas fréquent soja reconnu en tant que "sole"
                        allergeneWords.add(word);
                    }
                }
            }
            if(!allergeneWords.isEmpty()){
                foundAllergenes.put(allergen,allergeneWords);
            }
        }
        return foundAllergenes;
    }

    private static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }



}
