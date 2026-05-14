package com.example.polmanhealth.model

data class PendaftaranRequest(
    val id_pasien: Int,
    val id_dokter: Int,
    val id_jadwal: Int,
    val tanggal: String,
    val keluhan: String,
    val status: String = "Menunggu"
)