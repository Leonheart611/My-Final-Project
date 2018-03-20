package com.mikalh.purchaseorderonline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.CartAdapter;

public class cartUI extends AppCompatActivity implements CartAdapter.OnCartSelectedListener {
    RecyclerView cart_recylerView;
    CartAdapter adapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_ui);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        query = firestore.collection("Users").document(user.getUid()).collection("Cart");
        cart_recylerView = findViewById(R.id.cart_RV);
        adapter = new CartAdapter(query,this){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public void onBindViewHolder(CartHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            public CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("Error Adapater",e.getMessage());
            }
        };

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        cart_recylerView.setLayoutManager(llm);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(cart_recylerView.getContext(),llm.getOrientation());
        cart_recylerView.addItemDecoration(itemDecoration);
        cart_recylerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter!=null){
            adapter.stopListening();
        }
    }

    @Override
    public void onCartSelected(DocumentSnapshot cart) {

    }
}
