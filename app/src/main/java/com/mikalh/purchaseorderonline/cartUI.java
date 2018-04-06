package com.mikalh.purchaseorderonline;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikalh.purchaseorderonline.Adapter.CartAdapter;
import com.mikalh.purchaseorderonline.Model.Cart;

public class cartUI extends AppCompatActivity implements CartAdapter.OnCartSelectedListener {
    RecyclerView cart_recylerView;
    CartAdapter adapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    Query query;
    Button MakePO_do;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Cart");
        setContentView(R.layout.activity_cart_ui);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        query = firestore.collection("Users").document(user.getUid()).collection("Cart");
        cart_recylerView = findViewById(R.id.cart_RV);
        MakePO_do = findViewById(R.id.MakePO_do);
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
        MakePO_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference collectionReference = firestore.collection("Users").document(user.getUid()).collection("Cart");
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (DocumentSnapshot data : task.getResult()){
                                Cart cart =  data.toObject(Cart.class);
                                firestore.collection("Transaction").add(cart).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"Susccess add to Transaction",Toast.LENGTH_LONG).show();
                                            firestore.collection("Users").document(user.getUid()).
                                                    collection("Cart").document().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Susccess Delte Data","Done");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("Error Delte",e.getMessage());
                                                }
                                            });
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Error Pindah To Cart",e.getMessage());
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
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
