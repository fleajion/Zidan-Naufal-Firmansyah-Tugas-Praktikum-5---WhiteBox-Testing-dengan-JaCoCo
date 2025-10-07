package com.praktikum.whitebox.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KategoriTest {

    // Test Case 1: Menguji kedua constructor dan semua getter/setter.
    @Test
    void testConstructorDanGetterSetter() {
        // Uji Constructor default
        Kategori k1 = new Kategori();
        assertNotNull(k1);
        k1.setKode("ELK");
        k1.setNama("Elektronik");
        k1.setDeskripsi("Perangkat elektronik rumah tangga");
        k1.setAktif(true);

        // Verifikasi hasil setter
        assertEquals("ELK", k1.getKode());
        assertEquals("Elektronik", k1.getNama());
        assertEquals("Perangkat elektronik rumah tangga", k1.getDeskripsi());
        assertTrue(k1.isAktif());

        // Uji Constructor penuh
        Kategori k2 = new Kategori("MKN", "Makanan", "Makanan instan");
        assertEquals("MKN", k2.getKode());
        assertEquals("Makanan", k2.getNama());
        // Memastikan field 'aktif' di set ke true (sesuai logika constructor)
        assertTrue(k2.isAktif());
    }

    // Test Case 2: Menguji method equals() dan hashCode().
    @Test
    void testEqualsDanHashCode() {
        Kategori k1 = new Kategori("PRB", "Perabotan", "Alat rumah");
        Kategori k2 = new Kategori("PRB", "Perabotan Lama", "Alat rumah"); // Kode sama, Nama beda
        Kategori k3 = new Kategori("MBL", "Mobil", "Kendaraan");

        // 1. Jalur: k1.equals(k1) -> harus true (if (this == o))
        assertTrue(k1.equals(k1));

        // 2. Jalur: k1.equals(null) atau beda class -> harus false
        assertFalse(k1.equals(null)); // if (o == null)
        assertFalse(k1.equals(new Object())); // if (getClass() != o.getClass())

        // 3. Jalur: kodenya sama -> harus true
        assertTrue(k1.equals(k2)); // if (Objects.equals(kode, kategori.kode))

        // 4. Jalur: kodenya beda -> harus false
        assertFalse(k1.equals(k3));

        // Uji HashCode (harus sama jika equals true)
        assertEquals(k1.hashCode(), k2.hashCode());
        assertNotEquals(k1.hashCode(), k3.hashCode());
    }

    // Test Case 3: Menguji method toString().
    @Test
    void testToString() {
        Kategori k = new Kategori("TGS", "Tugas", "Modul");
        String expected = "Kategori{kode='TGS', nama='Tugas', deskripsi='Modul', aktif=true}";
        assertEquals(expected, k.toString());
    }
}