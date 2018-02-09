package com.mikalh.purchaseorderonline;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Model.Item;

public class detailItem extends AppCompatActivity {
    private String KEY_ITEM_ID;
    TextView namaBarang_detail,jenisBarang_detail,hargaBarang_detail
            ,quantitasBarang_detail,diskripsiBarang_detail;
    FirebaseFirestore firestore;
    DocumentReference documentReference;
    ProgressBar mybar;
    Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        namaBarang_detail = findViewById(R.id.namaBarang_detail);
        jenisBarang_detail = findViewById(R.id.jenisBarang_detail);
        hargaBarang_detail = findViewById(R.id.hargaBarang_detail);
        quantitasBarang_detail = findViewById(R.id.quantitasBarang_detail);
        diskripsiBarang_detail = findViewById(R.id.diskripsiBarang_detail);
        mybar = findViewById(R.id.mybar);
        firestore = FirebaseFirestore.getInstance();
        KEY_ITEM_ID = getIntent().getExtras().getString(userUI.KEY_ITEM_ID);

        documentReference = firestore.collection("Items").document(KEY_ITEM_ID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {

            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    item = documentSnapshot.toObject(Item.class);
                    mybar.setVisibility(View.GONE);

                    namaBarang_detail.setText(item.getNama_barang());
                    namaBarang_detail.setVisibility(View.VISIBLE);
                    jenisBarang_detail.setText(item.getJenis_barang());
                    jenisBarang_detail.setVisibility(View.VISIBLE);
                    hargaBarang_detail.setText(item.getHarga_barang()+"");
                    hargaBarang_detail.setVisibility(View.VISIBLE);
                    quantitasBarang_detail.setText(item.getNama_barang());
                    quantitasBarang_detail.setVisibility(View.VISIBLE);
                    diskripsiBarang_detail.setText(item.getDiskripsi_barang());
                    diskripsiBarang_detail.setVisibility(View.VISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error Get Data",e.getMessage());
            }
        });

    }
}
