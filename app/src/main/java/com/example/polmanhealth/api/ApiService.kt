package com.example.polmanhealth.api

import com.example.polmanhealth.model.PasienResponse
import com.example.polmanhealth.model.RegisterRequest
import com.example.polmanhealth.model.LoginResponse
import com.example.polmanhealth.model.AdminLoginResponse
import com.example.polmanhealth.model.JadwalHariResponse
import com.example.polmanhealth.model.DokterResponse
import com.example.polmanhealth.model.PendaftaranRequest
import com.example.polmanhealth.model.PendaftaranResponse
import com.example.polmanhealth.model.AdminAntreanResponse
import com.example.polmanhealth.model.RekamMedisRequest
import com.example.polmanhealth.model.RekamMedisResponse
import com.example.polmanhealth.model.RiwayatRekamMedisResponse
import com.example.polmanhealth.model.ObatRequest
import com.example.polmanhealth.model.ObatResponse
import com.example.polmanhealth.model.DetailResepRequest
import com.example.polmanhealth.model.DetailResepResponse
import com.example.polmanhealth.model.DetailResepObatResponse
import retrofit2.http.PUT
import com.example.polmanhealth.model.PasienUpdateRequest

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

    @GET("pasien/{id_pasien}/antrean-aktif")
    fun getAntreanAktif(
        @Path("id_pasien") idPasien: Int
    ): Call<PendaftaranResponse>

    @POST("pendaftaran")
    fun buatPendaftaran(
        @Body request: PendaftaranRequest
    ): Call<PendaftaranResponse>

    @GET("admin/daftar-antrean")
    fun getDaftarAntreanAdmin(): Call<List<AdminAntreanResponse>>

    @POST("rekam-medis")
    fun createRekamMedis(
        @Body request: RekamMedisRequest
    ): Call<RekamMedisResponse>

    @GET("pasien/{id_pasien}/riwayat-rekam-medis")
    fun getRiwayatRekamMedis(
        @Path("id_pasien") idPasien: Int
    ): Call<List<RiwayatRekamMedisResponse>>

    @POST("obat/get-or-create")
    fun getOrCreateObat(
        @Body request: ObatRequest
    ): Call<ObatResponse>

    @POST("detail-resep")
    fun createDetailResep(
        @Body request: DetailResepRequest
    ): Call<DetailResepResponse>

    @GET("rekam-medis/{id_rekam_medis}/detail-resep")
    fun getDetailResepByRekamMedis(
        @Path("id_rekam_medis") idRekamMedis: Int
    ): Call<List<DetailResepObatResponse>>

    @GET("obat")
    fun getAllObat(): Call<List<ObatResponse>>

    @PUT("pendaftaran/{id_pendaftaran}/status")
    fun updateStatusPendaftaran(
        @Path("id_pendaftaran") idPendaftaran: Int,
        @Query("status") status: String
    ): Call<Map<String, String>>

    @PUT("pasien/{id_pasien}")
    fun updatePasien(
        @Path("id_pasien") idPasien: Int,
        @Body request: PasienUpdateRequest
    ): Call<PasienResponse>

}