package com.adida.aka.androidgeneral.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.adida.aka.androidgeneral.R;
import com.adida.aka.androidgeneral.widget.Constans;

public class UpdateActivity extends AppCompatActivity {

    private TextView mTxtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        mTxtVersion = (TextView) findViewById(R.id.txt_version);

        int i = getIntent().getIntExtra(Constans.EXTRA_VERSION, 1);
        mTxtVersion.setText(i + "");


    }
}
