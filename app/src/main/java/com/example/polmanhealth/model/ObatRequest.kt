package com.example.polmanhealth.model

data class ObatRequest(
    val nama_obat: String,
    val jenis_obat: String,
    val stok: Int = 100
)