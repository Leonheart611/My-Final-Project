package com.mikalh.purchaseorderonline.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikalh.purchaseorderonline.ChatList;
import com.mikalh.purchaseorderonline.Model.Chat;
import com.mikalh.purchaseorderonline.R;

import java.lang.reflect.Array;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {
    List<Chat> chats;
    private LayoutInflater myInflater;
    public ChatListAdapter(Context context,List<Chat> chats){
        myInflater = LayoutInflater.from(context);
        this.chats = chats;
    }

    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ChatListHolder{
        TextView name_chatList,lastMessage_chatList,timeStamp_chatList;
        ImageView image_chatList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatListHolder holder;
        if (convertView == null){
            convertView = myInflater.inflate(R.layout.chat_list,null);
            holder = new ChatListHolder();
            holder.name_chatList = convertView.findViewById(R.id.name_chatList);
            holder.lastMessage_chatList = convertView.findViewById(R.id.lastMessage_chatList);
            holder.timeStamp_chatList = convertView.findViewById(R.id.timeStamp_chatList);
            convertView.setTag(holder);
        }else {
            holder = (ChatListHolder) convertView.getTag();
        }
        Chat chat = chats.get(position);
        holder.name_chatList.setText(chat.getTimeStamp());


        return convertView;
    }

}
