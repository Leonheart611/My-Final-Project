package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Model.Item;
import com.mikalh.purchaseorderonline.TextWatcher.CurcurencyFormater;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class detailEdit extends AppCompatActivity implements View.OnClickListener {
    private String KEY_ITEM;
    TextInputEditText itemName_edit, price_edit, unit_edit;
    Button updateDo;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    Item item;
    DocumentReference documentReference;
    ImageView itemImage_detailEdit;
    TextView deleteDetailEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Detail Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_detail_edit);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        itemImage_detailEdit = findViewById(R.id.itemImage_detailEdit);
        KEY_ITEM = getIntent().getExtras().getString(myCatalogue.KEY_ITEM);
        firestore = FirebaseFirestore.getInstance();
        itemName_edit = findViewById(R.id.itemName_edit);
        price_edit = findViewById(R.id.price_edit);
        price_edit.addTextChangedListener(new CurcurencyFormater(price_edit));
        unit_edit = findViewById(R.id.unit_edit);
        updateDo = findViewById(R.id.updateDo);
        updateDo.setOnClickListener(this);
        deleteDetailEdit = findViewById(R.id.deleteDetailEdit);
        deleteDetailEdit.setOnClickListener(this);
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
                    if (!item.getImageItemUrl().isEmpty()){
                        Glide.with(itemImage_detailEdit.getContext()).load(item.getImageItemUrl())
                                .into(itemImage_detailEdit);
                    }
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

        if (view == updateDo){
            String namaBarang = itemName_edit.getText().toString();
            String HargaBarang = price_edit.getText().toString();
            String unitItem = unit_edit.getText().toString();
            String urlItemBarang="";

            Map<String, Object> updates = new HashMap<>();
            updates.put("harga_barang",HargaBarang);
            updates.put("nama_barang",namaBarang);
            updates.put("unit",unitItem);

            firestore.collection("Items").document(KEY_ITEM).update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(detailEdit.this,"Update Complete",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(detailEdit.this,"Failed on update",Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(detailEdit.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.e("error update data",e.getMessage());
                }
            });
        }
        if (view == deleteDetailEdit){
            warningPopUp();
        }
    }

    void warningPopUp(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.warning_delete_item);

        final Button yes_warning = dialog.findViewById(R.id.yes_warning);
        final Button no_warning = dialog.findViewById(R.id.no_warning);

        yes_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Items").document(KEY_ITEM).delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                    onBackPressed();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.logcat(Log.ERROR, "Delete Item Failed", "NPE caught");
                        FirebaseCrash.report(e);
                    }
                });
            }
        });
        no_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                super.onBackPressed();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }
}
