package com.mikalh.purchaseorderonline.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.Transaction_fragment;
import com.mikalh.purchaseorderonline.chat;
import com.mikalh.purchaseorderonline.home;

/**
 * Created by mika.frentzen on 13/03/2018.
 */

public class MainPager extends FragmentStatePagerAdapter {
    int tabCount;

    public MainPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                home home = new home();
                return home;
            case 1:
                Transaction_fragment Transaction_fragment = new Transaction_fragment();
                return Transaction_fragment;
            case 2:
                chat chat = new chat();
                return chat;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
