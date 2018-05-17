package com.mikalh.purchaseorderonline.Adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    public ChatAdapter(Query query, OnChatListenerListener listener, FirebaseUser user, String senderID) {
        super(query);
        mListener = listener;
        this.user = user;
        this.senderID = senderID;
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* if (senderID.equals(user.getUid())) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recieve_sent_body, parent, false);
            return new ChatHolder(view);
        } else {*/
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recieve_chat_body, parent, false);
            return new ChatHolder(view);

    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener, user);
    }

    public static class ChatHolder extends RecyclerView.ViewHolder {
        ImageView image_message_profile;
        TextView text_message_name;
        TextView text_message_body,textMessage_you;
        TextView text_message_time_L,text_message_time_R;
        RelativeLayout detailChat_layout;
        public ChatHolder(View itemView) {
            super(itemView);
            text_message_body = itemView.findViewById(R.id.text_message_body);
            text_message_time_L = itemView.findViewById(R.id.text_message_time_L);
            text_message_time_R = itemView.findViewById(R.id.text_message_time_R);
            text_message_name = itemView.findViewById(R.id.text_message_name);
            detailChat_layout = itemView.findViewById(R.id.detailChat_layout);
            textMessage_you = itemView.findViewById(R.id.text_message_you);
            image_message_profile = itemView.findViewById(R.id.image_message_profile);
        }

        public void bind(final DocumentSnapshot snapshot, final OnChatListenerListener listener, FirebaseUser user) {
            Chat chat = snapshot.toObject(Chat.class);
            if (chat != null) {
                if (user.getUid().equals(chat.getSenderId())) {
                    text_message_name.setVisibility(View.GONE);
                    text_message_time_L.setVisibility(View.GONE);
                    image_message_profile.setVisibility(View.GONE);;
                    text_message_body.setText(chat.getMessage());
                    text_message_time_R.setText(chat.getTime());
                    detailChat_layout.setGravity(Gravity.END);
                    text_message_body.setGravity(Gravity.END);
                    text_message_time_R.setGravity(Gravity.END);


                } else {
                    textMessage_you.setVisibility(View.GONE);
                    text_message_time_R.setVisibility(View.GONE);
                    text_message_time_L.setText(chat.getTime());
                    text_message_body.setText(chat.getMessage());
                    text_message_name.setText(chat.getSender_name());
                }

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onChatSelected(snapshot);
                        }
                    }
                });
            }
            else {
                Log.e("Error","Gak ada isi chat");
            }
        }

        public String formatDate(String date) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date newDate = format.parse(date);

                format = new SimpleDateFormat("HH:mm");
                return new String(format.format(newDate));
            } catch (Exception e) {
                Crashlytics.logException(e);
            }
            return null;
        }


    }
}

