package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class userUI extends AppCompatActivity {
    FloatingActionButton addItem;
    RecyclerView myRecyler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ui);
        addItem = findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(userUI.this, com.mikalh.purchaseorderonline.addItem.class);
                startActivity(i);
            }
        });
    }
}
