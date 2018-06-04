package com.mikalh.purchaseorderonline;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikalh.purchaseorderonline.Adapter.POActiveAdapter;

public class SendPO extends AppCompatActivity implements POActiveAdapter.OnPOSelectedListener{
    RecyclerView poActive_RC;
    POActiveAdapter adapter;
    FirebaseUser user;
    FirebaseAuth auth;
    Query query;
    FirebaseFirestore firestore;
    public static String KEY_UID = "id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_po);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        poActive_RC = findViewById(R.id.poActive_RC);
        query = firestore.collection("Cart")
                .whereEqualTo("UserList."+user.getUid(),true)
                .whereEqualTo("MakePO",false);
        adapter = new POActiveAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public void onBindViewHolder(POActive holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Crashlytics.logException(e);
            }
        };
        poActive_RC.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setSmoothScrollbarEnabled(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        poActive_RC.setLayoutManager(llm);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.stopListening();
    }

    @Override
    public void onPOSelectedListener(DocumentSnapshot transaction) {
        String ID = transaction.getId();
        Intent i =  new Intent(SendPO.this,CreatePO.class);
        i.putExtra(KEY_UID,ID);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
