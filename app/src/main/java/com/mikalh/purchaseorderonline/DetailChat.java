package com.mikalh.purchaseorderonline;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.ServerTimestamp;
import com.mikalh.purchaseorderonline.Adapter.ChatAdapter;
import com.mikalh.purchaseorderonline.Model.Chat;

public class DetailChat extends AppCompatActivity implements ChatAdapter.OnChatListenerListener {
    RecyclerView reyclerview_message;
    EditText edittext_chatbox;
    Button button_chatbox;
    String senderId, recieverId;
    FirebaseUser user;
    FirebaseAuth auth;
    Query query;
    FirebaseFirestore firestore;
    ChatAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_chat);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        senderId = getIntent().getExtras().getString(detailItem.SENDER_ID);
        recieverId = getIntent().getExtras().getString(detailItem.RECIEVER_ID);
        query = firestore.collection("Chats").document(recieverId).collection(senderId);
        adapter = new ChatAdapter(query,this,user,senderId){
            @Override
            protected void onDataChanged() {
                super.onDataChanged();
            }

            @Override
            public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(ChatHolder holder, int position) {
                super.onBindViewHolder(holder, position);
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                super.onError(e);
                Crashlytics.logException(e);
            }

        };
        reyclerview_message = findViewById(R.id.reyclerview_message_list);
        reyclerview_message.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        reyclerview_message.setLayoutManager(llm);
        edittext_chatbox = findViewById(R.id.edittext_chatbox);
        button_chatbox = findViewById(R.id.button_chatbox_send);
        button_chatbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ChatValue = edittext_chatbox.getText().toString();

                if (!ChatValue.isEmpty()) {
                    Chat chat = new Chat(user.getUid(), user.getDisplayName(), ChatValue, "", "", recieverId, "");
                    firestore.collection("Chats").document(recieverId).collection(senderId).add(chat).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(DetailChat.this,"Your Message HasBeen send",Toast.LENGTH_LONG).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Crashlytics.logException(e);
                        }
                    });
                }else {

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter!= null){
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onChatSelected(DocumentSnapshot chat) {

    }
}