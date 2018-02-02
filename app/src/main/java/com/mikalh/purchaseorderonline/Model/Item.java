package com.mikalh.purchaseorderonline.Model;

/**
 * Created by mika.frentzen on 02/02/2018.
 */

public class Item {
    private String nama_barang, jenis_barang, diskripsi_barang, quantitas, userId;
    private int harga_barang, banyak_stock;

    public Item(String nama_barang, String jenis_barang, String diskripsi_barang, String quantitas, String userId, int harga_barang, int banyak_stock) {
        this.nama_barang = nama_barang;
        this.jenis_barang = jenis_barang;
        this.diskripsi_barang = diskripsi_barang;
        this.quantitas = quantitas;
        this.userId = userId;
        this.harga_barang = harga_barang;
        this.banyak_stock = banyak_stock;
    }

    public Item() {
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getJenis_barang() {
        return jenis_barang;
    }

    public void setJenis_barang(String jenis_barang) {
        this.jenis_barang = jenis_barang;
    }

    public String getDiskripsi_barang() {
        return diskripsi_barang;
    }

    public void setDiskripsi_barang(String diskripsi_barang) {
        this.diskripsi_barang = diskripsi_barang;
    }

    public String getQuantitas() {
        return quantitas;
    }

    public void setQuantitas(String quantitas) {
        this.quantitas = quantitas;
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

    public int getBanyak_stock() {
        return banyak_stock;
    }

    public void setBanyak_stock(int banyak_stock) {
        this.banyak_stock = banyak_stock;
    }
}
