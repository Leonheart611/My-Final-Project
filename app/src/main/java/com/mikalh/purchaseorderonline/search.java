package com.mikalh.purchaseorderonline;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toolbar;

import com.crashlytics.android.Crashlytics;
import com.firebase.ui.auth.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.ItemAdapter;
import com.mikalh.purchaseorderonline.Pager.SearchPagger;

public class search extends AppCompatActivity implements ItemAdapter.OnItemSelectedListener{
    private Toolbar searchToolBar;
    TabLayout searchTab;
    Query query,queryKategori;
    FirebaseFirestore firestore;
    FirebaseUser user;
    FirebaseAuth auth;
    CardView searchItem_cardView;
    ItemAdapter itemAdapter,itemAdapterKategori;
    RecyclerView itemRV_search;
    public static final String KEY_ITEM_ID = "keyItemID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Search");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("Items").orderBy("nama_barang", Query.Direction.ASCENDING);
        itemRV_search = findViewById(R.id.itemRV_search);
        itemAdapter = new ItemAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Crashlytics.logException(e);
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

        };
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        itemRV_search.setLayoutManager(horizontalLayoutManagaer);
        itemRV_search.setItemAnimator(new DefaultItemAnimator());
        itemRV_search.setAdapter(itemAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (itemAdapter != null){
            itemAdapter.startListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (itemAdapter!=null){
            itemAdapter.stopListening();
        }
    }

    @Override
    public void onItemSelected(DocumentSnapshot item) {
        Intent i = new Intent(this,detailItem.class);
        i.putExtra(KEY_ITEM_ID,item.getId());
        startActivity(i);
    }

}
