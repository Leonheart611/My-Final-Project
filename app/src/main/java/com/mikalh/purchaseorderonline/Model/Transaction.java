package com.mikalh.purchaseorderonline.Model;

import java.math.BigDecimal;

/**
 * Created by mika.frentzen on 19/03/2018.
 */

public class Transaction extends Cart{
    private String pengirim_id,penerima_id,status,tanggal;
    private String notificationIdPengirim;
    private BigDecimal totalHarga;

    public Transaction(String nama_barang, String userId, String unit, String namaPerusahaan, String harga_barang, String imageItemUrl, String notificationId, int quantitas_banyakBarang, String pengirim_id, String penerima_id, String status, String tanggal, String notificationIdPengirim, BigDecimal totalHarga) {
        super(nama_barang, userId, unit, namaPerusahaan, harga_barang, imageItemUrl, notificationId, quantitas_banyakBarang);
        this.pengirim_id = pengirim_id;
        this.penerima_id = penerima_id;
        this.status = status;
        this.tanggal = tanggal;
        this.notificationIdPengirim = notificationIdPengirim;
        this.totalHarga = totalHarga;
    }

    public BigDecimal getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(BigDecimal totalHarga) {
        this.totalHarga = totalHarga;
    }

    public Transaction() {

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
