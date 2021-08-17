package com.autostrad.rentcar.util;

import android.app.Activity;
import android.os.Build;

import com.autostrad.rentcar.R;

public class WindowView {

    public static void getWindow(Activity activity){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.iconicGray));
            }
        }catch (Exception e){
        }
    }
}
