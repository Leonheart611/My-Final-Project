package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Locale;


public class ChatAdapter extends FirestoreAdapter<ChatAdapter.ChatHolder> {
    public interface OnChatListenerListener {

        void onChatSelected(DocumentSnapshot chat);

    }

    private OnChatListenerListener mListener;
    private FirebaseUser user;
    private Date date;

    public ChatAdapter(Query query, OnChatListenerListener listener, FirebaseUser user) {
        super(query);
        mListener = listener;
        this.user = user;
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 1){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recieve_sent_body, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recieve_chat_body, parent, false);
        }

            return new ChatHolder(view);


    }

    @Override
    public int getItemViewType(int position) {
        Chat chat = getSnapshot(position).toObject(Chat.class);
        int posisi =0;
        if (user.getUid().equals(chat.getSenderId())){
            posisi =1;
        }else {
            posisi=2;
        }
        return posisi;
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener,user);
    }

    public static class ChatHolder extends RecyclerView.ViewHolder {
        ImageView image_message_profile;
        TextView text_message_name;
        TextView text_message_body;
        TextView text_message_time;
        public ChatHolder(View itemView) {
            super(itemView);
            text_message_body = itemView.findViewById(R.id.text_message_body);
            text_message_time = itemView.findViewById(R.id.text_message_time);
            text_message_name = itemView.findViewById(R.id.text_message_name);
            image_message_profile = itemView.findViewById(R.id.image_message_profile);

        }

        public void bind(final DocumentSnapshot snapshot, final OnChatListenerListener listener, FirebaseUser user) {
            Chat chat = snapshot.toObject(Chat.class);
            // setting up chat properties
            String time = formatTime(chat.getTime());
            text_message_body.setText(chat.getMessage());
            text_message_time.setText(time);
            text_message_name.setText(chat.getSender_name());
            itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onChatSelected(snapshot);
                        }
                    }
                });
        }

        public String formatTime(String date) {
            try {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
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

