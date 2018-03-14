package com.mikalh.purchaseorderonline;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.load.engine.executor.FifoPriorityThreadPoolExecutor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Model.Item;
import com.mikalh.purchaseorderonline.TextWatcher.CurcurencyFormater;

public class detailEdit extends AppCompatActivity implements View.OnClickListener {
    private String KEY_ITEM;
    TextInputEditText itemName_edit, price_edit, unit_edit;
    Button updateDo;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    Item item;
    DocumentReference documentReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_edit);

        KEY_ITEM = getIntent().getExtras().getString(myCatalogue.KEY_ITEM);
        firestore = FirebaseFirestore.getInstance();
        itemName_edit = findViewById(R.id.itemName_edit);
        price_edit = findViewById(R.id.price_edit);
        price_edit.addTextChangedListener(new CurcurencyFormater(price_edit));
        unit_edit = findViewById(R.id.unit_edit);
        updateDo = findViewById(R.id.updateDo);
        updateDo.setOnClickListener(this);

        documentReference = firestore.collection("Items").document(KEY_ITEM);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    item = documentSnapshot.toObject(Item.class);

                    itemName_edit.setText(item.getNama_barang(), TextView.BufferType.EDITABLE);
                    unit_edit.setText(item.getUnit(), TextView.BufferType.EDITABLE);
                    price_edit.setText(item.getHarga_barang()+"", TextView.BufferType.EDITABLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error get data",e.getMessage());
            }
        });

    }

    @Override
    public void onClick(View view) {

    }
}
