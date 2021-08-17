package com.autostrad.rentcar.util;

import android.text.Html;

public class RemoveHtml {

    public static String html2text(String html) {
        String result = "";
        try {
            result = Html.fromHtml(html).toString();
        }catch (Exception e){
            result = html;
        }
        return result;
    }

}
