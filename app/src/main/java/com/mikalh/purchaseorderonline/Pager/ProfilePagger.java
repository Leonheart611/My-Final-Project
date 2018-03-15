package com.mikalh.purchaseorderonline.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.PICContact;
import com.mikalh.purchaseorderonline.companyProfile;

/**
 * Created by mika.frentzen on 15/03/2018.
 */

public class ProfilePagger  extends FragmentStatePagerAdapter{
    int tabCount;
    public ProfilePagger(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                companyProfile companyProfile = new companyProfile();
                return companyProfile;
            case 1:
                PICContact picContact = new PICContact();
                return picContact;
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
