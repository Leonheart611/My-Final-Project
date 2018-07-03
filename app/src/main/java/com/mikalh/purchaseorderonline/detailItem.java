package com.mikalh.purchaseorderonline;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mikalh.purchaseorderonline.FCM.InstanceIdService;
import com.mikalh.purchaseorderonline.Model.Cart;
import com.mikalh.purchaseorderonline.Model.Chat;
import com.mikalh.purchaseorderonline.Model.Item;
import com.mikalh.purchaseorderonline.Model.User;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

public class detailItem extends AppCompatActivity implements View.OnClickListener {
    private String KEY_ITEM_ID;
    public static String SENDER_ID = "senderId", RECIEVER_ID = "recieverId",KEY_ID = "ItemID",ROOMID = "RoomId",NAMA="nama";
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
    Button chatButton;
    String IDPenjual;
    Boolean block;
    int GrandTotal = 0;
    public static final String USER_ID = "userID";
    public static final String NAMAPERUSAHAAN = "perusahaan";
    User userModel;
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
        namaPerusahaan_detail.setOnClickListener(this);
        firestore = FirebaseFirestore.getInstance();
        KEY_ITEM_ID = getIntent().getExtras().getString(userUI.KEY_ITEM_ID);
        customDialog.show();
        firestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    userModel = snapshot.toObject(User.class);

                    // lanjutan untuk dapat data View
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
                                IDPenjual = item.getUserId();
                                Glide.with(imageBarang_detail.getContext())
                                        .load(item.getImageItemUrl())
                                        .into(imageBarang_detail);
                                customDialog.dismiss();
                                if (item.getUserId() == user.getUid()){
                                    beliButton_do.setVisibility(View.GONE);
                                    chatButton.setVisibility(View.GONE);
                                }
                            }if (task.isSuccessful()){
                                firestore.collection("Users").document(IDPenjual).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot snapshot1 = task.getResult();
                                            block = (Boolean) snapshot1.get("Block");
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
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Error Get Data",e.getMessage());
                        }
                    });
                }
            }
        });


        beliButton_do.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (block){
                    Toast.makeText(getApplicationContext(),"Penjual ini sudah di blokir",Toast.LENGTH_LONG).show();
                }else {
                    popupBuyCart();
                }

            }
        });
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("RoomChat").whereEqualTo("Users."+user.getUid(),true).
                        whereEqualTo("Users."+item.getUserId(),true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                             i.putExtra(NAMA,item.getNamaPerusahaan());
                             startActivity(i);
                         }else {
                             DocumentReference ref = firestore.collection("RoomChat").document();
                             final String myId = ref.getId();
                             HashMap<String,Object> users = new HashMap<>();
                             users.put(user.getUid(),true);
                             users.put(item.getUserId(),true);
                             Map<String,Object> roomChat = new HashMap<>();
                             roomChat.put("Users",users);
                             roomChat.put("itemID",KEY_ITEM_ID);
                             roomChat.put("senderName",user.getDisplayName());
                             roomChat.put("sellerName",item.getNamaPerusahaan());
                             roomChat.put("idPenjual",item.getUserId());
                             roomChat.put("idPembeli",user.getUid());
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
        /*
*/
        saveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                customDialog.show();
                int pcs = Integer.parseInt(banyakPCS_popCart.getText().toString());
                BigDecimal harga = new BigDecimal(item.getHarga_barang().replace(".",""));
                final BigDecimal totalHarga = totalCost(pcs,harga);
                final Cart cart = new Cart(item.getNama_barang(),item.getUserId(),item.getUnit()
                        ,item.getNamaPerusahaan(),item.getHarga_barang(),item.getImageItemUrl(),item.getNotificationId(),item.getKategori(),pcs,totalHarga.intValue());
                // add database firestore
                DocumentReference ref = firestore.collection("Cart").document();
                final String myId = ref.getId();
                // teknik cara pengambilan
                firestore.collection("Cart").
                        whereEqualTo("UserList."+user.getUid(),true).whereEqualTo("UserList."+item.getUserId(),true)
                        .whereEqualTo("MakePO",false).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        final QuerySnapshot snapshots = task.getResult();
                        if (snapshots.getDocuments().size() !=0){ // data jika sudah ada dan di tinggal diupdate
                            final String curr = snapshots.getDocuments().get(0).getId();
                            firestore.collection("Cart").document(curr).collection("ItemList").document().set(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        firestore.collection("Cart").document(curr).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshot = task.getResult();
                                                    int i = Integer.parseInt(documentSnapshot.get("GrandTotal").toString());
                                                    GrandTotal = i;
                                                    // update data dan grand total dan semuanya  di update lah
                                                    final Query query = firestore.collection("Cart").document(curr).collection("ItemList");
                                                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()){
                                                                QuerySnapshot snapshots1 = task.getResult();
                                                                int banyakData = snapshots1.size();
                                                                Map<String,Object> dataUPdate = new HashMap<>();
                                                                dataUPdate.put("BanyakData",banyakData);
                                                                int totalHargaBarang = Integer.parseInt(totalHarga.toString());
                                                                int TotalSemua = GrandTotal + totalHargaBarang;
                                                                dataUPdate.put("GrandTotal",TotalSemua);
                                                                firestore.collection("Cart").document(curr).update(dataUPdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            Toast.makeText(detailItem.this,"berhasil memasukan data cart",Toast.LENGTH_LONG).show();
                                                                            customDialog.dismiss();
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
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Crashlytics.logException(e);
                                                            customDialog.dismiss();
                                                        }
                                                    });
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
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Crashlytics.logException(e);

                                            }
                                        });

                        }else {
                            // kalau dokumennya tidak ada dan baru di buat
                            firestore.collection("Users").document(item.getUserId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot snapshot = task.getResult();
                                        User userCompany = snapshot.toObject(User.class);
                                        Map<String,Object> cartStringMap = new HashMap<>();
                                        cartStringMap.put(item.getUserId(),true);
                                        cartStringMap.put(user.getUid(),true);
                                        final Map<String,Object> arrayUserID = new HashMap<>();
                                        arrayUserID.put("UserList",cartStringMap);
                                        arrayUserID.put("namaPerusahaan",item.getNamaPerusahaan());
                                        arrayUserID.put("GrandTotal",Integer.parseInt(totalHarga.toString()));
                                        arrayUserID.put("NamaPIC",userCompany.getNama_PIC());
                                        arrayUserID.put("Alamat",userCompany.getAlamat_perusahaan());
                                        arrayUserID.put("Propinsi",userCompany.getProvinsi());
                                        arrayUserID.put("Kota",userCompany.getKota());
                                        arrayUserID.put("Telp",userCompany.getNomorTelphone());
                                        arrayUserID.put("Fax",userCompany.getNo_fax());
                                        arrayUserID.put("IDPenjual",item.getUserId());
                                        arrayUserID.put("IDPembeli",user.getUid());
                                        arrayUserID.put("namaPerusahaanPembeli",userModel.getNama_perusahaan());
                                        arrayUserID.put("StatusPO","Belum Di buat PO");
                                        arrayUserID.put("MakePO",false);
                                        firestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()){
                                                    User pembeli = task.getResult().toObject(User.class);
                                                    if (FirebaseInstanceId.getInstance().getToken().isEmpty()){
                                                        new InstanceIdService().onTokenRefresh();
                                                    }
                                                    arrayUserID.put("PembeliNotif", FirebaseInstanceId.getInstance().getToken());
                                                    arrayUserID.put("PenjualNotif",item.getNotificationId());
                                                    firestore.collection("Cart").document(myId).set(arrayUserID).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Crashlytics.logException(e);
                                                            Toast.makeText(detailItem.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                                        }
                                                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                firestore.collection("Cart").document(myId).collection("ItemList").document().set(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()){
                                                                            Query query = firestore.collection("Cart").document(myId).collection("ItemList");
                                                                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        QuerySnapshot snapshots1 = task.getResult();
                                                                                        Map<String,Object> dataUPdate = new HashMap<>();
                                                                                        int banyakData = snapshots1.size();
                                                                                        dataUPdate.put("BanyakData",banyakData);
                                                                                        firestore.collection("Cart").document(myId).update(dataUPdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    Toast.makeText(detailItem.this,"Data berhasil di Simpan",Toast.LENGTH_LONG).show();
                                                                                                    dialog.dismiss();
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
                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                @Override
                                                                                public void onFailure(@NonNull Exception e) {
                                                                                    Crashlytics.logException(e);
                                                                                }
                                                                            });
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
                                        });
                                                }
                                            }
                                        });
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
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Crashlytics.logException(e);
                    }
                });
            }
        });
        dialog.show();
    }

    public BigDecimal totalCost(int itemQuantity, BigDecimal itemPrice){
        BigDecimal itemCost,totalCost = null;
        itemCost = itemPrice.multiply(new BigDecimal(itemQuantity));
        totalCost = itemCost;
        return totalCost;
    }

    @Override
    public void onClick(View view) {
        if (view == namaPerusahaan_detail){
            Intent i = new Intent(detailItem.this,Profile.class);
            i.putExtra(USER_ID,item.getUserId());
            startActivity(i);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
