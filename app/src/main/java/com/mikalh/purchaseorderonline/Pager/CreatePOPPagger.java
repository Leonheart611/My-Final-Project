package com.mikalh.purchaseorderonline.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.TanggalPO;

public class CreatePOPPagger extends FragmentStatePagerAdapter {
    int tabCount;

    public CreatePOPPagger(FragmentManager fm,int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TanggalPO();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
