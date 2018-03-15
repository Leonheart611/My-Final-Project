package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.CatalogueAdapter;

public class myCatalogue extends AppCompatActivity implements CatalogueAdapter.OnClickCatalogueListener {
    public static final String KEY_ITEM = "keyItem";
    RecyclerView catalogue_rv;
    Query query;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    ConstraintLayout addItem_layout;

    CatalogueAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("My Catalogue");
        setContentView(R.layout.activity_my_catalogue);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        catalogue_rv = findViewById(R.id.calatogue_rv);
        addItem_layout = findViewById(R.id.addItem_layout);


        firestore = FirebaseFirestore.getInstance();
        String id = user.getUid();
        query = FirebaseFirestore.getInstance().collection("Items").whereEqualTo("userId",user.getUid());
        adapter = new CatalogueAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public void onBindViewHolder(CatalogueHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            public CatalogueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("Error Adapter",e.getMessage());
            }
        };
        LinearLayoutManager lilm = new LinearLayoutManager(this);
        lilm.setOrientation(LinearLayoutManager.VERTICAL);
        catalogue_rv.setLayoutManager(lilm);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(catalogue_rv.getContext(),lilm.getOrientation());
        catalogue_rv.addItemDecoration(itemDecoration);
        catalogue_rv.setAdapter(adapter);

        addItem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(myCatalogue.this,addItem.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter !=null){
            adapter.stopListening();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter !=null){
            adapter.startListening();
        }
    }

    @Override
    public void onClickCatalogueListener(DocumentSnapshot item) {
        Intent i = new Intent(this,detailEdit.class);
        i.putExtra(KEY_ITEM,item.getId());
        startActivity(i);
    }
}
