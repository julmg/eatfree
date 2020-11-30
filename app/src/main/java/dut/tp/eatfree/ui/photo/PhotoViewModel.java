package dut.tp.eatfree.ui.photo;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dut.tp.eatfree.MainActivity;

public class PhotoViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private TessOCR mTessOCR;
    private ProgressDialog mProgressDialog;
    private MainActivity ocrView;

    public PhotoViewModel(MainActivity activity) {
        ocrView = activity;
        mText = new MutableLiveData<>();
        mTessOCR = new TessOCR(ocrView, "fra");
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public String getTextFromImage(Bitmap bitmap){
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(ocrView, "Processing",
                    "Doing OCR...", true);
        } else {
            mProgressDialog.show();
        }
        final String[] ocrText = {""};
        new Thread(new Runnable() {
            public void run() {
                final String srcText = mTessOCR.getOCRResult(bitmap);
                ocrView.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (srcText != null && !srcText.equals("")) {
                            ocrText[0] = srcText;
                        }
                        mTessOCR.onDestroy();
                        mProgressDialog.dismiss();
                    }
                });
            }
        }).start(); //TODO récupérer le thread quand il se termine
        return ocrText[0];
    }
}