package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.RiwayatRekamMedisResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class riwayat : AppCompatActivity() {

    private lateinit var cardRiwayat1: LinearLayout
    private lateinit var btnLihat1: TextView

    private lateinit var tvJudulRiwayat: TextView
    private lateinit var tvSubRiwayat: TextView
    private lateinit var tvTanggalRiwayat: TextView

    private var riwayatTerpilih: RiwayatRekamMedisResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        cardRiwayat1 = findViewById(R.id.cardRiwayat1)
        btnLihat1 = findViewById(R.id.btnLihat1)

        tvJudulRiwayat = findViewById(R.id.tvJudulRiwayat)
        tvSubRiwayat = findViewById(R.id.tvSubRiwayat)
        tvTanggalRiwayat = findViewById(R.id.tvTanggalRiwayat)

        tampilkanBelumAdaRiwayat()
        loadRiwayatMedis()

        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navJadwal).setOnClickListener {
            startActivity(Intent(this, jadwal::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navAntrean).setOnClickListener {
            startActivity(Intent(this, FormAntreanActivity::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navRiwayat).setOnClickListener {
            startActivity(Intent(this, riwayat::class.java))
            finish()
        }
    }

    private fun loadRiwayatMedis() {
        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val idPasien = sharedPref.getInt("id_pasien", 0)

        if (idPasien == 0) {
            tampilkanBelumAdaRiwayat()
            return
        }

        RetrofitClient.instance.getRiwayatRekamMedis(idPasien)
            .enqueue(object : Callback<List<RiwayatRekamMedisResponse>> {
                override fun onResponse(
                    call: Call<List<RiwayatRekamMedisResponse>>,
                    response: Response<List<RiwayatRekamMedisResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!

                        if (data.isNotEmpty()) {
                            val riwayatTerbaru = data.last()
                            tampilkanRiwayat(riwayatTerbaru)
                        } else {
                            tampilkanBelumAdaRiwayat()
                        }
                    } else {
                        tampilkanBelumAdaRiwayat()
                    }
                }

                override fun onFailure(call: Call<List<RiwayatRekamMedisResponse>>, t: Throwable) {
                    tampilkanBelumAdaRiwayat()
                }
            })
    }

    private fun tampilkanRiwayat(data: RiwayatRekamMedisResponse) {
        riwayatTerpilih = data

        tvJudulRiwayat.text = data.nama_dokter
        tvSubRiwayat.text = data.spesialis
        tvTanggalRiwayat.text = data.tanggal_pemeriksaan

        btnLihat1.isEnabled = true
        btnLihat1.alpha = 1f

        btnLihat1.setOnClickListener {
            bukaDetailResep(data)
        }

        cardRiwayat1.setOnClickListener {
            bukaDetailResep(data)
        }
    }

    private fun tampilkanBelumAdaRiwayat() {
        riwayatTerpilih = null

        tvJudulRiwayat.text = "Belum ada riwayat pemeriksaan"
        tvSubRiwayat.text = "-"
        tvTanggalRiwayat.text = "-"

        btnLihat1.isEnabled = false
        btnLihat1.alpha = 0.5f

        cardRiwayat1.setOnClickListener {
            Toast.makeText(
                this,
                "Belum ada riwayat pemeriksaan",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun bukaDetailResep(data: RiwayatRekamMedisResponse) {
        val intent = Intent(this, detail_resep::class.java)
        intent.putExtra("id_rekam_medis", data.id_rekam_medis)
        intent.putExtra("tanggal", data.tanggal_pemeriksaan)
        intent.putExtra("diagnosa", data.diagnosa)
        intent.putExtra("kode_icd", data.kode_icd ?: "-")
        intent.putExtra("catatan", data.catatan ?: "-")
        intent.putExtra("dokter", data.nama_dokter)
        intent.putExtra("status", "Selesai")
        startActivity(intent)
    }
}