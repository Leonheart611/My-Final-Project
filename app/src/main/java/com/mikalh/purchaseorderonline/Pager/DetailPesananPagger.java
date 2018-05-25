package com.mikalh.purchaseorderonline.Pager;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.DetailPesanan_DataItem;
import com.mikalh.purchaseorderonline.DetailPesanan_Status;

public class DetailPesananPagger  extends FragmentStatePagerAdapter {
    int TabCount;

    public DetailPesananPagger(FragmentManager fm,int TabCount) {
        super(fm);
        this.TabCount = TabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new DetailPesanan_DataItem();
            case 1:
                return new DetailPesanan_Status();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}
