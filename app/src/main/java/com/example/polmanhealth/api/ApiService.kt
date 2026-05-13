package com.example.polmanhealth.api

import com.example.polmanhealth.model.PasienResponse
import com.example.polmanhealth.model.RegisterRequest

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("pasien/register")
    fun registerPasien(
        @Body request: RegisterRequest
    ): Call<PasienResponse>
}