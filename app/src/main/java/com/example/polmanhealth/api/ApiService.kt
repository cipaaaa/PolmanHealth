package com.example.polmanhealth.api

import com.example.polmanhealth.model.PasienResponse
import com.example.polmanhealth.model.RegisterRequest
import com.example.polmanhealth.model.LoginResponse
import com.example.polmanhealth.model.AdminLoginResponse
import com.example.polmanhealth.model.JadwalHariResponse
import com.example.polmanhealth.model.DokterResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {



    @POST("pasien/register")
    fun registerPasien(
        @Body request: RegisterRequest
    ): Call<PasienResponse>

    @POST("pasien/login")
    fun loginPasien(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<LoginResponse>


    @POST("admin/login")
    fun loginAdmin(
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<AdminLoginResponse>

    @GET("jadwal-dokter/hari/{hari}")
    fun getJadwalByHari(
        @Path("hari") hari: String
    ): Call<List<JadwalHariResponse>>

    @GET("dokter")
    fun getAllDokter(): Call<List<DokterResponse>>

}