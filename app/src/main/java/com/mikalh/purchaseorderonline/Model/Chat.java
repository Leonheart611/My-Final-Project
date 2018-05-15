package com.mikalh.purchaseorderonline.Model;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by mika.frentzen on 01/03/2018.
 */
@IgnoreExtraProperties
public class Chat {
    public String sender_UID,sender_name, message, imageUrl, reciever_UID,reciever_name,timeStamp;


    public Chat(String sender_UID, String sender_name, String message, String imageUrl, String reciever_UID, String reciever_name, String timeStamp) {
        this.sender_UID = sender_UID;
        this.sender_name = sender_name;
        this.message = message;
        this.imageUrl = imageUrl;
        this.reciever_UID = reciever_UID;
        this.reciever_name = reciever_name;
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReciever_name() {
        return reciever_name;
    }

    public void setReciever_name(String reciever_name) {
        this.reciever_name = reciever_name;
    }

    public String getSender_UID() {
        return sender_UID;
    }

    public void setSender_UID(String sender_UID) {
        this.sender_UID = sender_UID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReciever_UID() {
        return reciever_UID;
    }

    public void setReciever_UID(String reciever_UID) {
        this.reciever_UID = reciever_UID;
    }

    public Chat() {
    }
}
