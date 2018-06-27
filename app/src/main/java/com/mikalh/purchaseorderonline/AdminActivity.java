package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InterruptedIOException;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView companyList_admin,blockedList_admin, blockRequest_admin,logOutAdmin;
    TextView companyListTxt_admin,blockedListTxt_admin,blockRequestTxt_admin,logOutAdminTxt;

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
        logOutAdmin = findViewById(R.id.logOutAdmin);
        logOutAdminTxt = findViewById(R.id.logOutAdminTxt);
        logOutAdminTxt.setOnClickListener(this);
        logOutAdmin.setOnClickListener(this);
        companyList_admin.setOnClickListener(this);
        companyListTxt_admin.setOnClickListener(this);
        blockedList_admin.setOnClickListener(this);
        blockedListTxt_admin.setOnClickListener(this);
        blockRequest_admin.setOnClickListener(this);
        blockRequestTxt_admin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == companyList_admin || v== companyListTxt_admin){
            Intent i = new Intent(this, CompanyList_admin.class);
            startActivity(i);
        }if (v == blockedList_admin || v == blockedListTxt_admin){
            Intent i = new Intent(this,BlockedList.class);
            startActivity(i);
        }if (v == blockRequest_admin || v == blockRequestTxt_admin){
            Intent i = new Intent(this,BlockRequest.class);
            startActivity(i);
        }if (v == logOutAdmin || v == logOutAdminTxt){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
