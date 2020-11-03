package com.example.fastuae.util;

import android.content.Context;

import com.adoisstudio.helper.LoadingDialog;
import com.example.fastuae.R;

public class ProgressView {

    public static void show(Context context,LoadingDialog loadingDialog){
        if (loadingDialog!=null){
            loadingDialog.show(context.getResources().getString(R.string.pleaseWait));
        }
    }

    public static void dismiss(LoadingDialog loadingDialog){
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
    }

}
