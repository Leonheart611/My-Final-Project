package com.mikalh.purchaseorderonline.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.registerCompanyProfile;
import com.mikalh.purchaseorderonline.registerUser;

/**
 * Created by mika.frentzen on 12/03/2018.
 */

public class RegisterPager extends FragmentStatePagerAdapter {
    int tabCount;
    public RegisterPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                registerCompanyProfile registerCompanyProfile = new registerCompanyProfile();
                return registerCompanyProfile;
            case 1:
                registerUser registerUser = new registerUser();
                return registerUser;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
