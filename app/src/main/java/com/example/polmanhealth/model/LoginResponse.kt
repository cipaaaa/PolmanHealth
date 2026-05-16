package com.example.polmanhealth.model

data class LoginResponse(
    val id_pasien: Int,
    val nama_pasien: String,
    val email: String,
    val no_telp: String?,
    val alamat: String?
)

data class AdminLoginResponse(
    val message: String,
    val role: String,
    val id_admin: Int,
    val email: String
)