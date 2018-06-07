package com.mikalh.purchaseorderonline;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mikalh.purchaseorderonline.FCM.InstanceIdService;
import com.mikalh.purchaseorderonline.Pager.MainPager;

public class newUserUI extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    ViewPager mainPagger;
    TabLayout mainTab;
    MainPager adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_main);
        mainPagger = findViewById(R.id.mainPagger);
        mainTab = findViewById(R.id.mainTab);
        adapter = new MainPager(getSupportFragmentManager(),3);
        mainPagger.setAdapter(adapter);
        mainPagger.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mainTab));
        mainTab.setOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mainPagger.setCurrentItem(tab.getPosition());
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
