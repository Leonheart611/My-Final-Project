package com.mikalh.purchaseorderonline.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.Chat_buyyer;
import com.mikalh.purchaseorderonline.Transaction_buyyer;
import com.mikalh.purchaseorderonline.home_buyyer;

public class BuyyerPagger extends FragmentStatePagerAdapter {
    int tabCount;
    public BuyyerPagger(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new home_buyyer();
            case 1:
                return new Transaction_buyyer();
            case 2:
                return new Chat_buyyer();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
