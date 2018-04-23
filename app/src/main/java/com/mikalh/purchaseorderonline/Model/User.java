package com.mikalh.purchaseorderonline.Model;

/**
 * Created by mika.frentzen on 02/02/2018.
 */

public class User extends Company{
    private String nama_PIC,
            email,userID,jabatan_PIC,username,notificationId;

    public User(String alamat_perusahaan, String nama_perusahaan, String nomorTelphone, String no_fax, String kota, String provinsi, String url_pictLogo, String nama_PIC, String email, String userID, String jabatan_PIC, String username, String notificationId) {
        super(alamat_perusahaan, nama_perusahaan, nomorTelphone, no_fax, kota, provinsi, url_pictLogo);
        this.nama_PIC = nama_PIC;
        this.email = email;
        this.userID = userID;
        this.jabatan_PIC = jabatan_PIC;
        this.username = username;
        this.notificationId = notificationId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
