package com.mikalh.purchaseorderonline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView companyList_admin,blockedList_admin, blockRequest_admin;
    TextView companyListTxt_admin,blockedListTxt_admin,blockRequestTxt_admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Admin");
        setContentView(R.layout.activity_admin);
        companyList_admin = findViewById(R.id.companyList_admin);
        companyListTxt_admin = findViewById(R.id.companyListTxt_admin);
        blockedList_admin = findViewById(R.id.blockedList_admin);
        blockedListTxt_admin = findViewById(R.id.blockedListTxt_admin);
        blockRequest_admin = findViewById(R.id.blockRequest_admin);
        blockRequestTxt_admin = findViewById(R.id.blockRequestTxt_admin);

    }

    @Override
    public void onClick(View v) {
        if (v == companyList_admin || v== companyListTxt_admin){

        }if (v == blockedList_admin || v == blockedListTxt_admin){

        }if (v == blockRequest_admin || v == blockRequestTxt_admin){

        }
    }
}
