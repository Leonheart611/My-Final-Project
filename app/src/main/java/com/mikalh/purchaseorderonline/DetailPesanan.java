package com.mikalh.purchaseorderonline;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mikalh.purchaseorderonline.Pager.DetailPesananPagger;
import com.pixelcan.inkpageindicator.InkPageIndicator;

public class DetailPesanan extends AppCompatActivity {
    ViewPager paggerDetailPesanan;
    DetailPesananPagger adapter;
    InkPageIndicator inkPageIndicator2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan);
        paggerDetailPesanan = findViewById(R.id.paggerDetailPesanan);
        adapter = new DetailPesananPagger(getSupportFragmentManager(),2);
        paggerDetailPesanan.setAdapter(adapter);
        inkPageIndicator2 = findViewById(R.id.createPoIndicator2);
        inkPageIndicator2.setViewPager(paggerDetailPesanan);
    }

    @Override
    public void onBackPressed() {
        if (paggerDetailPesanan.getCurrentItem()!=0){
            paggerDetailPesanan.setCurrentItem(paggerDetailPesanan.getCurrentItem()-1,true);
        }else {
            super.onBackPressed();
        }
    }
}
