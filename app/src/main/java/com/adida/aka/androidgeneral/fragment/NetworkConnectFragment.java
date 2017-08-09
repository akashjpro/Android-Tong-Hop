package com.adida.aka.androidgeneral.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adida.aka.androidgeneral.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkConnectFragment extends android.app.Fragment {


    public NetworkConnectFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_network_connect, container, false);
    }

}
