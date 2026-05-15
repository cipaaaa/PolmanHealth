package com.example.polmanhealth.model

data class RiwayatRekamMedisResponse(
    val id_rekam_medis: Int,
    val id_pendaftaran: Int,
    val diagnosa: String,
    val kode_icd: String?,
    val catatan: String?,
    val tanggal_pemeriksaan: String,
    val nama_dokter: String,
    val spesialis: String
)