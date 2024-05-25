package com.example.crudfirebase1

class data_mahasiswa {
    // deklarasi variable atau attribut
    var nim: String? = null
    var nama: String? = null
    var jurusan: String? = null
    var key: String? = null

    // membuat konstruktor kosong untuk membaca data snapshot
    constructor() {}

    // konstruktor dengan beberapa parameter, untuk mendapatkan input dari data user
    constructor(nim: String?, nama: String?, jurusan: String?) {
        this.nim = nim
        this.nama = nama
        this.jurusan = jurusan
    }
}