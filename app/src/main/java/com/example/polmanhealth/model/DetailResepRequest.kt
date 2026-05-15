package com.example.polmanhealth.model

data class DetailResepRequest(
    val id_rekam_medis: Int,
    val id_obat: Int,
    val dosis: String,
    val aturan_pakai: String,
    val jumlah: Int,
    val keterangan: String?
)