package com.mikalh.purchaseorderonline.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.CreatePODate;
import com.mikalh.purchaseorderonline.CreatePOItem;

public class CreatePOPagger extends FragmentStatePagerAdapter {
    int TabCount;

    public CreatePOPagger(FragmentManager fm, int TabCount) {
        super(fm);
        this.TabCount = TabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CreatePODate();
            case 1:
                return new CreatePOItem();
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return TabCount;
    }
}
