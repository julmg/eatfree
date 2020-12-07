package com.example.eatfree.photo;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @file TesseractOCR.java
 * @brief Classe implémentant l'OCRisation (Reconnaissance optique de caractères) via la bibliothèque Tess-Two (fork de Tesseract)
 * @date 2020
 */
public class TesseractOCR {

    private final TessBaseAPI mTess;

    /**
     * @brief Constructeur de l'OCRiseur
     * @param context le contexte de l'application
     * @param language la langue du texte sous forme de 3 caractères ("fra","eng", ...)
     * @warning Seul le français est importé dans le projet, donc seul "fra" fonctionnera comme paramètre language
     */
    public TesseractOCR(Context context, String language) {
        mTess = new TessBaseAPI();
        boolean fileExistFlag = false;

        AssetManager assetManager = context.getAssets();

        String dstPathDir = "/tesseract/tessdata/";

        String srcFile = "fra.traineddata"; //Seul fichier disponible pour le moment
        InputStream inFile = null;

        dstPathDir = context.getFilesDir() + dstPathDir;
        String dstInitPathDir = context.getFilesDir() + "/tesseract";
        String dstPathFile = dstPathDir + srcFile;
        FileOutputStream outFile = null;

        try {
            inFile = assetManager.open(srcFile);

            File f = new File(dstPathDir);

            if (!f.exists()) {
                if (!f.mkdirs()) {
                    Toast.makeText(context, srcFile + " can't be created.", Toast.LENGTH_SHORT).show();
                }
                outFile = new FileOutputStream(new File(dstPathFile));
            } else {
                fileExistFlag = true;
            }

        } catch (Exception ex) {
            Log.e("OCR", ex.getMessage());

        } finally {

            if (fileExistFlag) {
                try {
                    if (inFile != null) inFile.close();
                    mTess.init(dstInitPathDir, language);
                    return;

                } catch (Exception ex) {
                    Log.e("OCR", ex.getMessage());
                }
            }

            if (inFile != null && outFile != null) {
                try {
                    //copy file
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inFile.read(buf)) != -1) {
                        outFile.write(buf, 0, len);
                    }
                    inFile.close();
                    outFile.close();
                    mTess.init(dstInitPathDir, language);
                } catch (Exception ex) {
                    Log.e("OCR", ex.getMessage());
                }
            } else {
                Toast.makeText(context, srcFile + " can't be read.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Réalisation de l'OCRisation
     * @param bitmap le bitmap (image) à OCRiser
     * @return le texte reconnu, de type String
     */
    public String getOCRResult(Bitmap bitmap) { //Testé : 6 réussites sur 9 images différentes
        mTess.setImage(bitmap);
        return mTess.getUTF8Text();
    }

    public void onDestroy() {
        if (mTess != null) mTess.end();
    }


}