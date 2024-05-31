# Dompet Ajaib
  Dompet Ajaib merupakan aplikasi yang bertemakan Finance. Aplikasi ini akan membantu kamu dalam membuat catatan keuangan kamu. Aplikasi ini menggunakan database SQLite untuk menyimpan datanya. Aplikasi menggunakan Create, Update, dan delete.

## Cara Penggunaan Aplikasi
  * **Login dan Register** : Pada saat pertama membuka aplikasi muncul halaman utama untuk login dan register. Silahkan tekan button Register untuk membuat akun. Di halaman Register masukkan Username dan password setelah itu akan otomatis kembali kehalaman Login. Kemudian masukkan Username dan password yang telah dibuat di register.
  * **Home Activity** : Setelah Login ada tiga fragment atau bottomNav yang ada dibawah. Dihalaman home disini kita bisa melihat CardView yang berupa Saldo, pemasukan, dan pengeluaran. Dibawahnya akan muncul Recyclerview dimana list-list item yang telah di buat.
  * **Posting Activity** : Di halaman ini kita bisa menambahkan pemasukan atau pengeluaran, memilih kategori yang ingin dipilih, dan  masukkan jumlah dan catatan. Setelah dibuat kita bisa menekan button submit. Kemudian di home akan mucul list yang telah dibuat.
  * **Info Activity** : Disini kita bisa melihat list yang dari RestApi yang telha saya masukkan. Disini kita bisa melihat list-list produk yang dibutuhkan. List produk muncul jika jaringan atau internetnya bagus. Kita bisa mudah mencarinya dengan menekan tombol searchnya kemudian ketik yang ingin kita lihat.
  * **UpdateNoteActivty** : Di halaman Home ada list-list item yang telah dibuat. Pencet salah satu listnya jika ingin mengedit atau mengupdate datanya. Di listnya juga ada tombol delete, dimana kita bisa menghapus list item yang tidak diinginkan.

## Implementasi Teknis

1. **Login dan Register**
   * Data User yang telah regis akan disimpan ke dalam Shared Preference.
   * Pada saat user login atau register, maka ada informasi yang disimpan pada SharedPreference. "is_logged_in" Berguna jika user telah login dan menutup aplikasinya kemudian aplikasinya kembali dibuka, maka diarahkan langsung ke halaman Home.
2. **Home Activity**
   Implementasi menyimpan datanya ke dalam SQLite. Recyclerview menampilkan list jenis total pemasukan atau total pengeluaran  yang telah dibuat. CardView menampilkan Saldo dari total pemasukan - total pengeluaran.
3. **Posting Activity**
   Menggunakan SQLite untuk menyimpan datanya yang dibuat disini dan akan ditampilkan list itemnya di halaman Home.
4. **Info Activity**
   Implementasi Recyclerview untuk menampilkan list-list produk sesuai attribut yang diambil dari APInya. Data yang diambil dalam bentuk <array<List> GetAllProducts.
5. **SearchView**
   Menggunakan method setupSearchView() untuk mengelola hasil pencariannya. Menggunakan filter data untuk mencari sesuai nama produknya. Menggunakan Handler untuk melakukan delay selama 2000 mils setiap pencariannya. Menggunakan method LOadMoreProducts() untuk menampilkan 10 list pertama dan 10 list berikutnya.
6 **TOmbol Logout**
   Menggunakan ImageView untuk menggunakan icon logout. Jika tombol itu ditekan makan akan kembali ke halaman Login dan SharedPreference diperbarui.

