package com.develite.pro;
import java.lang.Math;

public class ConcreteCalculator {

    // === Kalkulasi Beton ===
    public static double hitungBeton(double panjang, double lebar, double tinggi) {
        return panjang * lebar * tinggi; // Volume beton dalam m³
    }

    public static double hitungBetonKubus(double sisi) {
        return Math.pow(sisi, 3);
    }

    public static double hitungBetonLingkar(double radius, double tinggi) {
        return Math.PI * Math.pow(radius, 2) * tinggi;
    }

    public static double hitungBetonKerucut(double radius, double tinggi) {
        return (1.0 / 3.0) * Math.PI * Math.pow(radius, 2) * tinggi;
    }

    public static double hitungBetonSegitiga(double alas, double tinggiSegitiga, double panjang) {
        return (0.5 * alas * tinggiSegitiga) * panjang;
    }

    // === Kalkulasi Bata ===
    public static double hitungBata(double panjangDinding, double tinggiDinding, double luasBata) {
        return (panjangDinding * tinggiDinding) / luasBata; // Jumlah bata
    }

    // === Kalkulasi Pasir ===
    public static double hitungPasir(double volumeBeton, double rasioPasir) {
        return volumeBeton * rasioPasir; // Estimasi jumlah pasir dalam m³
    }

    // === Kalkulasi Cat ===
    public static double hitungCat(double luasDinding, double dayaSebarPerLiter) {
        return luasDinding / dayaSebarPerLiter; // Jumlah liter cat yang dibutuhkan
    }

    // === Kalkulasi Kaso ===
    public static double hitungKaso(double panjangAtap, double jumlahKasoPerMeter) {
        return panjangAtap * jumlahKasoPerMeter;
    }

    // === Kalkulasi Besi ===
    public static double hitungBesi(double panjangBesi, double jumlahBatang) {
        return panjangBesi * jumlahBatang;
    }

    // === Kalkulasi Plafond ===
    public static double hitungPlafond(double luasRuang, double luasPlafondPerLembar) {
        return luasRuang / luasPlafondPerLembar;
    }

    // === Kalkulasi Keramik ===
    public static double hitungKeramik(double luasLantai, double luasKeramikPerPcs) {
        return luasLantai / luasKeramikPerPcs;
    }
}
