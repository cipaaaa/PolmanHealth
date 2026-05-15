package com.example.polmanhealth.model

data class DetailResepResponse(
    val id_detail_resep: Int,
    val id_rekam_medis: Int,
    val id_obat: Int,
    val dosis: String,
    val aturan_pakai: String,
    val jumlah: Int,
    val keterangan: String?
)