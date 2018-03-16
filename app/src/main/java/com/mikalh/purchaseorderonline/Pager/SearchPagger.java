package com.mikalh.purchaseorderonline.Pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mikalh.purchaseorderonline.catalogueItemSearch;
import com.mikalh.purchaseorderonline.companyNameSearch;

/**
 * Created by mika.frentzen on 16/03/2018.
 */

public class SearchPagger extends FragmentStatePagerAdapter{
        int tabCount;
        public SearchPagger(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                companyNameSearch companyNameSearch = new companyNameSearch();
                return companyNameSearch;
            case 1:
                catalogueItemSearch catalogueItemSearch = new catalogueItemSearch();
                return catalogueItemSearch;
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
