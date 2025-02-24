package com.develite.pro;

public class Proyek {
    private int id;
    private String nama;
    private double biaya;
    private String lokasi;
    private String tanggal;

    // Constructor
    public Proyek(int id, String nama, double biaya, String lokasi, String tanggal) {
        this.id = id;
        this.nama = nama;
        this.biaya = biaya;
        this.lokasi = lokasi;
        this.tanggal = tanggal;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getBiaya() {
        return biaya;
    }
    
    public String getLokasi() {
     return lokasi;
    }

    public String getTanggal() {
     return tanggal;
    }
    
    public void setLokasi(String lokasi) {
      this.lokasi = lokasi;
    }

    public void setTanggal(String tanggal) {
      this.tanggal = tanggal;
    }

    public void setBiaya(double biaya) {
        this.biaya = biaya;
    }
}
