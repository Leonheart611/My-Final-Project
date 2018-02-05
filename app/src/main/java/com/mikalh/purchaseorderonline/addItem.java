package com.mikalh.purchaseorderonline;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikalh.purchaseorderonline.Model.Item;

public class addItem extends AppCompatActivity {
    EditText namaItem_add, jenisItem_add, hargaItem_add,quantitasItem_add, deskripsiItem_add;
    Button addItemDo;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        namaItem_add = findViewById(R.id.namaItem_add);
        jenisItem_add = findViewById(R.id.jenisItem_add);
        hargaItem_add = findViewById(R.id.hargaItem_add);
        quantitasItem_add = findViewById(R.id.quantitasItem_add);
        deskripsiItem_add = findViewById(R.id.deskripsiItem_add);
        addItemDo = findViewById(R.id.addItemDo);
        //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
        addItemDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }
    void saveItem(){
        String namaBarang = namaItem_add.getText().toString();
        String jenisBarang = jenisItem_add.getText().toString();
        String Deskrpsi = deskripsiItem_add.getText().toString();
        int HargaBarang = Integer.parseInt(hargaItem_add.getText().toString());
        int banyakStok = Integer.parseInt(quantitasItem_add.getText().toString());
        String userId = user.getUid();

        Item item = new Item(namaBarang,jenisBarang,Deskrpsi,"",userId,HargaBarang,banyakStok);

        firestore.collection("Items").document()
                .set(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(addItem.this,"item added Success",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(addItem.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

}
