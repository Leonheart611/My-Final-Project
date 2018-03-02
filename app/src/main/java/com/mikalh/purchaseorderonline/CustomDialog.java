package com.mikalh.purchaseorderonline;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

/**
 * Created by mika.frentzen on 02/03/2018.
 */

public class CustomDialog extends Dialog {
    public Activity activity;
    ProgressBar progressBar;
    public CustomDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Loading");
        setContentView(R.layout.loading);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressBar = findViewById(R.id.progressBar_Loading);
        progressBar.setProgressDrawable(new ColorDrawable(Color.BLUE));
    }
}
