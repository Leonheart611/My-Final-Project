package com.mikalh.purchaseorderonline.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.Cart_buyyer;
import com.mikalh.purchaseorderonline.Transaction_buyyer;
import com.mikalh.purchaseorderonline.home_buyyer;

public class BuyyerPagger extends FragmentStatePagerAdapter {
    public BuyyerPagger(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new home_buyyer();
            case 1:
                return new Cart_buyyer();
            case 3:
                return new Transaction_buyyer();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
