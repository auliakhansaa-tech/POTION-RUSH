package com.potionrush.brewing;
import java.util.ArrayList;
import java.util.List;
// Satu kuali tempat bahan dimasukkan
public class Cauldron {
    public static final double WAKTU_MENDIDIH = 4.0;
    private final List<Ingredient> isi = new ArrayList<>();
    private CauldronState status = CauldronState.KOSONG;
    private double sisaWaktuMendidih = 0;
    private PotionType hasil = null;
    // Mengembalikan true kalau bahan berhasil masuk
    public boolean tambahBahan(Ingredient bahan) {
        if (status == CauldronState.MENDIDIH || status == CauldronState.SIAP)
        {
            return false;
        }
        isi.add(bahan);
        status = CauldronState.DIISI;
        return true;
    }
    // Menghapus bahan terakhir, hanya saat kuali masih diisi
    public void undo() {
        if (status != CauldronState.DIISI) {
            return;
        }
        isi.remove(isi.size() - 1);
        if (isi.isEmpty()) {
            status = CauldronState.KOSONG;
        }
    }
    // Mulai mendidihkan isi kuali selama 4 detik
    public void mulaiMendidih() {
        if (status == CauldronState.DIISI) {
            status = CauldronState.MENDIDIH;
            sisaWaktuMendidih = WAKTU_MENDIDIH;
        }
    }
    // Dipanggil game loop setiap frame
    public void update(double detik) {
        if (status != CauldronState.MENDIDIH) {
            return;
        }
        sisaWaktuMendidih -= detik;
        if (sisaWaktuMendidih <= 0) {
            sisaWaktuMendidih = 0;
            hasil = racik();
            status = CauldronState.SIAP;
        }
    }
    // Mengambil ramuan yang sudah jadi. null berarti ramuan gagal.
    // Setelah diambil, kuali kembali kosong.
    public PotionType ambilRamuan() {
        if (status != CauldronState.SIAP) {
            return null;
        }
        PotionType ramuan = hasil;
        hasil = null;
        status = CauldronState.KOSONG;
        return ramuan;
    }
    // Mencari resep yang cocok dengan isi kuali.
// Mengembalikan null kalau tidak ada yang cocok (ramuan gagal).
// Setelah diracik, kuali dikosongkan.
    public PotionType racik() {
        PotionType ramuan = null;
        for (Recipe resep : Recipe.semuaResep()) {
            if (resep.cocok(isi)) {
                ramuan = resep.getHasil();
                break;
            }
        }
        isi.clear();
        return ramuan;
    }
    public List<Ingredient> getIsi() {
        return isi;
    }
    public CauldronState getStatus() {
        return status;
    }
    public double getSisaWaktuMendidih() {
        return sisaWaktuMendidih;
    }
    // Hasil ramuan saat status SIAP (null = ramuan gagal)
    public PotionType getHasil() {
        return hasil;
    }
}
