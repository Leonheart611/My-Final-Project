package com.mikalh.purchaseorderonline.Model;

import android.net.Uri;

public class LastChat {
    String message,name,time,imageSeller,imageBuyer,namePerusahaan,namaPembeli;

    public LastChat(String message, String name, String time, String imageSeller, String imageBuyer, String namePerusahaan, String namaPembeli) {
        this.message = message;
        this.name = name;
        this.time = time;
        this.imageSeller = imageSeller;
        this.imageBuyer = imageBuyer;
        this.namePerusahaan = namePerusahaan;
        this.namaPembeli = namaPembeli;
    }

    public String getImageSeller() {
        return imageSeller;
    }

    public void setImageSeller(String imageSeller) {
        this.imageSeller = imageSeller;
    }

    public String getImageBuyer() {
        return imageBuyer;
    }

    public void setImageBuyer(String imageBuyer) {
        this.imageBuyer = imageBuyer;
    }

    public String getNamePerusahaan() {
        return namePerusahaan;
    }

    public void setNamePerusahaan(String namePerusahaan) {
        this.namePerusahaan = namePerusahaan;
    }

    public String getNamaPembeli() {
        return namaPembeli;
    }

    public void setNamaPembeli(String namaPembeli) {
        this.namaPembeli = namaPembeli;
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


}
