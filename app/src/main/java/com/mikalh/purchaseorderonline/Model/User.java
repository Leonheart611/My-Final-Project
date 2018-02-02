package com.mikalh.purchaseorderonline.Model;

/**
 * Created by mika.frentzen on 02/02/2018.
 */

public class User {
    private String nama, tanggal_lahir, no_hp, email, alamat_perusahaan, nama_perusahaan,userID;

    public User(String nama, String tanggal_lahir, String no_hp, String email, String alamat_perusahaan, String nama_perusahaan, String userID) {
        this.nama = nama;
        this.tanggal_lahir = tanggal_lahir;
        this.no_hp = no_hp;
        this.email = email;
        this.alamat_perusahaan = alamat_perusahaan;
        this.nama_perusahaan = nama_perusahaan;
        this.userID = userID;
    }

    public User() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat_perusahaan() {
        return alamat_perusahaan;
    }

    public void setAlamat_perusahaan(String alamat_perusahaan) {
        this.alamat_perusahaan = alamat_perusahaan;
    }

    public String getNama_perusahaan() {
        return nama_perusahaan;
    }

    public void setNama_perusahaan(String nama_perusahaan) {
        this.nama_perusahaan = nama_perusahaan;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
