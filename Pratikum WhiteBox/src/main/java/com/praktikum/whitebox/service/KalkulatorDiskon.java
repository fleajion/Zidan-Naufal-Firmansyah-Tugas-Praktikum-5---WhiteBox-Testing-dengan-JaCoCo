package com.praktikum.whitebox.service;

public class KalkulatorDiskon {

    public double hitungDiskon(double harga, int kuantitas, String tipePelanggan) {
        if (harga <= 0 || kuantitas <= 0) {
            throw new IllegalArgumentException("Harga dan kuantitas harus positif");
        }

        double totalHarga = harga * kuantitas;
        double diskonKuantitas = hitungDiskonKuantitas(kuantitas);
        double diskonPelanggan = hitungDiskonPelanggan(tipePelanggan);

        double totalPersentaseDiskon = diskonKuantitas + diskonPelanggan;

        if (totalPersentaseDiskon > 0.30) {
            totalPersentaseDiskon = 0.30;
        }

        return totalHarga * totalPersentaseDiskon;
    }

    private double hitungDiskonKuantitas(int kuantitas) {
        if (kuantitas >= 100) {
            return 0.20;
        } else if (kuantitas >= 50) {
            return 0.15;
        } else if (kuantitas >= 10) {
            return 0.10;
        } else if (kuantitas >= 5) {
            return 0.05;
        } else {
            return 0.00;
        }
    }

    private double hitungDiskonPelanggan(String tipePelanggan) {
        switch (tipePelanggan.toUpperCase()) {
            case "PREMIUM":
                return 0.10;
            case "REGULER":
                return 0.05;
            case "BARU":
                return 0.02;
            default:
                return 0.00;
        }
    }

    public double hitungHargaSetelahDiskon(double harga, int kuantitas, String tipePelanggan) {
        double totalHarga = harga * kuantitas;
        double diskon = hitungDiskon(harga, kuantitas, tipePelanggan);
        return totalHarga - diskon;
    }

    public String getKategoriDiskon(double persentaseDiskon) {
        final double TOLERANCE = 0.0001;
        double rounded = Math.round(persentaseDiskon * 1000.0) / 1000.0;

        if (rounded >= 0.20 - TOLERANCE) {
            return "DISKON_BESAR";
        } else if (rounded >= 0.10 - TOLERANCE) {
            return "DISKON_SEDANG";
        } else if (rounded >= 0.05 - TOLERANCE) {
            return "DISKON_RINGAN";
        } else {
            return "TANPA_DISKON";
        }
    }
}
