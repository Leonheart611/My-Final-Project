package com.mikalh.purchaseorderonline.Model;

import android.net.Uri;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by mika.frentzen on 01/03/2018.
 */
@IgnoreExtraProperties
public class Chat {
    public String senderId,sender_name, message, date,time,senderImageUrl;

    public Chat(String senderId, String sender_name, String message, String date, String time, String senderImageUrl) {
        this.senderId = senderId;
        this.sender_name = sender_name;
        this.message = message;
        this.date = date;
        this.time = time;
        this.senderImageUrl = senderImageUrl;
    }

    public String getSenderImageUrl() {
        return senderImageUrl;
    }

    public void setSenderImageUrl(String senderImageUrl) {
        this.senderImageUrl = senderImageUrl;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Chat() {
    }
}
