package com.mikalh.purchaseorderonline;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Pager.CreatePOPagger;
import com.pixelcan.inkpageindicator.InkPageIndicator;

public class CreatePO extends AppCompatActivity {
    ViewPager createPOPagger;
    InkPageIndicator inkPageIndicator;
    CreatePOPagger pagger;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_po);
        createPOPagger = findViewById(R.id.createPoPagger);
        inkPageIndicator = findViewById(R.id.createPoIndicator);
        pagger = new CreatePOPagger(getSupportFragmentManager(),2);
        createPOPagger.setAdapter(pagger);
        inkPageIndicator.setViewPager(createPOPagger);
    }

    @Override
    public void onBackPressed() {
        if (createPOPagger.getCurrentItem() !=0){
            createPOPagger.setCurrentItem(createPOPagger.getCurrentItem()-1,true);
        }else {
            super.onBackPressed();
        }
    }
}
