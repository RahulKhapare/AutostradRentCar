package com.autostrad.rentcar.util;

import android.view.View;

public class Click {

    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            public void run() {
                view.setEnabled(true);
            }
        }, 1000);
    }

}
