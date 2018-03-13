package com.mikalh.purchaseorderonline.Model;

import java.io.Serializable;

/**
 * Created by mika.frentzen on 12/03/2018.
 */

public class Company implements Serializable {
    private String
            alamat_perusahaan,
            nama_perusahaan,nomorTelphone, no_fax, kota, Provinsi
            , url_pictLogo;

    public Company(String alamat_perusahaan, String nama_perusahaan, String nomorTelphone, String no_fax, String kota, String provinsi, String url_pictLogo) {
        this.alamat_perusahaan = alamat_perusahaan;
        this.nama_perusahaan = nama_perusahaan;
        this.nomorTelphone = nomorTelphone;
        this.no_fax = no_fax;
        this.kota = kota;
        Provinsi = provinsi;
        this.url_pictLogo = url_pictLogo;
    }

    public Company() {
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

    public String getNomorTelphone() {
        return nomorTelphone;
    }

    public void setNomorTelphone(String nomorTelphone) {
        this.nomorTelphone = nomorTelphone;
    }

    public String getNo_fax() {
        return no_fax;
    }

    public void setNo_fax(String no_fax) {
        this.no_fax = no_fax;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getProvinsi() {
        return Provinsi;
    }

    public void setProvinsi(String provinsi) {
        Provinsi = provinsi;
    }

    public String getUrl_pictLogo() {
        return url_pictLogo;
    }

    public void setUrl_pictLogo(String url_pictLogo) {
        this.url_pictLogo = url_pictLogo;
    }
}
