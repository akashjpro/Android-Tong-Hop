package com.adida.aka.androidgeneral.adapter;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Aka on 8/8/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> listFragment = new ArrayList<>();
    private ArrayList<String>   listTitle    = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    public void addFragmentAndTitle(Fragment fragment, String title){
        listFragment.add(fragment);
        listTitle.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }
}
