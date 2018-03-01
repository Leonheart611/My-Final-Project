package com.mikalh.purchaseorderonline.Model;

/**
 * Created by mika.frentzen on 02/02/2018.
 */

public class User {
    private String nama_PIC,
            no_hp, email, alamat_perusahaan,
            nama_perusahaan,userID, no_fax, kota, Provinsi, jabatan_PIC
            , url_pict;

    public User(String nama_PIC, String no_hp, String email, String alamat_perusahaan, String nama_perusahaan, String userID, String no_fax, String kota, String provinsi, String jabatan_PIC) {
        this.nama_PIC = nama_PIC;
        this.no_hp = no_hp;
        this.email = email;
        this.alamat_perusahaan = alamat_perusahaan;
        this.nama_perusahaan = nama_perusahaan;
        this.userID = userID;
        this.no_fax = no_fax;
        this.kota = kota;
        Provinsi = provinsi;
        this.jabatan_PIC = jabatan_PIC;
    }

    public User() {
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
