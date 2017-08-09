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
import com.adida.aka.androidgeneral.receiver.NameReceiver;
import com.adida.aka.androidgeneral.receiver.NetworkChangeBroadcast;

/**
 * A simple {@link Fragment} subclass.
 */
public class BroadcastReceiverFragment extends android.app.Fragment implements View.OnClickListener{

    private Button mBtnData, mBtnNetwork, mBtnLocation;
    private NameReceiver mNameReceiver;
    private NetworkChangeBroadcast mNetworkChangeBroadcast;
    private TextView mTxtName;

    private boolean isData, isNetwork, isLocation;

    public BroadcastReceiverFragment() {
        // Required empty public constructor
    }


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

    private void listenerNetwork() {
        if (!isNetwork){
            isNetwork = true;
            mBtnNetwork.setText("Stop Listener Network");
            mNetworkChangeBroadcast = new NetworkChangeBroadcast(getActivity());
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            getActivity().registerReceiver(mNetworkChangeBroadcast, intentFilter);

        }else {
            isNetwork = false;
            mBtnNetwork.setText("Listener Network");
            getActivity().unregisterReceiver(mNetworkChangeBroadcast);

        }
    }

    private void listenerLocation() {
        if(!isLocation){
            isLocation = true;
            mBtnLocation.setText("Listener Location");
        }else {
            isLocation = false;
            mBtnLocation.setText("Stop Listener Location");
        }
    }

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
