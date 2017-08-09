package com.adida.aka.androidgeneral.widget;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Aka on 8/10/2017.
 */

public class Utilities {

    /**
     * Check state internet
     * @param context
     * @return
     */
    public static boolean checkNetwok(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
