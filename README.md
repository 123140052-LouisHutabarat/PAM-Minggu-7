# Notes App - Minggu 7 PAM

**Nama** : Louis Hutabarat

**NIM** : 123140052 

Aplikasi mobile multiplatform yang dibangun menggunakan **Kotlin Multiplatform (KMP)** dan **Jetpack Compose Multiplatform**. Aplikasi ini mencakup fitur catatan, berita teknologi, pengaturan, dan profil pengguna.

---

##  Link Tambahan
* **Link Google Drive (APK/Dokumentasi)**: [Klik di sini untuk mengakses]([https://drive.google.com/drive/folders/1A2lBZcdC_pJts3uBErPaDCw8ms_HZ_VJ?usp=sharing](https://drive.google.com/drive/folders/1ADL2QPdpXAQg8n_20aTaH4GZ79rqYC3b?usp=sharing))

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

---

## Screenshots

### News
| List Berita | Tidak Ada Koneksi |
|---|---|
| ![News List](https://github.com/user-attachments/assets/8eeb01f9-bebe-44f1-85d9-b6c74b76a6d9) | ![News Error](https://github.com/user-attachments/assets/00cc9cad-d761-419c-bb1c-23d12053e94e) |

### Notes
| Daftar Catatan |
|---|
| ![Notes](https://github.com/user-attachments/assets/6530be0d-ee3e-4047-bb0c-e78d37fb178f) |

### Favorites
| Catatan Favorit |
|---|
| ![Favorites](https://github.com/user-attachments/assets/0f4d629e-0e0c-4f91-ac6a-f574deee636b) |

### Settings
| Light Mode | Dark Mode |
|---|---|
| ![Settings Light](https://github.com/user-attachments/assets/201d47d8-7963-4987-aeb5-5096eaf3af81) | ![Settings Dark](https://github.com/user-attachments/assets/00d28f5e-65c2-4a3c-bdc7-f27ff481a167) |
