package com.mikalh.purchaseorderonline.Model;


import java.util.ArrayList;

/**
 * Created by mika.frentzen on 02/02/2018.
 */

public class Item  {
    private String nama_barang, userId, unit, deskripsi_barang;
    private int harga_barang;
    private ArrayList<String> imageItemUrl;

    public Item(String nama_barang, String userId, String unit, String deskripsi_barang, int harga_barang, ArrayList<String> imageItemUrl) {
        this.nama_barang = nama_barang;
        this.userId = userId;
        this.unit = unit;
        this.deskripsi_barang = deskripsi_barang;
        this.harga_barang = harga_barang;
        this.imageItemUrl = imageItemUrl;
    }

    public String getDeskripsi_barang() {
        return deskripsi_barang;
    }

    public void setDeskripsi_barang(String deskripsi_barang) {
        this.deskripsi_barang = deskripsi_barang;
    }

    public Item() {
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getHarga_barang() {
        return harga_barang;
    }

    public void setHarga_barang(int harga_barang) {
        this.harga_barang = harga_barang;
    }

}
