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
 * @file OCRInstrumentedTest.java
 * @brief Tests unitaires instrumentés du fonctionnement de Tesseract sur la reconnaissance optique de caractères
 * @date 2020
 */
@RunWith(AndroidJUnit4.class)
public class OCRInstrumentedTest {

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



}

