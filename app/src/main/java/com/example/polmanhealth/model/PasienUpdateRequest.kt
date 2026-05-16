package com.example.polmanhealth.model

data class PasienUpdateRequest(
    val nama_pasien: String,
    val email: String,
    val no_telp: String,
    val alamat: String
)