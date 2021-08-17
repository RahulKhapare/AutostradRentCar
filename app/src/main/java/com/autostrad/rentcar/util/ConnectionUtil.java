package com.autostrad.rentcar.util;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionUtil {

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
