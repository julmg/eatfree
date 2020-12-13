package com.example.eatfree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.eatfree.photoUtils.PhotoUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @file PhotoModelInstrumentedTest.java
 * @brief Tests unitaires instrumentés du modèle du panneau Photo : tests des findAllergenesWith...
 * @date 2020
 */
@RunWith(AndroidJUnit4.class)
public class PhotoUtilsInstrumentedTest {

    Context appTargetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();


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
    public void allergenesOFF1() throws Exception {
        Map<String, ArrayList<String>> expectedAllergenes = new HashMap<String, ArrayList<String>>() {{
            put("Gluten", new ArrayList<>(Collections.singletonList("ble")));
            put("Fruits du groupe latex", new ArrayList<>(Collections.singletonList("soja")));
            put("Protéine de lait de vache", new ArrayList<>(Collections.singletonList("beurre"))); }};

        Map<String, ArrayList<String>> allergenes = PhotoUtils.findAllergenesWithBarcodeOFF(getBMP("B2.jpg"));
        Assert.assertEquals(expectedAllergenes.entrySet(),allergenes.entrySet());
    }

    @Test
    public void allergenesOFF2() throws Exception {
        Map<String, ArrayList<String>> expectedAllergenes = new HashMap<String, ArrayList<String>>() {{
            put("Gluten", new ArrayList<>(Arrays.asList("gluten","ble")));
            put("Fruits secs oléagineux", new ArrayList<>(Collections.singletonList("carotte")));
            put("Œuf", new ArrayList<>(Collections.singletonList("œuf"))); }};

        Map<String, ArrayList<String>> allergenes = PhotoUtils.findAllergenesWithBarcodeOFF(getBMP("B3.jpg"));
        Assert.assertEquals(expectedAllergenes.entrySet(),allergenes.entrySet());
    }

    @Test
    public void allergenesOFF3() throws Exception {
        Map<String, ArrayList<String>> expectedAllergenes = new HashMap<String, ArrayList<String>>() {};

        Map<String, ArrayList<String>> allergenes = PhotoUtils.findAllergenesWithBarcodeOFF(getBMP("B4.jpg"));
        Assert.assertEquals(expectedAllergenes.entrySet(),allergenes.entrySet());
    }

    @Test
    public void allergenesOFF4() throws Exception {
        Map<String, ArrayList<String>> expectedAllergenes = new HashMap<String, ArrayList<String>>() {};

        Map<String, ArrayList<String>> allergenes = PhotoUtils.findAllergenesWithBarcodeOFF(getBMP("B5.jpg"));
        Assert.assertEquals(expectedAllergenes.entrySet(),allergenes.entrySet());
    }

    @Test
    public void allergenesOCR1() {
        Map<String, ArrayList<String>> expectedAllergenes = new HashMap<String, ArrayList<String>>() {{
            put("Gluten", new ArrayList<>(Collections.singletonList("ble")));
            put("Fruits du groupe latex", new ArrayList<>(Collections.singletonList("soja")));
            put("Protéine de lait de vache", new ArrayList<>(Collections.singletonList("lait"))); }};

        Map<String, ArrayList<String>> allergenes = PhotoUtils.findAllergenesWithOCR(getBMP("1.jpg"),appTargetContext);
        Assert.assertEquals(expectedAllergenes.entrySet(),allergenes.entrySet());
    }

    @Test
    public void allergenesOCR2() {
        Map<String, ArrayList<String>> expectedAllergenes = new HashMap<String, ArrayList<String>>() {{
            put("Arachides", new ArrayList<>(Collections.singletonList("arachide")));
            put("Gluten", new ArrayList<>(Collections.singletonList("ble")));
            put("Fruits du groupe latex", new ArrayList<>(Collections.singletonList("soja")));
            put("Fruits rosacées", new ArrayList<>(Collections.singletonList("noisette")));
            put("Protéine de lait de vache", new ArrayList<>(Arrays.asList("caramel","lait"))); }};


        Map<String, ArrayList<String>> allergenes = PhotoUtils.findAllergenesWithOCR(getBMP("2.png"),appTargetContext);
        Assert.assertEquals(expectedAllergenes.entrySet(),allergenes.entrySet());
    }

    @Test
    public void allergenesOCR3() {
        Map<String, ArrayList<String>> expectedAllergenes = new HashMap<String, ArrayList<String>>() {};

        Map<String, ArrayList<String>> allergenes = PhotoUtils.findAllergenesWithOCR(getBMP("3.jpg"),appTargetContext);
        Assert.assertEquals(expectedAllergenes.entrySet(),allergenes.entrySet());
    }


    @Test
    public void allergenesOCR5() {
        Map<String, ArrayList<String>> expectedAllergenes = new HashMap<String, ArrayList<String>>() {{
            put("Fruits rosacées", new ArrayList<>(Collections.singletonList("fraise"))); }};


        Map<String, ArrayList<String>> allergenes = PhotoUtils.findAllergenesWithOCR(getBMP("5.jpg"),appTargetContext);
        Assert.assertEquals(expectedAllergenes.entrySet(),allergenes.entrySet());
    }

    @Test
    public void allergenesOCR6() {
        Map<String, ArrayList<String>> expectedAllergenes = new HashMap<String, ArrayList<String>>() {{
            put("Fruits du groupe latex", new ArrayList<>(Collections.singletonList("soja")));
            put("Gluten", new ArrayList<>(Collections.singletonList("gluten")));
            put("Œuf", new ArrayList<>(Collections.singletonList("œuf")));
            put("Fruits secs oléagineux", new ArrayList<>(Arrays.asList("carotte","celeri", "persil"))); }};

        Map<String, ArrayList<String>> allergenes = PhotoUtils.findAllergenesWithOCR(getBMP("6.jpg"),appTargetContext);
        Assert.assertEquals(expectedAllergenes.entrySet(),allergenes.entrySet());
    }

    @Test
    public void allergenesOCR7() {
        Map<String, ArrayList<String>> expectedAllergenes = new HashMap<String, ArrayList<String>>() {{
            put("Gluten", new ArrayList<>(Arrays.asList("gluten", "ble")));
            put("Œuf", new ArrayList<>(Collections.singletonList("œuf")));
            put("Protéine de lait de vache", new ArrayList<>(Collections.singletonList("lait"))); }};

        Map<String, ArrayList<String>> allergenes = PhotoUtils.findAllergenesWithOCR(getBMP("7.jpg"),appTargetContext);
        Assert.assertEquals(expectedAllergenes.entrySet(),allergenes.entrySet());
    }



}
