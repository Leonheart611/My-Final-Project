package com.mikalh.purchaseorderonline.Model;

import android.net.Uri;

public class LastChat {
    String message,name,time,image;

    public LastChat(String message, String name, String time, String image) {
        this.message = message;
        this.name = name;
        this.time = time;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LastChat() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LastChat(String message, String name, String time) {
        this.message = message;
        this.name = name;
        this.time = time;
    }
}
