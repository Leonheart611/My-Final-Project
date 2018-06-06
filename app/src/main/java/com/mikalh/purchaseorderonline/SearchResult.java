package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.CatalogueAdapter;
import com.mikalh.purchaseorderonline.Adapter.ItemAdapter;

public class SearchResult extends AppCompatActivity implements CatalogueAdapter.OnClickCatalogueListener{
    TextView searchText;
    RecyclerView resultSearch_RV;
    FirebaseFirestore firestore;
    FirebaseUser user;
    FirebaseAuth auth;
    Query query;
    String ID;
    CatalogueAdapter adapter;
    public static final String KEY_ITEM_ID = "keyItemID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ID = getIntent().getExtras().getString(DetailChat.IDSeller);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("Items").whereEqualTo("userId",ID);
        adapter = new CatalogueAdapter(query,this){
            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
            }

            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }
        };
        searchText = findViewById(R.id.searchText);
        resultSearch_RV = findViewById(R.id.resultSearch_RV);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        resultSearch_RV.setLayoutManager(llm);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(resultSearch_RV.getContext(), llm.getOrientation());
        resultSearch_RV.addItemDecoration(itemDecoration);
        resultSearch_RV.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onClickCatalogueListener(DocumentSnapshot item) {
        Intent i = new Intent(this,detailItem.class);
        i.putExtra(KEY_ITEM_ID,item.getId());
        startActivity(i);
    }
}
