package com.mikalh.purchaseorderonline.Model;

import java.util.ArrayList;

/**
 * Created by mikal on 3/4/2018.
 */

public class Cart extends Item {
    private int quantitas;

    public Cart(String nama_barang, String userId, String unit,
                String deskripsi_barang, int harga_barang, ArrayList<String> imageItemUrl, int quantitas) {
        super(nama_barang, userId, unit, deskripsi_barang, harga_barang, imageItemUrl);
        this.quantitas = quantitas;
    }
}
