package com.mikalh.purchaseorderonline.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.PIC_Profile;
import com.mikalh.purchaseorderonline.company_Profile;

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
                company_Profile company_Profile = new company_Profile();
                return company_Profile;
            case 1:
                PIC_Profile picProfile = new PIC_Profile();
                return picProfile;
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
