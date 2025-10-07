package com.praktikum.whitebox.util;

import com.praktikum.whitebox.model.Kategori;
import com.praktikum.whitebox.model.Produk;

public class ValidationUtils {
    public static boolean isValidKodeProduk(String kode) {
        if (kode == null || kode.trim().isEmpty()) {
            return false;
        }
        String kodeBersih = kode.trim();
        return kodeBersih.matches("[A-Za-z0-9]{3,10}");
    }

    // Validasi nama (3-100 karakter, boleh huruf, angka, spasi)
    public static boolean isValidNama(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String nameBersih = name.trim();
        return nameBersih.length() >= 3 && nameBersih.length() <= 100;
    }

    // Validasi harga (harga positif)
    public static boolean isValidHarga(double harga) {
        return harga > 0;
    }

    // Validasi stok (non-negatif)
    public static boolean isValidStok(int stok) {
        return stok >= 0;
    }

    // Validasi stok minimum (non-negatif)
    public static boolean isValidStokMinimum(int stokMinimum) {
        return stokMinimum >= 0;
    }

    // Validasi produk lengkap
    public static boolean isValidProduk(Produk produk) {
        if (produk == null) {
            return false;
        }
        return isValidKodeProduk(produk.getKode()) &&
                isValidNama(produk.getNama()) &&
                isValidNama(produk.getKategori()) &&
                isValidHarga(produk.getHarga()) &&
                isValidStok(produk.getStok()) &&
                isValidStokMinimum(produk.getStokMinimum()) &&
                produk.getStok() >= 0 &&
                produk.getStokMinimum() >= 0;
    }

    // Validasi kategori
    public static boolean isValidKategori(Kategori kategori) {
        if (kategori == null) {
            return false;
        }
        return isValidKodeProduk(kategori.getKode()) &&
                isValidNama(kategori.getNama()) &&
                (kategori.getDeskripsi() == null || kategori.getDeskripsi().length() <= 500);
    }

    // Validasi percentage (0-100)
    public static boolean isValidPercentage(double percentage) {
        return percentage >= 0 && percentage <= 100;
    }

    // Validasi kuantitas (positif)
    public static boolean isValidKuantitas(int kuantitas) {
        return kuantitas > 0;
    }
}