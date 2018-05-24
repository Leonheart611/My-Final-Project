package com.mikalh.purchaseorderonline.Model;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by mika.frentzen on 05/03/2018.
 */

public class Cart extends Item {
    private int quantitas_banyakBarang,TotalHargaBarang;

    public Cart(String nama_barang, String userId, String unit, String namaPerusahaan, String harga_barang, String imageItemUrl, String notificationId, String kategori, int quantitas_banyakBarang, int totalHargaBarang) {
        super(nama_barang, userId, unit, namaPerusahaan, harga_barang, imageItemUrl, notificationId, kategori);
        this.quantitas_banyakBarang = quantitas_banyakBarang;
        TotalHargaBarang = totalHargaBarang;
    }

    public int getTotalHargaBarang() {
        return TotalHargaBarang;
    }

    public void setTotalHargaBarang(int totalHargaBarang) {
        TotalHargaBarang = totalHargaBarang;
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
