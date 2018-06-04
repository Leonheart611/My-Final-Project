package com.mikalh.purchaseorderonline.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.POBuyer_Item;
import com.mikalh.purchaseorderonline.POBuyer_date;

public class POBuyerPagger extends FragmentStatePagerAdapter {
    int tabCount;
    public POBuyerPagger(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new POBuyer_Item();
            case 1:
                return new POBuyer_date();
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
