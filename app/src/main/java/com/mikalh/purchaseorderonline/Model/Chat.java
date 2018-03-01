package com.mikalh.purchaseorderonline.Model;

/**
 * Created by mika.frentzen on 01/03/2018.
 */

public class Chat {
    String sender_UID, message, timeStamp, imageUrl;

    public Chat(String sender_UID, String message, String timeStamp, String imageUrl) {
        this.sender_UID = sender_UID;
        this.message = message;
        this.timeStamp = timeStamp;
        this.imageUrl = imageUrl;
    }

    public Chat() {
    }
}
