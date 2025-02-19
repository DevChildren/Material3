package com.develite.pro;

public class Proyek {
    private int id;
    private String nama;
    private double biaya;

    // Constructor
    public Proyek(int id, String nama, double biaya) {
        this.id = id;
        this.nama = nama;
        this.biaya = biaya;
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

    public void setBiaya(double biaya) {
        this.biaya = biaya;
    }
}
