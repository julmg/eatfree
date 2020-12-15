package com.example.eatfree.photoUtils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @file BarcodeScan.java
 * @brief Classe implémentant le scan de code-barres et la récupération des données de l'API OpenFoodFacts
 * @date 2020
 * @author Julian Lecocq--Mage
 */
public class BarcodeScan {

    private final static String OFFURLPrefix = "https://world.openfoodfacts.org/api/v0/product/"; //!< Préfixe API OpenFoodFacts

    /**
     * @brief Récupération du code-barres à 13 chiffres à partir d'un bitmap (image), utilisant la bibliothèque ZXing
     * @param bmp le bitmap duquel on veut extraire le code-barres
     * @return Le code-barres de l'image, ou 0 si code-barres illisible/inexistant
     */
    public static long getBarcode(Bitmap bmp) {
        String contents;
        Bitmap bmp2 = PhotoUtils.fastBlur(bmp); //On crée un peu de flou sur le bitmap afin de réduire le bruit de l'image et d'améliorer la détection du code barres

        int[] intArray = new int[bmp2.getWidth()*bmp2.getHeight()];
        bmp.getPixels(intArray, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bmp.getWidth(), bmp.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();
        Map<DecodeHintType, Object> hints = new HashMap<>();
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Arrays.asList(BarcodeFormat.EAN_8,BarcodeFormat.EAN_13));
        Result result;
        try {
            result = reader.decode(bitmap, hints);
        } catch (Exception e) {
            return 0; //retourne 0 si impossible de reconnaitre code barre
        }
        contents = result.getText();
        String contentLong13 = contents.substring(0, Math.min(contents.length(), 13));
        return Long.parseLong(contentLong13);
    }

    /**
     * @brief Récupération des ingrédients d'un produit alimentaire à partir de son code-barres et de la base de données OpenFoodFacts
     * @param barcode le code-barres du produit alimentaire
     * @return La liste des ingrédients du produit sous forme de String, ou un String vide si impossible
     * @warning La méthode agrège plusieurs noeuds du JSON de l'API, donc il peut y avoir des doublons d'ingrédients dans le retour
     */
    public static String getIngredientsFromOFF(long barcode){
        if(barcode==0){
            return "";
        }
        String jsonTxt = JSONWeb.getJSON(OFFURLPrefix+ barcode +".json");
        String ingredients = "";
        JSONObject json = null;
        try {
            json = new JSONObject(jsonTxt);
        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            ingredients += json.getJSONObject("product").getString("ingredients_text");
        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            ingredients += json.getJSONObject("product").getString("ingredients_text_fr");
        } catch(Exception e){
            e.printStackTrace();
        }
        try {
            ingredients += json.getJSONObject("product").getString("ingredients_text_with_allergens_fr");
        } catch(Exception e){
            e.printStackTrace();
        }

        return ingredients;
    }
}
