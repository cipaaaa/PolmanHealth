package com.example.polmanhealth.model

data class RekamMedisRequest(
    val id_pendaftaran: Int,
    val diagnosa: String,
    val kode_icd: String?,
    val catatan: String?,
    val tanggal_pemeriksaan: String
)