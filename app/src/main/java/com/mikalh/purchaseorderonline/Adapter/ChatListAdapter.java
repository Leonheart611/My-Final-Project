package com.mikalh.purchaseorderonline.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class ChatListAdapter extends FirestoreAdapter<ChatListAdapter.ChatListHolder>  {
    public interface OnChatListListenerListener {

        void onChatListSelected(DocumentSnapshot chat);

    }
    private OnChatListListenerListener mListener;

    public ChatListAdapter(Query query, OnChatListListenerListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ChatListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ChatListHolder holder, int position) {

    }


    public static class ChatListHolder extends RecyclerView.ViewHolder{


        public ChatListHolder(View itemView) {
            super(itemView);
        }
    }
}
