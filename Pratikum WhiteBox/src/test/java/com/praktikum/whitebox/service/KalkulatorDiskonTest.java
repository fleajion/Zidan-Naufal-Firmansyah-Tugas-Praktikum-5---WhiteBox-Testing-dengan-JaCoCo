package com.praktikum.whitebox.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Kalkulator Diskon - Path Coverage")
public class KalkulatorDiskonTest {

    private KalkulatorDiskon kalkulatorDiskon;

    @BeforeEach
    void setup() {
        kalkulatorDiskon = new KalkulatorDiskon();
    }

    // ✅ Test hitungDiskon dengan berbagai kombinasi input
    @ParameterizedTest
    @DisplayName("Test hitung diskon - berbagai kombinasi kuantitas dan tipe pelanggan")
    @CsvSource({
            // kuantitas, tipePelanggan, expectedDiskon
            "1, 'BARU', 20",        // 2% dari 1000
            "5, 'BARU', 350",       // 7% dari 5000
            "10, 'REGULER', 1500",  // 15% dari 10000
            "50, 'PREMIUM', 12500", // 25% dari 50000
            "100, 'PREMIUM', 30000",// 30% dari 100000
            "200, 'PREMIUM', 60000" // 30% dari 200000
    })
    void testHitungDiskonVariousCases(int kuantitas, String tipePelanggan, double expectedDiskon) {
        double harga = 1000;
        double diskon = kalkulatorDiskon.hitungDiskon(harga, kuantitas, tipePelanggan);
        assertEquals(expectedDiskon, diskon, 0.001,
                () -> String.format("Diskon tidak sesuai untuk kuantitas=%d, tipe=%s", kuantitas, tipePelanggan));
    }

    // ✅ Test validasi parameter invalid
    @Test
    @DisplayName("Test hitung diskon - parameter invalid (harga/kuantitas negatif atau nol)")
    void testHitungDiskonInvalidParameters() {
        // harga negatif
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                kalkulatorDiskon.hitungDiskon(-1000, 5, "REGULER"));
        assertEquals("Harga dan kuantitas harus positif", exception.getMessage());

        // kuantitas nol
        exception = assertThrows(IllegalArgumentException.class, () ->
                kalkulatorDiskon.hitungDiskon(1000, 0, "REGULER"));
        assertEquals("Harga dan kuantitas harus positif", exception.getMessage());
    }

    // ✅ Test harga akhir setelah diskon diterapkan
    @Test
    @DisplayName("Test hitung harga setelah diskon")
    void testHitungHargaSetelahDiskon() {
        double harga = 1000;
        int kuantitas = 10;
        String tipePelanggan = "REGULER";

        double hargaSetelahDiskon = kalkulatorDiskon.hitungHargaSetelahDiskon(harga, kuantitas, tipePelanggan);

        double expectedTotal = 1000 * 10;       // 10000
        double expectedDiskon = expectedTotal * 0.15; // 1500 (10% + 5%)
        double expectedHargaAkhir = expectedTotal - expectedDiskon; // 8500

        assertEquals(expectedHargaAkhir, hargaSetelahDiskon, 0.001);
    }

    // ✅ Test kategori diskon berdasarkan persentase
    @ParameterizedTest
    @DisplayName("Test kategori diskon berdasarkan persentase (threshold fix)")
    @CsvSource({
            "0.00, 'TANPA_DISKON'",      // 0% → TANPA_DISKON
            "0.04, 'TANPA_DISKON'",      // <5%
            "0.05, 'DISKON_RINGAN'",     // >=5%
            "0.09, 'DISKON_RINGAN'",     // <10%
            "0.10, 'DISKON_SEDANG'",     // >=10%
            "0.15, 'DISKON_SEDANG'",     // <20%
            "0.19, 'DISKON_SEDANG'",     // <20%
            "0.20, 'DISKON_BESAR'",      // >=20%
            "0.25, 'DISKON_BESAR'",      // >=20%
            "0.30, 'DISKON_BESAR'"       // >=20%
    })
    void testGetKategoriDiskon(double persentaseDiskon, String expectedKategori) {
        String kategori = kalkulatorDiskon.getKategoriDiskon(persentaseDiskon);
        assertEquals(expectedKategori, kategori,
                () -> String.format("Kategori salah untuk persentase=%.2f", persentaseDiskon));
    }

    // ✅ Test nilai batas (boundary test)
    @Test
    @DisplayName("Test boundary values untuk kuantitas diskon")
    void testBoundaryValuesKuantitas() {
        double harga = 1000;

        // Boundary: 4 -> 5 (naik ke 5%)
        double diskon4 = kalkulatorDiskon.hitungDiskon(harga, 4, "BARU");
        double diskon5 = kalkulatorDiskon.hitungDiskon(harga, 5, "BARU");
        assertTrue(diskon5 > diskon4, "Diskon kuantitas 5 harus lebih besar dari 4");

        // Boundary: 9 -> 10 (naik ke 10%)
        double diskon9 = kalkulatorDiskon.hitungDiskon(harga, 9, "BARU");
        double diskon10 = kalkulatorDiskon.hitungDiskon(harga, 10, "BARU");
        assertTrue(diskon10 > diskon9, "Diskon kuantitas 10 harus lebih besar dari 9");

        // Boundary: 49 -> 50 (naik ke 15%)
        double diskon49 = kalkulatorDiskon.hitungDiskon(harga, 49, "BARU");
        double diskon50 = kalkulatorDiskon.hitungDiskon(harga, 50, "BARU");
        assertTrue(diskon50 > diskon49, "Diskon kuantitas 50 harus lebih besar dari 49");

        // Boundary: 99 -> 100 (naik ke 20%)
        double diskon99 = kalkulatorDiskon.hitungDiskon(harga, 99, "BARU");
        double diskon100 = kalkulatorDiskon.hitungDiskon(harga, 100, "BARU");
        assertTrue(diskon100 > diskon99, "Diskon kuantitas 100 harus lebih besar dari 99");
    }
}
