package com.mikalh.purchaseorderonline.Model;

import java.util.ArrayList;

/**
 * Created by mika.frentzen on 05/03/2018.
 */

public class Cart extends Item {
    private int quantitas_banyakBarang;
    private String AlamatPengiriman, TanggalDibutuhkan;

    public Cart(String nama_barang, String userId, String unit, String namaPerusahaan, String harga_barang, String imageItemUrl, String notificationId, String kategori
            , int quantitas_banyakBarang, String alamatPengiriman, String tanggalDibutuhkan) {
        super(nama_barang, userId, unit, namaPerusahaan, harga_barang, imageItemUrl, notificationId, kategori);
        this.quantitas_banyakBarang = quantitas_banyakBarang;
        AlamatPengiriman = alamatPengiriman;
        TanggalDibutuhkan = tanggalDibutuhkan;
    }

    public Cart() {
    }

    public String getAlamatPengiriman() {
        return AlamatPengiriman;
    }

    public void setAlamatPengiriman(String alamatPengiriman) {
        AlamatPengiriman = alamatPengiriman;
    }

    public String getTanggalDibutuhkan() {
        return TanggalDibutuhkan;
    }

    public void setTanggalDibutuhkan(String tanggalDibutuhkan) {
        TanggalDibutuhkan = tanggalDibutuhkan;
    }

    public int getQuantitas_banyakBarang() {
        return quantitas_banyakBarang;
    }

    public void setQuantitas_banyakBarang(int quantitas_banyakBarang) {
        this.quantitas_banyakBarang = quantitas_banyakBarang;
    }
}
