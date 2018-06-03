package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Model.Chat;
import com.mikalh.purchaseorderonline.Model.LastChat;
import com.mikalh.purchaseorderonline.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends FirestoreAdapter<ChatListAdapter.ChatListHolder>  {
    public interface OnChatListListenerListener {

        void onChatListSelected(DocumentSnapshot chat);

    }
    private OnChatListListenerListener mListener;
    private FirebaseUser user;
    private String senderID;
    public ChatListAdapter(Query query, OnChatListListenerListener listener,FirebaseUser user) {
        super(query);
        mListener = listener;
        this.user = user;
    }

    @Override
    public ChatListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list,parent,false);
        return new ChatListHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatListHolder holder, int position) {
        holder.bind(getSnapshot(position),mListener,user);
    }


    public static class ChatListHolder extends RecyclerView.ViewHolder{
        TextView name_chatList,lastMessage_chatList,timeStampt_chatList;
        CircleImageView image_chatList;
        public ChatListHolder(View itemView) {
            super(itemView);
            name_chatList = itemView.findViewById(R.id.name_chatList);
            lastMessage_chatList = itemView.findViewById(R.id.lastMessage_chatList);
            timeStampt_chatList = itemView.findViewById(R.id.timeStamp_chatList);
            image_chatList = itemView.findViewById(R.id.image_chatList);
        }
        public void bind(final DocumentSnapshot snapshot, final OnChatListListenerListener listener, FirebaseUser user){
            LastChat chat = snapshot.toObject(LastChat.class);
            String time = formatTime(chat.getTime());
            name_chatList.setText(chat.getName());
            lastMessage_chatList.setText(chat.getMessage());
            timeStampt_chatList.setText(time);
            if (chat.getImage()!=null) {
                Glide.with(image_chatList.getContext())
                        .load(chat.getImage())
                        .into(image_chatList);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onChatListSelected(snapshot);
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

