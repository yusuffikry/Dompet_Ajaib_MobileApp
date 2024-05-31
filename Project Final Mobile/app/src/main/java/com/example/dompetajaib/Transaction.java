package com.example.dompetajaib;

public class Transaction {
    private int id;
    private String jenis;
    private int jumlah;
    private String catatan;
    private String kategori;

    public Transaction() {

    }

    public Transaction(int id, String jenis, int jumlah, String catatan, String kategori) {
        this.id = id;
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.catatan = catatan;
        this.kategori = kategori;
    }

    public int getId() {
        return id;
    }

    public String getJenis() {
        return jenis;
    }

    public int getJumlah() {
        return jumlah;
    }

    public String getCatatan() {
        return catatan;
    }

    public String getKategori() {
        return kategori;
    }
}
