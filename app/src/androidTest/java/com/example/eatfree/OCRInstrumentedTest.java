package com.example.eatfree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.eatfree.photo.JSONWeb;
import com.example.eatfree.photo.TesseractOCR;

import org.hamcrest.CoreMatchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class OCRInstrumentedTest {

    private String OFFURLPrefix = "https://world.openfoodfacts.org/api/v0/product/";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appTargetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Assert.assertEquals("com.example.eatfree", appTargetContext.getPackageName());
    }

    public String ocr_isCorrect(String filestr)  {
        Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
        Context appTargetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Bitmap bmp;

        try {
            InputStream ims = appContext.getAssets().open(filestr);
            bmp = BitmapFactory.decodeStream(ims);
        }
        catch(IOException ex) {
            bmp = null;
        }

        TesseractOCR tocr = new TesseractOCR(appTargetContext, "fra");
        String output = tocr.getOCRResult(bmp);
        Log.i("OCR OUTPUT","Sortie : "+output);

        return output;
    }



    @Test
    public void octT1(){
        String op = ocr_isCorrect("1.jpg");
        Assert.assertThat(op, CoreMatchers.containsString("lait"));
    }

    @Test
    public void octT2(){
        String op = ocr_isCorrect("2.png");
        Assert.assertThat(op, CoreMatchers.containsString("noisette"));
        Assert.assertThat(op, CoreMatchers.containsString("lait"));
        Assert.assertThat(op, CoreMatchers.containsString("soja"));
        Assert.assertThat(op, CoreMatchers.containsString("blé"));
        Assert.assertThat(op, CoreMatchers.containsString("arachides"));
        Assert.assertThat(op, CoreMatchers.containsString("coque"));
    }

    @Test
    public void octT3(){
        String op = ocr_isCorrect("3.jpg");
        Assert.assertThat(op, CoreMatchers.containsString("amidon"));
    }

    @Test
    public void octT4(){
        String op = ocr_isCorrect("4.jpg");
        Assert.assertThat(op, CoreMatchers.containsString("blé"));
        Assert.assertThat(op, CoreMatchers.containsString("oeuf"));
    }

    @Test
    public void octT5(){
        String op = ocr_isCorrect("5.jpg");
        Assert.assertThat(op, CoreMatchers.containsString("sucre"));
    }

    @Test
    public void octT6(){
        String op = ocr_isCorrect("6.jpg");
        Assert.assertThat(op, CoreMatchers.containsString("céleri"));
        Assert.assertThat(op, CoreMatchers.containsString("soja"));
    }

    @Test
    public void octT7(){
        String op = ocr_isCorrect("7.jpg");
        Assert.assertThat(op, CoreMatchers.containsString("blé"));
        Assert.assertThat(op, CoreMatchers.containsString("gluten"));
    }

    @Test
    public void octT8(){
        String op = ocr_isCorrect("8.jpg");
        Assert.assertThat(op, CoreMatchers.containsString("amidon"));
    }


    @Test
    public void offT1(){
        String jsontxt = JSONWeb.getJSON(OFFURLPrefix+ 3445850061370L +".json");
        Log.i("---------------------------",jsontxt);
        String ingredients = "";
        try {
            JSONObject json = new JSONObject(jsontxt);
            ingredients = json.getJSONObject("product").getString("ingredients_text");
        } catch(JSONException e){
            e.printStackTrace();
        }
        Assert.assertThat(ingredients, CoreMatchers.containsString("Sel"));
    }

    @Test
    public void offT2(){
        String jsontxt = JSONWeb.getJSON(OFFURLPrefix+ 3245412366185L +".json");
        String ingredients = "";
        try {
            JSONObject json = new JSONObject(jsontxt);
            ingredients = json.getJSONObject("product").getString("ingredients_text");
        } catch(JSONException e){
            e.printStackTrace();
        }
        Assert.assertThat(ingredients, CoreMatchers.containsString("blé"));
    }

    @Test
    public void offT3(){
        String jsontxt = JSONWeb.getJSON(OFFURLPrefix+ 3564700610510L +".json");
        String ingredients = "";
        try {
            JSONObject json = new JSONObject(jsontxt);
            ingredients = json.getJSONObject("product").getString("ingredients_text");
        } catch(JSONException e){
            e.printStackTrace();
        }
        Assert.assertThat(ingredients, CoreMatchers.containsString("amidon"));
    }

    @Test
    public void offT4(){
        String jsontxt = JSONWeb.getJSON(OFFURLPrefix+ 3564707002158L +".json");
        String ingredients = "";
        try {
            JSONObject json = new JSONObject(jsontxt);
            ingredients = json.getJSONObject("product").getString("ingredients_text");
        } catch(JSONException e){
            e.printStackTrace();
        }
        Assert.assertThat(ingredients, CoreMatchers.containsString("blé"));
    }

    @Test
    public void offT5(){
        String jsontxt = JSONWeb.getJSON(OFFURLPrefix+ 3564700414354L +".json");
        String ingredients = "";
        try {
            JSONObject json = new JSONObject(jsontxt);
            ingredients = json.getJSONObject("product").getString("ingredients_text");
        } catch(JSONException e){
            e.printStackTrace();
        }
        Assert.assertThat(ingredients, CoreMatchers.containsString("céleri"));
    }
}

