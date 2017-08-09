package com.adida.aka.androidgeneral.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adida.aka.androidgeneral.activity.FrameActivity;
import com.adida.aka.androidgeneral.activity.Gird;
import com.adida.aka.androidgeneral.activity.LinearActivity;
import com.adida.aka.androidgeneral.activity.Relative;
import com.adida.aka.androidgeneral.activity.Table;
import com.adida.aka.androidgeneral.R;

/**
 * Created by Aka on 8/8/2017.
 */

public class LayoutFragment extends Fragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        view.findViewById(R.id.btn_frame_layout).setOnClickListener(this);
        view.findViewById(R.id.btn_gird_layout).setOnClickListener(this);
        view.findViewById(R.id.btn_linear_layout).setOnClickListener(this);
        view.findViewById(R.id.btn_Relative_layout).setOnClickListener(this);
        view.findViewById(R.id.btn_table_layout).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_linear_layout:
                getActivity().startActivity(new Intent(getActivity(), LinearActivity.class));
                break;
            case R.id.btn_frame_layout:
                getActivity().startActivity(new Intent(getActivity(), FrameActivity.class));
                break;
            case R.id.btn_Relative_layout:
                getActivity().startActivity(new Intent(getActivity(), Relative.class));
                break;
            case R.id.btn_gird_layout:
                getActivity().startActivity(new Intent(getActivity(), Gird.class));
                break;
            case R.id.btn_table_layout:
                getActivity().startActivity(new Intent(getActivity(), Table.class));
                break;

        }
    }
}
