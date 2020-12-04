package com.example.eatfree.photo;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import org.json.JSONException;
import org.json.JSONObject;

public class BarcodeScan {

    private static String OFFURLPrefix = "https://world.openfoodfacts.org/api/v0/product/";

    public static long getDoubleBarcode(Bitmap bmp) {
        String contents;
        int[] intArray = new int[bmp.getWidth()*bmp.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array

        bmp.getPixels(intArray, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bmp.getWidth(), bmp.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();
        Result result;
        try {
            result = reader.decode(bitmap);
        } catch (Exception e) {
            return 0; //retourne 0 si impossible de reconnaitre code barre
        }
        contents = result.getText();
        String contentLong13 = contents.substring(0, Math.min(contents.length(), 13));
        return Long.parseLong(contentLong13);
    }

    public static String getIngredientsFromOFF(long barcode){
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
