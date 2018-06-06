package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.mikalh.purchaseorderonline.Model.LastChat;
import com.mikalh.purchaseorderonline.Model.User;
import com.mikalh.purchaseorderonline.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListBuyerAdapter extends FirestoreAdapter<ChatListBuyerAdapter.ChatListBuyerHolder>  {

    public interface OnChatListListenerListener {

        void onChatListSelected(DocumentSnapshot chat);

    }
    private ChatListAdapter.OnChatListListenerListener mListener;
    private User user;
    private String senderID;
    public ChatListBuyerAdapter(Query query, ChatListAdapter.OnChatListListenerListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ChatListBuyerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list,parent,false);
        return new ChatListBuyerHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatListBuyerHolder holder, int position) {
        holder.bind(getSnapshot(position),mListener);
    }




    public static class ChatListBuyerHolder extends RecyclerView.ViewHolder{
        TextView name_chatList,lastMessage_chatList,timeStampt_chatList;
        CircleImageView image_chatList;
        public ChatListBuyerHolder(View itemView) {
            super(itemView);
            name_chatList = itemView.findViewById(R.id.name_chatList);
            lastMessage_chatList = itemView.findViewById(R.id.lastMessage_chatList);
            timeStampt_chatList = itemView.findViewById(R.id.timeStamp_chatList);
            image_chatList = itemView.findViewById(R.id.image_chatList);
        }
        public void bind(final DocumentSnapshot snapshot, final ChatListAdapter.OnChatListListenerListener listener){
            LastChat chat = snapshot.toObject(LastChat.class);
            String time = formatTime(chat.getTime());
            lastMessage_chatList.setText(chat.getMessage());
            timeStampt_chatList.setText(time);
            if (chat.getImageBuyer()!=null) {
                Glide.with(image_chatList.getContext())
                        .load(chat.getImageBuyer())
                        .into(image_chatList);
            }
            name_chatList.setText(chat.getNamaPembeli());
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

