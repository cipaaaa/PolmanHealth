package com.example.polmanhealth.model

data class AdminAntreanResponse(
    val id_pendaftaran: Int,
    val nama_pasien: String,
    val nama_dokter: String,
    val spesialis: String,
    val nomor_antrean: Int,
    val status: String
)