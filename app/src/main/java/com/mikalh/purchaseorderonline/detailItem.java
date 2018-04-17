package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Model.Cart;
import com.mikalh.purchaseorderonline.Model.Item;

public class detailItem extends AppCompatActivity {
    private String KEY_ITEM_ID;
    TextInputEditText namaBarang_detail,hargaBarang_detail,unit_detail;
    ImageView imageBarang_detail;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference documentReference;
    CustomDialog customDialog;
    Item item;
    Button beliButton_do;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Detail Item");
        setContentView(R.layout.activity_detail_item);
        customDialog = new CustomDialog(detailItem.this);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        namaBarang_detail = findViewById(R.id.namaBarang_detail);
        hargaBarang_detail = findViewById(R.id.hargaBarang_detail);
        unit_detail = findViewById(R.id.unit_detail);
        imageBarang_detail = findViewById(R.id.imageBarang_detail);
        beliButton_do = findViewById(R.id.beliButton_do);
        firestore = FirebaseFirestore.getInstance();
        KEY_ITEM_ID = getIntent().getExtras().getString(userUI.KEY_ITEM_ID);
        customDialog.show();
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
                    namaBarang_detail.setText(item.getNama_barang());;
                    hargaBarang_detail.setText(item.getHarga_barang()+"");
                    unit_detail.setText(item.getUnit()+"");
                    Glide.with(imageBarang_detail.getContext())
                            .load(item.getImageItemUrl())
                            .into(imageBarang_detail);
                    customDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error Get Data",e.getMessage());
            }
        });

        beliButton_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupBuyCart();
            }
        });


    }
    void popupBuyCart(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_buy_cart);

        final Button saveToCart = dialog.findViewById(R.id.saveToCart);
        final EditText banyakPCS_popCart = dialog.findViewById(R.id.banyakPCS_popCart);

        saveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pcs = Integer.parseInt(banyakPCS_popCart.getText().toString());
                Cart cart = new Cart(item.getNama_barang(),user.getUid(),item.getUnit(),item.getHarga_barang(),item.getImageItemUrl(),pcs,false);
                // add database firestore
                firestore.collection("Users").document(user.getUid()).collection("Cart").document().set(cart).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Gagal Save To DB",e.getMessage());
                        Toast.makeText(detailItem.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(detailItem.this,"Barang sudah masuk di Cart",Toast.LENGTH_LONG).show();
                            dialog.show();
                        }
                    }
                });
                // add database firestore


            }
        });
        dialog.show();
    }
}
