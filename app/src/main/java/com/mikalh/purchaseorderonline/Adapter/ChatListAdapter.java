package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        public ChatListHolder(View itemView) {
            super(itemView);
            name_chatList = itemView.findViewById(R.id.name_chatList);
            lastMessage_chatList = itemView.findViewById(R.id.lastMessage_chatList);
            timeStampt_chatList = itemView.findViewById(R.id.timeStamp_chatList);
        }
        public void bind(final DocumentSnapshot snapshot, final OnChatListListenerListener listener, FirebaseUser user){
            Chat chat = snapshot.toObject(Chat.class);
            if (user.getUid().equals(chat.getSender_UID())){
                name_chatList.setText("You");
            }else {
                name_chatList.setText(chat.getSender_name());
            }
            String time = formatDate(chat.getTimeStamp());
            lastMessage_chatList.setText(chat.getMessage());
            timeStampt_chatList.setText(time);
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
