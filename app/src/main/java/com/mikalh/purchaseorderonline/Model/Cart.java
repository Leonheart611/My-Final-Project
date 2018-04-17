package com.mikalh.purchaseorderonline.Model;

import java.util.ArrayList;

/**
 * Created by mika.frentzen on 05/03/2018.
 */

public class Cart extends Item {
    private int quantitas_banyakBarang;
    private boolean ToTransaction;

    public Cart(String nama_barang, String userId, String unit, String harga_barang, String imageItemUrl, int quantitas_banyakBarang, boolean toTransaction) {
        super(nama_barang, userId, unit, harga_barang, imageItemUrl);
        this.quantitas_banyakBarang = quantitas_banyakBarang;
        ToTransaction = toTransaction;
    }

    public boolean isToTransaction() {
        return ToTransaction;
    }

    public void setToTransaction(boolean toTransaction) {
        ToTransaction = toTransaction;
    }

    public Cart() {
    }

    public int getQuantitas_banyakBarang() {
        return quantitas_banyakBarang;
    }

    public void setQuantitas_banyakBarang(int quantitas_banyakBarang) {
        this.quantitas_banyakBarang = quantitas_banyakBarang;
    }
}
