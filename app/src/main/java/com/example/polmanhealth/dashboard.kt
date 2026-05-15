package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.PendaftaranResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class dashboard : AppCompatActivity() {

    private lateinit var tvStatusAntrean: TextView
    private lateinit var tvTanggalAntrean: TextView
    private lateinit var tvPoliAntrean: TextView
    private lateinit var tvNomorAntreanAktif: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val nama = sharedPref.getString("nama_pasien", "Pasien")
        val idPasien = sharedPref.getInt("id_pasien", 0)

        findViewById<TextView>(R.id.tvNamaUser).text = "Halo, $nama 👋"
        findViewById<TextView>(R.id.tvStatusUser).text = "Masyarakat Umum"

        tvStatusAntrean = findViewById(R.id.tvStatusAntrean)
        tvTanggalAntrean = findViewById(R.id.tvTanggalAntrean)
        tvPoliAntrean = findViewById(R.id.tvPoliAntrean)
        tvNomorAntreanAktif = findViewById(R.id.tvNomorAntreanAktif)

        loadAntreanAktif(idPasien)

        findViewById<LinearLayout>(R.id.menuFormAntrean).setOnClickListener {
            startActivity(Intent(this, FormAntreanActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.menuJadwal).setOnClickListener {
            startActivity(Intent(this, jadwal::class.java))
        }

        findViewById<LinearLayout>(R.id.menuRiwayat).setOnClickListener {
            startActivity(Intent(this, riwayat::class.java))
        }

        findViewById<LinearLayout>(R.id.menuProfil).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        findViewById<TextView>(R.id.navHome).setOnClickListener {
            // Sudah di home
        }

        findViewById<TextView>(R.id.navJadwal).setOnClickListener {
            startActivity(Intent(this, jadwal::class.java))
        }

        findViewById<TextView>(R.id.navAntrean).setOnClickListener {
            startActivity(Intent(this, FormAntreanActivity::class.java))
        }

        findViewById<TextView>(R.id.navRiwayat).setOnClickListener {
            startActivity(Intent(this, riwayat::class.java))
        }

        findViewById<TextView>(R.id.navProfil).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val idPasien = sharedPref.getInt("id_pasien", 0)

        loadAntreanAktif(idPasien)
    }

    private fun loadAntreanAktif(idPasien: Int) {
        if (idPasien == 0) {
            tampilkanBelumAdaAntrean()
            return
        }

        RetrofitClient.instance.getAntreanAktif(idPasien)
            .enqueue(object : Callback<PendaftaranResponse> {
                override fun onResponse(
                    call: Call<PendaftaranResponse>,
                    response: Response<PendaftaranResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val antrean = response.body()!!

                        tvStatusAntrean.text = "Antrean Aktif"
                        tvTanggalAntrean.text = antrean.tanggal
                        tvPoliAntrean.text = getPoliByDokter(antrean.id_dokter)
                        tvNomorAntreanAktif.text = antrean.nomor_antrean.toString()
                    } else {
                        tampilkanBelumAdaAntrean()
                    }
                }

                override fun onFailure(call: Call<PendaftaranResponse>, t: Throwable) {
                    tampilkanBelumAdaAntrean()
                }
            })
    }

    private fun tampilkanBelumAdaAntrean() {
        tvStatusAntrean.text = "Belum ada antrean"
        tvTanggalAntrean.text = "-"
        tvPoliAntrean.text = "-"
        tvNomorAntreanAktif.text = "-"
    }

    private fun getPoliByDokter(idDokter: Int): String {
        return when (idDokter) {
            1 -> "Dokter Umum"
            2 -> "Dokter Gigi"
            3 -> "Dokter Anak"
            4 -> "Dokter Kulit"
            5 -> "Dokter THT"
            else -> "Dokter"
        }
    }
}