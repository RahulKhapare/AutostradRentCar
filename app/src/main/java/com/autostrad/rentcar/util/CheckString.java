package com.autostrad.rentcar.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

public class CheckString {

    public static String checkAndHide(String string, LinearLayout linearLayout){
        String value = "";
        if (!TextUtils.isEmpty(string) && !string.equals("null")){
            value = string.trim();
        }else {
            linearLayout.setVisibility(View.GONE);
        }
        return value;
    }

    public static String check(String string){
        String value = "";
        if (!TextUtils.isEmpty(string) && !string.equals("null")){
            value = string.trim();
        }
        return value;
    }
}
