package com.mikalh.purchaseorderonline.Model;

public class LastChat {
    String message,name,time;

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
