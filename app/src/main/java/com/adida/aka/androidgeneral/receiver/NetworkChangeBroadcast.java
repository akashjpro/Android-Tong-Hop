package com.adida.aka.androidgeneral.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by tmha on 8/9/2017.
 */

public class NetworkChangeBroadcast extends BroadcastReceiver {
    Activity mContext;

    public NetworkChangeBroadcast(Activity mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isNetworkConnected()){
            Toast.makeText(mContext, "Connected", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mContext, "Disconnected", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
