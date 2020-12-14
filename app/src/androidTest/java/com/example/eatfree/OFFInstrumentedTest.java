package com.example.eatfree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.eatfree.photoUtils.BarcodeScan;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
/**
 * @file OFFInstrumentedTest.java
 * @brief Tests unitaires instrumentés du fonctionnement de l'API OpenFoodFacts pour la récupération des allergènes
 * @date 2020
 * @author Julian Lecocq--Mage
 */
@RunWith(AndroidJUnit4.class)
public class OFFInstrumentedTest {

    private String OFFURLPrefix = "https://world.openfoodfacts.org/api/v0/product/";

    private Bitmap getBMP(String filestr){
        Context appContext = InstrumentationRegistry.getInstrumentation().getContext();
        Bitmap bmp;

        try {
            InputStream ims = appContext.getAssets().open(filestr);
            bmp = BitmapFactory.decodeStream(ims);
        }
        catch(IOException ex) {
            bmp = null;
        }
        return bmp;
    }

    @Test
    public void offT1(){
        String ingredients = BarcodeScan.getIngredientsFromOFF(3445850061370L);
        Assert.assertThat(ingredients, CoreMatchers.containsString("Sel"));
    }

    @Test
    public void offT2(){
        String ingredients = BarcodeScan.getIngredientsFromOFF(3245412366185L);
        Assert.assertThat(ingredients, CoreMatchers.containsString("blé"));
    }

    @Test
    public void offT3(){
        String ingredients = BarcodeScan.getIngredientsFromOFF(3564700610510L);
        Assert.assertThat(ingredients, CoreMatchers.containsString("amidon"));
    }

    @Test
    public void offT4(){
        String ingredients = BarcodeScan.getIngredientsFromOFF(3564707002158L);
        Assert.assertThat(ingredients, CoreMatchers.containsString("blé"));
    }

    @Test
    public void offT5(){
        String ingredients = BarcodeScan.getIngredientsFromOFF(3564700414354L);
        Assert.assertThat(ingredients, CoreMatchers.containsString("céleri"));
    }

    @Test
    public void barcodeScanT0(){
        long barcode = BarcodeScan.getBarcode(getBMP("B1.jpg"));
        Assert.assertEquals(3445850061370L,barcode);
    }

    @Test
    public void barcodeScanT1(){
        long barcode = BarcodeScan.getBarcode(getBMP("B2.jpg"));
        Assert.assertEquals(7622300689124L,barcode);
    }

    @Test
    public void barcodeScanT2(){
        long barcode = BarcodeScan.getBarcode(getBMP("B3.jpg"));
        Assert.assertEquals(3245412366185L,barcode);
    }

    @Test
    public void barcodeScanT3(){
        long barcode = BarcodeScan.getBarcode(getBMP("B4.jpg"));
        Assert.assertEquals(3564700610510L,barcode);
    }

    @Test
    public void barcodeScanT4(){
        long barcode = BarcodeScan.getBarcode(getBMP("B5.jpg"));
        Assert.assertEquals(3250390259701L,barcode);
    }
}
