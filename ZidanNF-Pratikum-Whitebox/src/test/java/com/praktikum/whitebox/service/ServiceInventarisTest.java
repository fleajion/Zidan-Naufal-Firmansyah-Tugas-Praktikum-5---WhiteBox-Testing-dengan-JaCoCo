package com.praktikum.whitebox.service;

import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.repository.RepositoryProduk;
import com.praktikum.whitebox.util.ValidationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

// Anotasi untuk menggunakan Mockito
@ExtendWith(MockitoExtension.class)
@DisplayName("Test Service Inventaris")
public class ServiceInventarisTest {

    @Mock
    private RepositoryProduk mockRepositoryProduk;

    @InjectMocks
    private ServiceInventaris serviceInventaris;

    private Produk produkTest;
    private Produk produkNonAktif;

    @BeforeEach
    void setUp() {
        produkTest = new Produk("PROD001", "Laptop Gaming", "Elektronik", 15000000, 10, 5);
        produkNonAktif = new Produk("PROD002", "Keyboard Lama", "Elektronik", 300000, 3, 1);
        produkNonAktif.setAktif(false);
    }

    // =================================================================
    // TAMBAH PRODUK
    // =================================================================

    @Test
    @DisplayName("Tambah produk berhasil - semua kondisi valid")
    void testTambahProdukBerhasil() {
        try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
            mocked.when(() -> ValidationUtils.isValidProduk(any(Produk.class))).thenReturn(true);
            when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
            when(mockRepositoryProduk.simpan(produkTest)).thenReturn(true);

            assertTrue(serviceInventaris.tambahProduk(produkTest));
            verify(mockRepositoryProduk).simpan(produkTest);
        }
    }

    @Test
    @DisplayName("Tambah produk gagal - produk sudah ada")
    void testTambahProdukGagalSudahAda() {
        try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
            mocked.when(() -> ValidationUtils.isValidProduk(any(Produk.class))).thenReturn(true);
            when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

            assertFalse(serviceInventaris.tambahProduk(produkTest));
            verify(mockRepositoryProduk, never()).simpan(any(Produk.class));
        }
    }

    @Test
    @DisplayName("Tambah produk gagal - produk tidak valid")
    void testTambahProdukGagalTidakValid() {
        try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
            mocked.when(() -> ValidationUtils.isValidProduk(any(Produk.class))).thenReturn(false);

            assertFalse(serviceInventaris.tambahProduk(produkTest));
            verify(mockRepositoryProduk, never()).cariByKode(anyString());
        }
    }

    // =================================================================
    // HAPUS PRODUK
    // =================================================================

    @Test
    @DisplayName("Hapus produk berhasil - stok nol")
    void testHapusProdukBerhasil() {
        try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
            Produk produkStokNol = new Produk("PROD001", "Laptop", "Elektronik", 0.0, 0, 5);

            mocked.when(() -> ValidationUtils.isValidKodeProduk("PROD001")).thenReturn(true);
            when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkStokNol));
            when(mockRepositoryProduk.hapus("PROD001")).thenReturn(true);

            assertTrue(serviceInventaris.hapusProduk("PROD001"));
            verify(mockRepositoryProduk).hapus("PROD001");
        }
    }

    @Test
    @DisplayName("Hapus produk gagal - kode tidak valid")
    void testHapusProdukGagalKodeInvalid() {
        try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
            mocked.when(() -> ValidationUtils.isValidKodeProduk(anyString())).thenReturn(false);

            assertFalse(serviceInventaris.hapusProduk(""));
            verify(mockRepositoryProduk, never()).cariByKode(anyString());
        }
    }

    @Test
    @DisplayName("Hapus produk gagal - produk tidak ditemukan")
    void testHapusProdukGagalTidakDitemukan() {
        try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
            mocked.when(() -> ValidationUtils.isValidKodeProduk("PROD001")).thenReturn(true);
            when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());

            assertFalse(serviceInventaris.hapusProduk("PROD001"));
            verify(mockRepositoryProduk, never()).hapus(anyString());
        }
    }

    @Test
    @DisplayName("Hapus produk gagal - stok masih ada (> 0)")
    void testHapusProdukGagalStokMasihAda() {
        try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
            mocked.when(() -> ValidationUtils.isValidKodeProduk("PROD001")).thenReturn(true);
            when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

            assertFalse(serviceInventaris.hapusProduk("PROD001"));
            verify(mockRepositoryProduk, never()).hapus(anyString());
        }
    }

    // =================================================================
    // KELUAR STOK
    // =================================================================

    @Test
    @DisplayName("Keluar stok berhasil - stok mencukupi")
    void testKeluarStokBerhasil() {
        produkTest.setAktif(true);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 5)).thenReturn(true);

        assertTrue(serviceInventaris.keluarStok("PROD001", 5));
        verify(mockRepositoryProduk).updateStok("PROD001", 5);
    }

    @Test
    @DisplayName("Keluar stok gagal - jumlah <= 0 atau kode tidak valid")
    void testKeluarStokGagalJumlahInvalid() {
        assertFalse(serviceInventaris.keluarStok("PROD001", 0));
        assertFalse(serviceInventaris.keluarStok("PROD001", -5));
    }

    @Test
    @DisplayName("Keluar stok gagal - produk tidak ditemukan/tidak aktif")
    void testKeluarStokGagalProdukTidakDitemukanAtauTidakAktif() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        assertFalse(serviceInventaris.keluarStok("PROD001", 5));

        when(mockRepositoryProduk.cariByKode("PROD002")).thenReturn(Optional.of(produkNonAktif));
        assertFalse(serviceInventaris.keluarStok("PROD002", 5));

        verify(mockRepositoryProduk, times(2)).cariByKode(anyString());
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Keluar stok gagal - stok tidak mencukupi")
    void testKeluarStokGagalStokTidakMencukupi() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        assertFalse(serviceInventaris.keluarStok("PROD001", 15));
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    // =================================================================
    // METHOD LAIN
    // =================================================================

    @Test
    @DisplayName("Masuk stok berhasil dan gagal (path coverage)")
    void testMasukStok() {
        produkTest.setAktif(true);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 20)).thenReturn(true);
        assertTrue(serviceInventaris.masukStok("PROD001", 10));

        assertFalse(serviceInventaris.masukStok("PROD001", 0));

        when(mockRepositoryProduk.cariByKode("PROD002")).thenReturn(Optional.of(produkNonAktif));
        assertFalse(serviceInventaris.masukStok("PROD002", 5));

        verify(mockRepositoryProduk).cariByKode("PROD002");
    }

    @Test
    @DisplayName("Cari produk by kode - sukses, tidak ditemukan, dan validasi gagal")
    void testCariProdukByKode() {
        try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
            mocked.when(() -> ValidationUtils.isValidKodeProduk("PROD001")).thenReturn(true);
            when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
            assertTrue(serviceInventaris.cariProdukByKode("PROD001").isPresent());

            mocked.when(() -> ValidationUtils.isValidKodeProduk("PROD002")).thenReturn(true);
            when(mockRepositoryProduk.cariByKode("PROD002")).thenReturn(Optional.empty());
            assertFalse(serviceInventaris.cariProdukByKode("PROD002").isPresent());

            mocked.when(() -> ValidationUtils.isValidKodeProduk("INVALID")).thenReturn(false);
            assertFalse(serviceInventaris.cariProdukByKode("INVALID").isPresent());
        }
    }

    @Test
    @DisplayName("Cari produk by nama - menguji method cariProdukByNama")
    void testCariProdukByNama() {
        Produk p1 = new Produk("P1", "Kemeja A", "K", 100, 2, 1);
        when(mockRepositoryProduk.cariByNama(anyString())).thenReturn(Collections.singletonList(p1));

        List<Produk> hasil = serviceInventaris.cariProdukByNama("Kemeja");

        assertFalse(hasil.isEmpty());
        assertEquals(1, hasil.size());
        verify(mockRepositoryProduk).cariByNama(anyString());
    }

    @Test
    @DisplayName("Cari produk by kategori - menguji method cariProdukByKategori")
    void testCariProdukByKategori() {
        Produk p1 = new Produk("P1", "Kemeja", "KAT_FASHION", 100, 2, 1);
        when(mockRepositoryProduk.cariByKategori(anyString())).thenReturn(Collections.singletonList(p1));

        List<Produk> hasil = serviceInventaris.cariProdukByKategori("FASHION");

        assertFalse(hasil.isEmpty());
        assertEquals(1, hasil.size());
        verify(mockRepositoryProduk).cariByKategori(anyString());
    }

    @Test
    @DisplayName("Hitung total stok - melewati filter non-aktif")
    void testHitungTotalStok() {
        Produk p1 = new Produk("P1", "A", "Kat1", 100, 2, 1);
        Produk p2 = new Produk("P2", "B", "Kat2", 50, 15, 1);
        Produk p3 = new Produk("P3", "C", "Kat3", 1000, 100, 1);
        p3.setAktif(false);

        when(mockRepositoryProduk.cariSemua()).thenReturn(Arrays.asList(p1, p2, p3));

        assertEquals(17, serviceInventaris.hitungTotalStok());
    }

    @Test
    @DisplayName("Hitung total nilai inventaris - melewati filter non-aktif")
    void testHitungTotalNilaiInventaris() {
        Produk p1 = new Produk("P1", "A", "Kat1", 100, 2, 1);
        Produk p2 = new Produk("P2", "B", "Kat2", 50, 5, 1);
        Produk p3 = new Produk("P3", "C", "Kat3", 1000, 10, 1);
        p3.setAktif(false);

        List<Produk> semuaProduk = Arrays.asList(p1, p2, p3);
        when(mockRepositoryProduk.cariSemua()).thenReturn(semuaProduk);

        double expected = (100 * 2) + (50 * 5);
        assertEquals(450.0, serviceInventaris.hitungTotalNilaiInventaris(), 0.001);
    }

    @Test
    @DisplayName("Get produk stok menipis")
    void testGetProdukStokMenipis() {
        Produk produkStokMenipis = new Produk("PROD002", "Mouse", "Elektronik", 500000, 3, 5);
        List<Produk> produkMenipis = Collections.singletonList(produkStokMenipis);

        when(mockRepositoryProduk.cariProdukStokMenipis()).thenReturn(produkMenipis);

        List<Produk> hasil = serviceInventaris.getProdukStokMenipis();

        assertEquals(1, hasil.size());
        assertEquals("PROD002", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariProdukStokMenipis();
    }

    // ✅ Tambahan untuk menutupi method terakhir yang belum pernah dipanggil
    @Test
    @DisplayName("Get produk stok habis - memastikan method terpanggil dan hasil sesuai")
    void testGetProdukStokHabis() {
        Produk produkHabis = new Produk("P9", "Printer", "Elektronik", 1000000, 0, 1);
        List<Produk> produkList = Collections.singletonList(produkHabis);

        when(mockRepositoryProduk.cariProdukStokHabis()).thenReturn(produkList);

        List<Produk> hasil = serviceInventaris.getProdukStokHabis();

        assertEquals(1, hasil.size());
        assertEquals("P9", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariProdukStokHabis();
    }

    @Test
    @DisplayName("Update stok berhasil dan gagal validasi/produk tidak ditemukan")
    void testUpdateStok() {
        try (MockedStatic<ValidationUtils> mocked = mockStatic(ValidationUtils.class)) {
            String kode = "PROD001";

            mocked.when(() -> ValidationUtils.isValidKodeProduk(anyString())).thenReturn(false);
            assertFalse(serviceInventaris.updateStok(kode, 20));

            mocked.when(() -> ValidationUtils.isValidKodeProduk(kode)).thenReturn(true);
            assertFalse(serviceInventaris.updateStok(kode, -5));

            when(mockRepositoryProduk.cariByKode(kode)).thenReturn(Optional.empty());
            assertFalse(serviceInventaris.updateStok(kode, 20));

            when(mockRepositoryProduk.cariByKode(kode)).thenReturn(Optional.of(produkTest));
            when(mockRepositoryProduk.updateStok(kode, 20)).thenReturn(true);
            assertTrue(serviceInventaris.updateStok(kode, 20));
        }
    }

    @Test
    @DisplayName("Get semua produk - pastikan method terpanggil dan hasil sesuai")
    void testGetSemuaProduk() {
        Produk p1 = new Produk("P1", "Laptop", "Elektronik", 10000, 2, 1);
        when(mockRepositoryProduk.cariSemua()).thenReturn(Collections.singletonList(p1));

        List<Produk> hasil = serviceInventaris.getSemuaProduk();

        assertEquals(1, hasil.size());
        assertEquals("P1", hasil.get(0).getKode());
        assertEquals("Laptop", hasil.get(0).getNama());
        verify(mockRepositoryProduk).cariSemua();
    }

    @Test
    @DisplayName("Get produk aktif - pastikan hanya produk aktif dikembalikan")
    void testGetProdukAktif() {
        Produk aktif = new Produk("A1", "Mouse", "Elektronik", 50000, 10, 1);
        Produk nonAktif = new Produk("A2", "Keyboard", "Elektronik", 75000, 5, 1);
        nonAktif.setAktif(false);

        when(mockRepositoryProduk.cariSemua()).thenReturn(Arrays.asList(aktif, nonAktif));

        List<Produk> hasil = serviceInventaris.getProdukAktif();

        assertEquals(1, hasil.size());
        assertTrue(hasil.get(0).isAktif());
        assertEquals("A1", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariSemua();
    }
}