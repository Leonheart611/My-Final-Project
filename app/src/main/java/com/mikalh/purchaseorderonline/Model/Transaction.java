package com.mikalh.purchaseorderonline.Model;

/**
 * Created by mika.frentzen on 19/03/2018.
 */

public class Transaction extends Cart{
    private String pengirim_id,penerima_id,status,tanggal;
    private String[] item_id;

    public Transaction(String pengirim_id, String penerima_id, String status, String tanggal, String[] item_id) {
        this.pengirim_id = pengirim_id;
        this.penerima_id = penerima_id;
        this.status = status;
        this.tanggal = tanggal;
        this.item_id = item_id;
    }

    public String[] getItem_id() {
        return item_id;
    }

    public void setItem_id(String[] item_id) {
        this.item_id = item_id;
    }

    public String getPengirim_id() {
        return pengirim_id;
    }

    public void setPengirim_id(String pengirim_id) {
        this.pengirim_id = pengirim_id;
    }

    public String getPenerima_id() {
        return penerima_id;
    }

    public void setPenerima_id(String penerima_id) {
        this.penerima_id = penerima_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
