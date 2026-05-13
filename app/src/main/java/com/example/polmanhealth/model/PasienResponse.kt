package com.example.polmanhealth.model

data class PasienResponse(
    val id_pasien: Int,
    val nama_pasien: String,
    val email: String,
    val no_telp: String,
    val alamat: String
)