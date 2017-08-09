package com.adida.aka.androidgeneral.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.adida.aka.androidgeneral.R;
import com.adida.aka.androidgeneral.adapter.ViewPagerAdapter;
import com.adida.aka.androidgeneral.fragment.AudioFragment;
import com.adida.aka.androidgeneral.fragment.BroadcastReceiverFragment;
import com.adida.aka.androidgeneral.fragment.ControlsFragment;
import com.adida.aka.androidgeneral.fragment.DialogFragment;
import com.adida.aka.androidgeneral.fragment.LayoutFragment;
import com.adida.aka.androidgeneral.fragment.LoadImageFragment;
import com.adida.aka.androidgeneral.fragment.MultiThreadFragment;
import com.adida.aka.androidgeneral.fragment.NotificationsFragment;
import com.adida.aka.androidgeneral.fragment.UploadAndDownLoadFragment;
import com.adida.aka.androidgeneral.fragment.VideoFragment;

import static com.adida.aka.androidgeneral.widget.Constans.REQUEST_CODE_FOLDER;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    UploadAndDownLoadFragment mUploadAndDownLoadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mUploadAndDownLoadFragment = new UploadAndDownLoadFragment();

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        pagerAdapter.addFragmentAndTitle(new LayoutFragment(), "Layouts");
        pagerAdapter.addFragmentAndTitle(new DialogFragment(), "Dialog");
        pagerAdapter.addFragmentAndTitle(new ControlsFragment(), "Controls");
        pagerAdapter.addFragmentAndTitle(new MultiThreadFragment(), "Multi Thread");
//        pagerAdapter.addFragmentAndTitle(new NetworkConnectFragment(), "Network");
        pagerAdapter.addFragmentAndTitle(new LoadImageFragment(), "Load Image");
        pagerAdapter.addFragmentAndTitle(new BroadcastReceiverFragment(), "Broadcast");
        pagerAdapter.addFragmentAndTitle(new NotificationsFragment(), "Notification");
        pagerAdapter.addFragmentAndTitle(new VideoFragment(), "Video");
        pagerAdapter.addFragmentAndTitle(mUploadAndDownLoadFragment, "Upload & Download");
        pagerAdapter.addFragmentAndTitle(new AudioFragment(), "Music");

        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        mTabLayout   = (TabLayout) findViewById(R.id.tablayout);
        mViewPager   = (ViewPager) findViewById(R.id.view_pager);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            mUploadAndDownLoadFragment.setImage(uri);
        }
    }
}
