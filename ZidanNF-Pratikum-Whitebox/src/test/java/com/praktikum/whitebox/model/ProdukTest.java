package com.praktikum.whitebox.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Class Produk - White Box Testing")
public class ProdukTest {
    private Produk produk;

    @BeforeEach
    void setUp() {
        produk = new Produk("PROD001",
                "Laptop Gaming",
                "Elektronik",
                15000000,
                10,
                5);
    }

    // =======================================================================
    // PERBAIKAN: MENARGETKAN GETTER/SETTER & CONSTRUCTOR DEFAULT (100% Method Cov.)
    // =======================================================================
    @Test
    @DisplayName("Test getter/setter, constructor default, dan toString")
    void testGettersSettersAndToString() {
        // Test Constructor Default
        Produk p = new Produk();
        assertNotNull(p);

        // Test Setters dan Getters
        p.setKode("ABC");
        assertEquals("ABC", p.getKode());

        p.setNama("Meja");
        assertEquals("Meja", p.getNama());

        p.setKategori("FURNITUR");
        assertEquals("FURNITUR", p.getKategori());

        p.setHarga(100.0);
        assertEquals(100.0, p.getHarga());

        p.setStok(20);
        assertEquals(20, p.getStok());

        p.setStokMinimum(1);
        assertEquals(1, p.getStokMinimum());

        p.setAktif(false);
        assertFalse(p.isAktif());

        // Test toString()
        assertTrue(p.toString().contains("Meja"));
    }
    // =======================================================================
    // LOGIKA STOK (BOUNDARY VALUE)
    // =======================================================================
    @Test
    @DisplayName("Test status stok - stok aman")
    void testStokAman() {
        produk.setStok(10);
        produk.setStokMinimum(5);
        assertTrue(produk.isStokAman());
        assertFalse(produk.isStokMenipis());
        assertFalse(produk.isStokHabis());
    }

    @Test
    @DisplayName("Test status stok - stok menipis (Batas sama dengan Min)")
    void testStokMenipisBoundary() {
        produk.setStok(5); // Stok = StokMinimum (Path isStokMenipis = TRUE)
        produk.setStokMinimum(5);
        assertFalse(produk.isStokAman());
        assertTrue(produk.isStokMenipis());
    }

    @Test
    @DisplayName("Test status stok - stok habis")
    void testStokHabis() {
        produk.setStok(0); // Batas 0 (Path isStokHabis = TRUE)
        produk.setStokMinimum(5);
        assertFalse(produk.isStokAman());
        assertFalse(produk.isStokMenipis());
        assertTrue(produk.isStokHabis());
    }

    // =======================================================================
    // KURANGI STOK (PATH COVERAGE & EXCEPTION HANDLING)
    // =======================================================================
    @ParameterizedTest
    @DisplayName("Test kurangi stok berhasil (Valid Path)")
    @CsvSource({
            "5, 5",
            "3, 7",
            "10, 0"
    })
    void testKurangiStokValid(int jumlah, int expectedStok) {
        produk.kurangiStok(jumlah);
        assertEquals(expectedStok, produk.getStok());
    }

    @Test
    @DisplayName("Test kurangi stok - jumlah nol atau negatif (Exception Path 1)")
    void testKurangiStokNegatifAtauNol() {
        // Path Exception 1: jumlah <= 0
        Exception exceptionNol = assertThrows(IllegalArgumentException.class, () -> {
            produk.kurangiStok(0); // Test jumlah = 0
        });
        assertEquals("Jumlah harus positif", exceptionNol.getMessage());

        Exception exceptionNegatif = assertThrows(IllegalArgumentException.class, () -> {
            produk.kurangiStok(-5); // Test jumlah negatif
        });
        assertEquals("Jumlah harus positif", exceptionNegatif.getMessage());
    }

    @Test
    @DisplayName("Test kurangi stok - stok tidak mencukupi (Exception Path 2)")
    void testKurangiStokTidakMencukupi() {
        // Path Exception 2: jumlah > stok
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.kurangiStok(15);
        });
        assertEquals("Stok tidak mencukupi", exception.getMessage());
    }

    // =======================================================================
    // TAMBAH STOK & HARGA (METHOD COVERAGE & EXCEPTION HANDLING)
    // =======================================================================
    @Test
    @DisplayName("Test tambah stok valid & gagal")
    void testTambahStok() {
        // Valid Path
        produk.tambahStok(5);
        assertEquals(15, produk.getStok());

        // Exception Path: jumlah <= 0
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.tambahStok(0);
        });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Test hitung total harga valid")
    @CsvSource({
            "1, 15000000",
            "2, 30000000",
            "5, 75000000"
    })
    void testHitungTotalHarga(int jumlah, double expectedTotal) {
        double total = produk.hitungTotalHarga(jumlah);
        assertEquals(expectedTotal, total, 0.001);
    }

    @Test
    @DisplayName("Test hitung total harga - jumlah negatif (Exception Path)")
    void testHitungTotalHargaNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            produk.hitungTotalHarga(-1);
        });
        assertEquals("Jumlah harus positif", exception.getMessage());
    }

    // Test equals, hashCode (Sudah ada di kode Anda)
    @Test
    @DisplayName("Test equals dan hashCode")
    void testEqualsAndHashCode() {
        Produk produk1 = new Produk("PROD001", "Laptop", "Elektronik", 1000000, 5, 2);
        Produk produk2 = new Produk("PROD001", "Laptop Baru", "Elektronik", 1200000, 3, 1);
        Produk produk3 = new Produk("PROD002", "Mouse", "Elektronik", 50000, 10, 5);

        // equals() paths
        assertEquals(produk1, produk2); // kode sama (TRUE)
        assertNotEquals(produk1, produk3); // kode berbeda (FALSE)

        // hashCode()
        assertEquals(produk1.hashCode(), produk2.hashCode());
    }
}