package com.mikalh.purchaseorderonline;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.mikalh.purchaseorderonline.Model.Cart;
import com.mikalh.purchaseorderonline.Model.Chat;
import com.mikalh.purchaseorderonline.Model.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

public class detailItem extends AppCompatActivity {
    private String KEY_ITEM_ID;
    public static String SENDER_ID = "senderId", RECIEVER_ID = "recieverId",KEY_ID = "ItemID",ROOMID = "RoomId";
    TextInputEditText namaBarang_detail,hargaBarang_detail,unit_detail;
    TextView namaPerusahaan_detail;
    ImageView imageBarang_detail;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference documentReference;
    CustomDialog customDialog;
    Item item;
    Button beliButton_do;
    Calendar myCalendar = Calendar.getInstance();
    Button chatButton;
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
        chatButton = findViewById(R.id.chatButton);
        beliButton_do = findViewById(R.id.beliButton_do);
        namaPerusahaan_detail = findViewById(R.id.namaPerusahaan_detail);
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
                    namaPerusahaan_detail.setText(item.getNamaPerusahaan());
                    Glide.with(imageBarang_detail.getContext())
                            .load(item.getImageItemUrl())
                            .into(imageBarang_detail);
                    customDialog.dismiss();
                    if (item.getUserId() == user.getUid()){
                        beliButton_do.setVisibility(View.GONE);
                        chatButton.setVisibility(View.GONE);
                    }
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

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("RoomChat").whereEqualTo("Users.users1",user.getUid()).whereEqualTo("Users.users2",item.getUserId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.e("Hasil Query masa kosong",task.toString());
                     if (task.isSuccessful()){
                         QuerySnapshot snapshots = task.getResult();
                         if (snapshots.getDocuments().size() != 0){
                             String Id = snapshots.getDocuments().get(0).getId();
                             Log.e("ID data", Id);
                             Intent i = new Intent(detailItem.this,DetailChat.class);
                             i.putExtra(SENDER_ID,user.getUid());
                             i.putExtra(RECIEVER_ID,item.getUserId());
                             i.putExtra(KEY_ID,KEY_ITEM_ID);
                             i.putExtra(ROOMID,Id);
                             startActivity(i);
                         }else {
                             DocumentReference ref = firestore.collection("RoomChat").document();
                             final String myId = ref.getId();
                             HashMap<String,String> users = new HashMap<>();
                             users.put("users1",user.getUid());
                             users.put("users2",item.getUserId());
                             Map<String,Object> roomChat = new HashMap<>();
                             roomChat.put("Users",users);
                             roomChat.put("itemID",KEY_ITEM_ID);
                             roomChat.put("senderName",user.getDisplayName());
                             Log.e("ID Data",myId);
                             firestore.collection("RoomChat").document(myId).set(roomChat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()){
                                         Intent i = new Intent(detailItem.this,DetailChat.class);
                                         i.putExtra(SENDER_ID,user.getUid());
                                         i.putExtra(RECIEVER_ID,item.getUserId());
                                         i.putExtra(KEY_ID,KEY_ITEM_ID);
                                         i.putExtra(ROOMID,myId);
                                         startActivity(i);
                                     }
                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     Crashlytics.logException(e);
                                 }
                             });
                         }
                     }
                    }
                });

            }
        });

    }
    void popupBuyCart(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_buy_cart);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


        final TextView namaBarang_popUP = dialog.findViewById(R.id.namaBarang_popUP);
       /* final EditText tanggalEstimasi = dialog.findViewById(R.id.tanggalEstimasi);*/
        final Button saveToCart = dialog.findViewById(R.id.saveToCart);
        final EditText banyakPCS_popCart = dialog.findViewById(R.id.banyakPCS_popCart);
        namaBarang_popUP.setText(":"+item.getNama_barang());
        // data tanggal untuk cart
        /*final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(tanggalEstimasi);
            }
        };

        tanggalEstimasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(detailItem.this,date,myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
*/
        saveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pcs = Integer.parseInt(banyakPCS_popCart.getText().toString());
                Cart cart = new Cart(item.getNama_barang(),item.getUserId(),item.getUnit()
                        ,item.getNamaPerusahaan(),item.getHarga_barang(),item.getImageItemUrl(),item.getNotificationId(),item.getKategori(),pcs);
                // add database firestore
                Map<String,Object> cartStringMap = new HashMap<>();
                cartStringMap.put("nama_barang",cart.getNama_barang());
                cartStringMap.put("userId", cart.getUserId());
                cartStringMap.put("unit",cart.getUnit());
                cartStringMap.put("namaPerusahaan",cart.getNamaPerusahaan());
                cartStringMap.put("harga_barang",cart.getHarga_barang());
                cartStringMap.put("imageItemUrl",cart.getImageItemUrl());
                cartStringMap.put("notificationId",cart.getNotificationId());
                cartStringMap.put("kategori",cart.getKategori());
                cartStringMap.put("quantitas_banyakBarang",cart.getQuantitas_banyakBarang());
                Map<String,Object> dataBarang = new HashMap<>();
                dataBarang.put("itemList",cartStringMap);
                firestore.collection("Users").document(user.getUid()).collection("Cart").document(item.getNamaPerusahaan()).set(dataBarang).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Crashlytics.logException(e);
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
    private void updateLabel(EditText textInputEditText){
        String myFormat = "dd-MMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        textInputEditText.setText(sdf.format(myCalendar.getTime()));
    }
}
