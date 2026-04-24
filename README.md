# Notes App - Minggu 7 PAM

**Nama** : Louis Hutabarat

**NIM** : 123140052 

Aplikasi mobile multiplatform yang dibangun menggunakan **Kotlin Multiplatform (KMP)** dan **Jetpack Compose Multiplatform**. Aplikasi ini mencakup fitur catatan, berita teknologi, pengaturan, dan profil pengguna.

---

## Fitur

### Notes (CRUD)
- Tambah catatan baru dengan judul dan isi
- Edit catatan yang sudah ada
- Hapus catatan
- Cari catatan berdasarkan judul atau isi
- Data disimpan secara lokal menggunakan **SQLDelight**

### Favorites
- Tandai catatan sebagai favorit
- Lihat semua catatan favorit di tab tersendiri
- Toggle favorit langsung dari daftar catatan

### Settings
- Toggle **Dark Mode** / Light Mode
- Pengaturan urutan tampilan catatan (terbaru, terlama, judul)
- Preferensi disimpan menggunakan **Multiplatform Settings**

### Profile
- Tampilkan data profil pengguna
- Edit nama, bio, email, nomor telepon, dan lokasi
- Avatar otomatis dari nama pengguna

### News
- Menampilkan berita teknologi terbaru dari API
- Pull-to-refresh untuk memperbarui berita
- Tampilan detail artikel
- Pesan error ramah saat tidak ada koneksi internet

---

## Teknologi yang Digunakan

| Teknologi | Kegunaan |
|---|---|
| Kotlin Multiplatform (KMP) | Framework multiplatform Android & iOS |
| Jetpack Compose Multiplatform | UI deklaratif lintas platform |
| SQLDelight 2.0.2 | Database lokal |
| Ktor 2.3.7 | HTTP client untuk fetch berita |
| Multiplatform Settings 1.1.1 | Penyimpanan preferensi pengguna |
| Kotlinx DateTime 0.5.0 | Pengelolaan timestamp |
| Kamel Image 0.9.4 | Async image loading |
| Navigation Compose | Navigasi antar layar |
