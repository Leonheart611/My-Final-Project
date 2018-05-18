package com.mikalh.purchaseorderonline;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.mikalh.purchaseorderonline.Adapter.ChatAdapter;
import com.mikalh.purchaseorderonline.Model.Chat;
import com.mikalh.purchaseorderonline.Model.Item;
import com.mikalh.purchaseorderonline.Model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.services.common.Crash;

public class DetailChat extends AppCompatActivity implements ChatAdapter.OnChatListenerListener {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chat);
        reyclerview_message = findViewById(R.id.reyclerview_message_list);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        senderId = getIntent().getExtras().getString(detailItem.SENDER_ID);
        RoomId = getIntent().getExtras().getString(detailItem.ROOMID);
        query = firestore.collection("RoomChat").document(RoomId).collection("ChatList").orderBy("time").limit(20);


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
                    chat = new Chat(user.getUid(),user.getDisplayName(),ChatValue,tanggal,time);
                    final Map<String,Object> lastChat = new HashMap<>();
                    lastChat.put("message",chat.getMessage());
                    lastChat.put("name",chat.getSender_name());
                    lastChat.put("time",chat.getTime());
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
}
