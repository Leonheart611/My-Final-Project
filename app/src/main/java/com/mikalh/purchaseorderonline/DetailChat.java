package com.mikalh.purchaseorderonline;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Adapter.ChatAdapter;
import com.mikalh.purchaseorderonline.Model.Chat;
import com.mikalh.purchaseorderonline.Model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailChat extends AppCompatActivity implements ChatAdapter.OnChatListenerListener {
    public static String IDSeller= "ID";
    RecyclerView reyclerview_message;
    EditText edittext_chatbox;
    Button button_chatbox;
    String senderId,RoomId;
    FirebaseUser user;
    FirebaseAuth auth;
    Query query;
    FirebaseFirestore firestore;
    ChatAdapter adapter;
    ArrayList<String> userList;
    final Date calendar = Calendar.getInstance().getTime();
    Chat chat;
    String imageUrlSeller,imageUrlBuyer;
    String sellerID,buyyerID;
    String nama;
    CustomDialog customDialog;
    String BlockId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chat);
        reyclerview_message = findViewById(R.id.reyclerview_message_list);
        firestore = FirebaseFirestore.getInstance();
        RoomId = getIntent().getExtras().getString(detailItem.ROOMID);
        customDialog = new CustomDialog(this);
        firestore.collection("RoomChat").document(RoomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot snapshot = task.getResult();
                    buyyerID = snapshot.get("idPembeli").toString();
                    String itemId = snapshot.get("itemID").toString();
                    firestore.collection("Items").document(itemId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot snapshot1 = task.getResult();
                                if (snapshot1.exists()) {
                                    try {
                                        nama = snapshot1.get("namePerusahaan").toString();
                                        setTitle(nama);
                                    } catch (Exception e) {
                                        nama = snapshot1.get("namaPerusahaan").toString();
                                        setTitle(nama);
                                    }
                                    sellerID = snapshot1.get("userId").toString();
                                    firestore.collection("Users").document(sellerID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot snapshot2 = task.getResult();
                                            if (snapshot2.exists()) {
                                                imageUrlSeller = snapshot2.get("url_pictLogo").toString();
                                            }else {
                                                Toast.makeText(DetailChat.this,"Data Untuk PIC URL kosong",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }else {
                                    Toast.makeText(DetailChat.this,"Data Untuk Name Perusahaan Kosong",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        senderId = getIntent().getExtras().getString(detailItem.SENDER_ID);
        query = firestore.collection("RoomChat").document(RoomId).collection("ChatList").orderBy("date").orderBy("time").limit(35);
        firestore.collection("Users").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                imageUrlBuyer = user.getUrl_pictLogo();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Crashlytics.logException(e);
            }
        });
        adapter = new ChatAdapter(query,this,user){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
                reyclerview_message.smoothScrollToPosition(adapter.getItemCount());
            }
            @Override
            public void onBindViewHolder(ChatHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }
            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Crashlytics.logException(e);
            }
        };

        reyclerview_message.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        llm.setSmoothScrollbarEnabled(true);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        reyclerview_message.setLayoutManager(llm);


        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        button_chatbox = findViewById(R.id.button_chatbox_send);
        button_chatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ChatValue = edittext_chatbox.getText().toString();
                final Date calendar = Calendar.getInstance().getTime();
                final String date = calendar.toString();
                final String time = formatTime(date);
                final String tanggal = formatDate(date);
                if (!ChatValue.isEmpty()) {
                    edittext_chatbox.setText("");
                    chat = new Chat(user.getUid(),user.getDisplayName(),ChatValue,tanggal,time,imageUrlBuyer);
                    final Map<String,Object> lastChat = new HashMap<>();
                    lastChat.put("message",chat.getMessage());
                    lastChat.put("namePerusahaan",nama);
                    lastChat.put("namaPembeli",user.getDisplayName());
                    lastChat.put("time",chat.getTime());
                    lastChat.put("imageSeller",imageUrlSeller);
                    lastChat.put("imageBuyer",imageUrlBuyer);
                    //add to Conversasion session
                    firestore.collection("RoomChat").document(RoomId).collection("ChatList").add(chat).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                firestore.collection("RoomChat").document(RoomId).update(lastChat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

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

    @Override
    protected void onStart() {
        super.onStart();
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onChatSelected(DocumentSnapshot chat) {

    }
    public static String formatTime(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date newDate = format.parse(date);

            format = new SimpleDateFormat("HH:mm:ss");
            return new String(format.format(newDate));
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return null;
    }

    public static String formatDate(String date) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date newDate = format.parse(date);

            format = new SimpleDateFormat("dd MMM yyyy");
            return new String(format.format(newDate));
        } catch (Exception e) {
            Crashlytics.logException(e);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.catalog_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }
    void popUpBlock(){
        final Dialog dialog = new Dialog(this);
        /* dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);*/
        dialog.setContentView(R.layout.block_user_submit);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        final EditText reason_block = dialog.findViewById(R.id.reason_block);
        Button submit_block = dialog.findViewById(R.id.submit_block);
        submit_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reason_block.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Reason harus di isi",Toast.LENGTH_LONG).show();
                }else {
                    dialog.dismiss();
                    customDialog.show();
                    if (sellerID.equals(user.getUid())){
                        BlockId = buyyerID;
                        firestore.collection("Users").document(BlockId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    nama = documentSnapshot.get("nama_perusahaan").toString();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Crashlytics.logException(e);
                            }
                        });
                    }else {
                        BlockId = sellerID;
                    }
                    String reason = reason_block.getText().toString();
                    Map<String,Object> dataBlock = new HashMap<>();
                    dataBlock.put("BlockedID",BlockId);
                    dataBlock.put("RequestBy",user.getUid());
                    dataBlock.put("NamaPerusahaan",nama);
                    dataBlock.put("Alasan",reason);
                    dataBlock.put("Done",false);
                    firestore.collection("BlockRequest").document().set(dataBlock).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                customDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"Data Berhasil dikirim,dan sedang dalam pengecekan",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            customDialog.dismiss();
                            Crashlytics.logException(e);
                        }
                    });
                }
            }
        });
    dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.moreItem_chat:
                Intent i = new Intent(getApplicationContext(),SearchResult.class);
                i.putExtra(IDSeller,sellerID);
                startActivity(i);
                return true;
            case R.id.reportUser:
                popUpBlock();
                return true;
            default:
                return false;
        }
    }

}
