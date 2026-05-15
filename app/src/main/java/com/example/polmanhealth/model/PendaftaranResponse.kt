package com.example.polmanhealth.model

data class PendaftaranResponse(
    val id_pendaftaran: Int,
    val id_pasien: Int,
    val id_dokter: Int,
    val id_jadwal: Int,
    val tanggal: String,
    val keluhan: String,
    val nomor_antrean: Int,
    val status: String
)