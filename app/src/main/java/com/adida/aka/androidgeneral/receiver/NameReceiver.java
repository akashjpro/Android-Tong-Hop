package com.adida.aka.androidgeneral.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by tmha on 8/9/2017.
 */

public class NameReceiver extends BroadcastReceiver {

    public static final String ACTION_SEND =  "com.example.tmha.sendbroadcast.ACTION_SEND";
    public static final String BUNDLE_NAME = "Name";
    private CallBack mCallBack;

    public NameReceiver(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case ACTION_SEND:
                Bundle bundle = intent.getExtras();
                String name = bundle.getString(BUNDLE_NAME);
                mCallBack.getData(name);
                break;
        }
    }

    public interface CallBack{
        void getData(String data);
    }
}
