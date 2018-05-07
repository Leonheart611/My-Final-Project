package com.mikalh.purchaseorderonline;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mikalh.purchaseorderonline.Pager.BuyyerPagger;

public class buyerActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    TabLayout buyyerTab;
    ViewPager buyyerView;
    BuyyerPagger adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_buyer);
        buyyerTab = findViewById(R.id.buyyerTab);
        buyyerView = findViewById(R.id.buyyerView);
        adapter = new BuyyerPagger(getSupportFragmentManager(),3);
        buyyerView.setAdapter(adapter);
        buyyerView.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(buyyerTab));
        buyyerTab.setOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        buyyerView.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
