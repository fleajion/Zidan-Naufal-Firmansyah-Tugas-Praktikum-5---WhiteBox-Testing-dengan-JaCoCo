package com.praktikum.whitebox.util;

import com.praktikum.whitebox.model.Kategori;
import com.praktikum.whitebox.model.Produk;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilsTest {

    // ----------- isValidKodeProduk (Target Branch Coverage) -----------
    // Menguji kode valid
    @Test
    void testIsValidKodeProduk_Valid() {
        // PERBAIKAN: Mengganti "KODE_ABC" dengan "KODEABC" untuk menghilangkan underscore
        assertTrue(ValidationUtils.isValidKodeProduk("A123")); // Min 3, Max 10
        assertTrue(ValidationUtils.isValidKodeProduk("KODEABC"));
    }

    // Menguji kode TIDAK valid (jalur kegagalan: null, kosong, batas min/max)
    @Test
    void testIsValidKodeProduk_Invalid() {
        // Jalur Kegagalan 1: null
        assertFalse(ValidationUtils.isValidKodeProduk(null));
        // Jalur Kegagalan 2: string kosong/spasi
        assertFalse(ValidationUtils.isValidKodeProduk(""));
        assertFalse(ValidationUtils.isValidKodeProduk("   "));
        // Jalur Kegagalan 3: Batas bawah (2 karakter)
        assertFalse(ValidationUtils.isValidKodeProduk("AB"));
        // Jalur Kegagalan 4: Batas atas (11 karakter)
        assertFalse(ValidationUtils.isValidKodeProduk("1234567890X"));
        // Jalur Kegagalan 5: Karakter ilegal
        assertFalse(ValidationUtils.isValidKodeProduk("KODE*"));
    }

    // ----------- isValidNama (Target Boundary Value) -----------
    @Test
    void testIsValidNama_ValidDanBoundary() {
        assertTrue(ValidationUtils.isValidNama("ABC")); // Batas min (3)
        // Perluas agar Batas Max juga teruji (tidak perlu 100 karakter, tapi cukup panjang)
        String maxName = "A".repeat(100);
        assertTrue(ValidationUtils.isValidNama(maxName));
    }

    // Menguji nama TIDAK valid (jalur kegagalan: null, kosong, batas min/max)
    @Test
    void testIsValidNama_Invalid() {
        // Jalur Kegagalan 1: null
        assertFalse(ValidationUtils.isValidNama(null));
        // Jalur Kegagalan 2: string kosong/spasi
        assertFalse(ValidationUtils.isValidNama(""));
        assertFalse(ValidationUtils.isValidNama("  "));
        // Jalur Kegagalan 3: Di bawah batas min (2 karakter)
        assertFalse(ValidationUtils.isValidNama("AB"));
        // Jalur Kegagalan 4: Di atas batas max (101 karakter)
        String longName = "a".repeat(101);
        assertFalse(ValidationUtils.isValidNama(longName));
    }

    // ----------- isValidHarga (Target Branch Coverage: <= 0) -----------
    @Test
    void testIsValidHarga() {
        assertTrue(ValidationUtils.isValidHarga(100.0));
        // Jalur Kegagalan: 0
        assertFalse(ValidationUtils.isValidHarga(0.0));
        // Jalur Kegagalan: Negatif
        assertFalse(ValidationUtils.isValidHarga(-10.0));
    }

    // Tambahan untuk isValidStok dan isValidStokMinimum
    @Test
    void testIsValidStok() {
        assertTrue(ValidationUtils.isValidStok(5));
        // Jalur Kegagalan (0 diizinkan, negatif tidak)
        assertTrue(ValidationUtils.isValidStok(0));
        assertFalse(ValidationUtils.isValidStok(-1));

        assertTrue(ValidationUtils.isValidStokMinimum(5));
        assertTrue(ValidationUtils.isValidStokMinimum(0));
        assertFalse(ValidationUtils.isValidStokMinimum(-1));
    }

    // Tambahan untuk isValidPersentase dan isValidKuantitas
    @Test
    void testIsValidLainnya() {
        // isValidPersentase (0-100)
        assertTrue(ValidationUtils.isValidPersentase(50.0));
        assertTrue(ValidationUtils.isValidPersentase(0.0));
        assertTrue(ValidationUtils.isValidPersentase(100.0));
        assertFalse(ValidationUtils.isValidPersentase(-1.0));
        assertFalse(ValidationUtils.isValidPersentase(101.0));

        // isValidKuantitas (> 0)
        assertTrue(ValidationUtils.isValidKuantitas(1));
        assertFalse(ValidationUtils.isValidKuantitas(0));
        assertFalse(ValidationUtils.isValidKuantitas(-1));
    }

    // ----------- isValidKategori (Target Path Coverage) -----------
    @Test
    void testIsValidKategori() {
        // PENTING: Constructor Kategori: (kode, nama, deskripsi)
        Kategori validKategori = new Kategori("KAT", "Test Nama", "Deskripsi pendek");
        // Jalur Sukses
        assertTrue(ValidationUtils.isValidKategori(validKategori));

        // Jalur Kegagalan 1: Kategori null
        assertFalse(ValidationUtils.isValidKategori(null));

        // Jalur Kegagalan 2: Kode tidak valid (null)
        Kategori invalidKode = new Kategori(null, "Test", "Desk");
        assertFalse(ValidationUtils.isValidKategori(invalidKode));

        // Jalur Kegagalan 3: Nama tidak valid (1 karakter)
        Kategori invalidNama = new Kategori("KAT", "A", "Desk");
        assertFalse(ValidationUtils.isValidKategori(invalidNama));

        // Jalur Kegagalan 4: Deskripsi terlalu panjang (di atas 500)
        Kategori longDesc = new Kategori("KAT", "Test", "a".repeat(501));
        assertFalse(ValidationUtils.isValidKategori(longDesc));
    }

    // Tambahan untuk isValidProduk
    @Test
    void testIsValidProduk() {
        // PENTING: Constructor Produk: (kode, nama, kategori, harga, stok, stokMinimum)
        Produk valid = new Produk("PROD1", "Laptop", "Elek", 1000.0, 10, 5);

        // Jalur Sukses
        assertTrue(ValidationUtils.isValidProduk(valid));

        // Jalur Kegagalan 1: Produk null
        assertFalse(ValidationUtils.isValidProduk(null));

        // Jalur Kegagalan 2: Kode tidak valid
        Produk invalidKode = new Produk("P", "Laptop", "Elek", 1000.0, 10, 5);
        assertFalse(ValidationUtils.isValidProduk(invalidKode));

        // Jalur Kegagalan 3: Stok negatif (Meskipun sudah dicek di isValidStok, ini menguji Branch &&)
        Produk invalidStok = new Produk("PROD1", "Laptop", "Elek", 1000.0, -5, 5);
        assertFalse(ValidationUtils.isValidProduk(invalidStok));
    }
}