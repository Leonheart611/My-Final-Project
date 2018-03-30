package com.mikalh.purchaseorderonline;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Pager.ProfilePagger;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    CircleImageView profile_image;
    TabLayout profile_tab;
    ViewPager profile_pagger;
    FloatingActionButton changImage_profile;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore firestore;
    ProfilePagger adapter;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Profile");
        setContentView(R.layout.activity_profile);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userID = getIntent().getExtras().getString(home.USER_ID);
        profile_image = findViewById(R.id.imageProfile);
        profile_tab = findViewById(R.id.profile_tab);
        profile_pagger = findViewById(R.id.profile_pagger);
        changImage_profile = findViewById(R.id.changeImage_profile);
        if (!user.getUid().equals(userID)){
            changImage_profile.hide();
        }
        adapter = new ProfilePagger(getSupportFragmentManager(),2);
        profile_pagger.setAdapter(adapter);
        profile_tab.setOnTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        profile_pagger.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
