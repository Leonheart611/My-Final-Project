package com.mikalh.purchaseorderonline;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mikalh.purchaseorderonline.Pager.POBuyerPagger;
import com.pixelcan.inkpageindicator.InkPageIndicator;

public class POBuyer extends AppCompatActivity {
    ViewPager poBuyer_Pagger;
    InkPageIndicator poBuyerIndicator;
    POBuyerPagger pagger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_po__buyer);
        poBuyer_Pagger = findViewById(R.id.poBuyer_Pagger);
        poBuyerIndicator = findViewById(R.id.poBuyerIndicator);
        pagger = new POBuyerPagger(getSupportFragmentManager(),2);
        poBuyer_Pagger.setAdapter(pagger);
        poBuyerIndicator.setViewPager(poBuyer_Pagger);
    }

    @Override
    public void onBackPressed() {
        if (poBuyer_Pagger.getCurrentItem()!=0){
            poBuyer_Pagger.setCurrentItem(poBuyer_Pagger.getCurrentItem()-1,true);
        }else {
            super.onBackPressed();
        }
    }
}
