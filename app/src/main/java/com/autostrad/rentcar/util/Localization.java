package com.autostrad.rentcar.util;

import android.content.Context;
import android.content.res.Configuration;


import java.util.Locale;

public class Localization {

    public static void setLocal(Context context,String language) {
        Locale local = new Locale(language);
        Locale.setDefault(local);
        Configuration config = new Configuration();
        config.locale = local;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static void loadLocal(Context context,String language) {
        setLocal(context,language);
    }


}
