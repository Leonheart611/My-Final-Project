package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Model.Chat;
import com.mikalh.purchaseorderonline.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ChatAdapter extends FirestoreAdapter<ChatAdapter.ChatHolder> {
    public interface OnChatListenerListener {

        void onChatSelected(DocumentSnapshot chat);

    }
    private OnChatListenerListener mListener;
    private FirebaseUser user;
    private String senderID;

    public ChatAdapter(Query query, OnChatListenerListener listener,FirebaseUser user,String senderID) {
        super(query);
        mListener = listener;
        this.user = user;
        this.senderID = senderID;
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (senderID.equals(user.getUid())){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recieve_sent_body,parent,false);
            return new ChatHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recieve_chat_body,parent,false);
            return new ChatHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        holder.bind(getSnapshot(position),mListener,user);
    }

    public static class ChatHolder extends RecyclerView.ViewHolder{
        ImageView image_message_profile;
        TextView text_message_name;
        TextView text_message_body;
        TextView text_message_time;

        public ChatHolder(View itemView) {
            super(itemView);
            text_message_body = itemView.findViewById(R.id.text_message_body);
            text_message_time = itemView.findViewById(R.id.text_message_time);
            text_message_name = itemView.findViewById(R.id.text_message_name);
        }
        public void bind(final DocumentSnapshot snapshot, final OnChatListenerListener listener,FirebaseUser user){
            Chat chat = snapshot.toObject(Chat.class);
            if (chat != null){
                String time = formatDate(chat.getTimeStamp());
                if (user.getUid().equals(chat.getSender_UID())){
                    text_message_body.setText(chat.getMessage());
                    text_message_time.setText(time);
                }else {
                    text_message_time.setText(time);
                    text_message_body.setText(chat.getMessage());
                    text_message_name.setText(chat.getReciever_name());
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null){
                            listener.onChatSelected(snapshot);
                        }
                    }
                });
            }
        }
        public String formatDate(String date) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date newDate = format.parse(date);

                format = new SimpleDateFormat("HH:mm");
                return new String(format.format(newDate));
            }catch (Exception e){
                Crashlytics.logException(e);
            }
            return null;
        }


    }
}
