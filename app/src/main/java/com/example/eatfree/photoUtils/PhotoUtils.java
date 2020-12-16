package com.example.eatfree.photoUtils;

import android.content.Context;
import android.graphics.Bitmap;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @file PhotoModel.java
 * @brief Modèle du panel Photo
 * @date 2020
 * @author Julian Lecocq--Mage
 */
public class PhotoUtils {

    //! Map des allergènes pris en charge par l'application (1er élément : groupe d'allergènes / 2e élément : mots apparaissant dans la liste d'ingrédients, se référant à ce groupe d'allergènes)
    private static Map<String, String[]> mAllergenes = new HashMap<String, String[]>() {{
        put("Arachides", new String[]{"arachide", "cacahuete"});
        put("Œuf", new String[]{"œuf", "oeuf", "ovalbumine","ovomucoide","ovomucoide","lecithine d'oeuf","lecithine d'œuf","lysozyme"});
        put("Protéine de lait de vache", new String[]{"albumine", "babeurre", "beurre","caramel","caseine","caseinate","creme","ferments lactiques",
        "fromage","galactose","globuline","graisse butyrique","lactoferrine","lactalbumine","lactoglobuline","lactose","lactosérum","lait","yaourt","proteines animales"});
        put("Gluten", new String[]{"gluten", "ble", "seigle","orge","avoine","epeautre","kamut","triticale"});
        put("Fruits du groupe latex", new String[]{"latex","banane", "avocat", "chataigne","kiwi","mangue","fraise","soja"});
        put("Fruits rosacées", new String[]{"abricot", "cerise", "fraise","framboise","noisette","peche","poire","pomme","prune"});
        put("Fruits secs oléagineux", new String[]{"oleagineux","aneth", "carotte", "celeri","fenouil","persil"});
    }};



    /**
     * @brief Récupération des allergènes présents dans un produit alimentaire à partir du bitmap (image) de la liste d'ingrédients : OCRisation
     * @param bmp L'image de la liste d'ingrédients
     * @return La map des groupes d'allergènes et termes qui y font référence
     */
    public static Map<String,ArrayList<String>> findAllergenesWithOCR(Bitmap bmp, Context appContext){
        TesseractOCR tocr = new TesseractOCR(appContext, "fra");
        String output = tocr.getOCRResult(bmp);

        return findAllergenesInText(stripAccents(output.toLowerCase()));
    }

    /**
     * @brief Récupération des allergènes présents dans un produit alimentaire  à partir du bitmap (image) du code-barres du produit : scan + bdd OpenFoodFacts
     * @param bmp L'image du code-barres
     * @return La map des groupes d'allergènes et termes qui y font référence
     */
    public static Map<String,ArrayList<String>> findAllergenesWithBarcodeOFF(Bitmap bmp) throws Exception {
        Long barcode = BarcodeScan.getBarcode(bmp);
        String ingredients = BarcodeScan.getIngredientsFromOFF(barcode);
        if(ingredients==""){
            if(barcode!=0){
                throw new Exception("Une erreur s'est produite avec le scan de code-barres "+barcode+" (problème de connexion ?)");
            } else {
                throw new Exception("Code barre non reconnu");
            }

        }
        return findAllergenesInText(stripAccents(ingredients.toLowerCase()));

    }

    /**
     * @brief Recherche de mots-clés d'allergènes dans un String
     * @param text Le String dans lequel effectuer la recherche
     * @return La map des groupes d'allergènes et termes qui y font référence
     */

    private static Map<String,ArrayList<String>> findAllergenesInText(String text){
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

    /**
     * @brief Suppression des accents dans un String
     * @param s le String duquel on veut retirer tous les accents
     * @return Ce même String, sans les accents
     */
    private static String stripAccents(String s)
    {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    /**
     * @brief Création rapide de flou sur un bitmap, via un redimensionnement *0.5 puis *2 de l'image
     * @param bmp L'image que l'on veut traiter
     * @return l'image traitée
     */
    public static Bitmap fastBlur(Bitmap bmp){
        int height = bmp.getHeight();
        int width = bmp.getWidth();
        Bitmap smallbmp = Bitmap.createScaledBitmap(bmp, width/2, height/2, true);
        return Bitmap.createScaledBitmap(smallbmp, width, height, true);
    }

    public static Bitmap scale(Bitmap bmp, int divscale){
        return Bitmap.createScaledBitmap(bmp, bmp.getWidth()/divscale, bmp.getHeight()/divscale, false);
    }

}
