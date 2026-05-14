package com.example.polmanhealth.model

data class JadwalHariResponse(
    val id_jadwal: Int,
    val id_dokter: Int,
    val nama_dokter: String,
    val spesialis: String,
    val hari: String,
    val jam_mulai: String,
    val jam_selesai: String
)