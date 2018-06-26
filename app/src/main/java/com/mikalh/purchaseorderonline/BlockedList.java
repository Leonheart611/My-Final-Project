package com.mikalh.purchaseorderonline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.BlockRequestAdapter;

public class BlockedList extends AppCompatActivity implements BlockRequestAdapter.OnBlockRequestListener {
    FirebaseFirestore firestore;
    Query query;
    RecyclerView blockedListRV_admin;
    BlockRequestAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_list);
        setTitle("Blocked List");
        firestore = FirebaseFirestore.getInstance();
        query = firestore.collection("BlockRequest").whereEqualTo("Done",true);
        blockedListRV_admin = findViewById(R.id.blockedListRV_admin);
        adapter = new BlockRequestAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Crashlytics.logException(e);
            }
        };
        blockedListRV_admin.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(BlockedList.this);
        llm.setSmoothScrollbarEnabled(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(blockedListRV_admin.getContext(), llm.getOrientation());
        blockedListRV_admin.setLayoutManager(llm);
        blockedListRV_admin.addItemDecoration(itemDecoration);

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
    public void onBlockRequestListener(DocumentSnapshot data) {

    }
}
