package com.adida.aka.androidgeneral.fragment;


import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adida.aka.androidgeneral.R;
import com.adida.aka.androidgeneral.receiver.LocationReceiver;
import com.adida.aka.androidgeneral.receiver.NameReceiver;
import com.adida.aka.androidgeneral.receiver.NetworkChangeReceiver;

/**
 * A simple {@link Fragment} subclass.
 */
public class BroadcastReceiverFragment extends android.app.Fragment implements View.OnClickListener{

    private static final String ACTION_NETWORK  = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final String ACTION_LOCATION = "android.location.PROVIDERS_CHANGED";

    private boolean isData, isNetwork, isLocation;
    private Button mBtnData, mBtnNetwork, mBtnLocation;
    private TextView mTxtName;

    private NameReceiver mNameReceiver;
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private LocationReceiver mLocationReceiver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_broadcast_receiver, container, false);
        mBtnData = view.findViewById(R.id.btn_data);
        mBtnNetwork = view.findViewById(R.id.btn_network);
        mBtnLocation = view.findViewById(R.id.btn_location);
        mTxtName     = view.findViewById(R.id.txt_name);

        mBtnData.setOnClickListener(this);
        mBtnLocation.setOnClickListener(this);
        mBtnNetwork.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_data:
                listenerData();
                break;
            case R.id.btn_network:
                listenerNetwork();
                break;
            case R.id.btn_location:
                listenerLocation();
                break;
        }
    }

    /**
     * Listener change network
     */
    private void listenerNetwork() {
        if (!isNetwork){
            isNetwork = true;
            mBtnNetwork.setText("Stop Listener Network");
            mNetworkChangeReceiver = new NetworkChangeReceiver(getActivity());
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_NETWORK);
            getActivity().registerReceiver(mNetworkChangeReceiver, intentFilter);

        }else {
            isNetwork = false;
            mBtnNetwork.setText("Listener Network");
            getActivity().unregisterReceiver(mNetworkChangeReceiver);

        }
    }

    /**
     * Lister change location
     */
    private void listenerLocation() {
        if(!isLocation){
            isLocation = true;
            mBtnLocation.setText("Stop Listener Location");
            mLocationReceiver = new LocationReceiver();   
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION_LOCATION);
            getActivity().registerReceiver(mLocationReceiver, intentFilter);
        }else {
            isLocation = false;
            mBtnLocation.setText("Listener Location");
            getActivity().unregisterReceiver(mLocationReceiver);
        }
    }

    /**
     *  Listener reciver data from send broadcast
     */
    private void listenerData() {
        if (!isData){
            isData = true;
            mBtnData.setText("Stop Listener Data");
            mNameReceiver = new NameReceiver(new NameReceiver.CallBack() {
                @Override
                public void getData(String data) {
                    mTxtName.setText("Name: "+data);
                    Toast.makeText(getActivity(), "Name: " + data, Toast.LENGTH_SHORT).show();
                }
            });
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(mNameReceiver.ACTION_SEND);
            getActivity().registerReceiver(mNameReceiver, intentFilter);
        }else {
            isData = false;
            mBtnData.setText("Listener Data");
            getActivity().unregisterReceiver(mNameReceiver);
        }


    }
}
