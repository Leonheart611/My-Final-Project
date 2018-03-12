package com.mikalh.purchaseorderonline.Model;

/**
 * Created by mika.frentzen on 02/02/2018.
 */

public class User {
    private String nama_PIC,
            email,userID,jabatan_PIC,alamat_perusahaan,
            nama_perusahaan,nomorTelphone, no_fax, kota, Provinsi
            , url_pictLogo;

    public User(String nama_PIC, String email, String userID, String jabatan_PIC, String alamat_perusahaan
            , String nama_perusahaan, String nomorTelphone, String no_fax, String kota, String provinsi, String url_pictLogo) {
        this.nama_PIC = nama_PIC;
        this.email = email;
        this.userID = userID;
        this.jabatan_PIC = jabatan_PIC;
        this.alamat_perusahaan = alamat_perusahaan;
        this.nama_perusahaan = nama_perusahaan;
        this.nomorTelphone = nomorTelphone;
        this.no_fax = no_fax;
        this.kota = kota;
        Provinsi = provinsi;
        this.url_pictLogo = url_pictLogo;
    }

    public User() {
    }

    public String getNama_PIC() {
        return nama_PIC;
    }

    public void setNama_PIC(String nama_PIC) {
        this.nama_PIC = nama_PIC;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getJabatan_PIC() {
        return jabatan_PIC;
    }

    public void setJabatan_PIC(String jabatan_PIC) {
        this.jabatan_PIC = jabatan_PIC;
    }
}
