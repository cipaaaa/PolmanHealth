package com.example.polmanhealth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class jadwal : AppCompatActivity() {

    private lateinit var btnSenin: TextView
    private lateinit var btnSelasa: TextView
    private lateinit var btnRabu: TextView
    private lateinit var btnKamis: TextView
    private lateinit var btnJumat: TextView

    private lateinit var txtNamaDokter: List<TextView>
    private lateinit var txtSpesialis: List<TextView>
    private lateinit var txtJam: List<TextView>

    private val client = OkHttpClient()

    // Kalau pakai emulator Android Studio
    private val baseUrl = "http://10.0.2.2:8000"

    // Kalau pakai HP asli, ganti jadi IP laptop kamu:
    // private val baseUrl = "http://192.168.1.20:8000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

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

        btnSenin = findViewById(R.id.btnSenin)
        btnSelasa = findViewById(R.id.btnSelasa)
        btnRabu = findViewById(R.id.btnRabu)
        btnKamis = findViewById(R.id.btnKamis)
        btnJumat = findViewById(R.id.btnJumat)

        val listHari = listOf(btnSenin, btnSelasa, btnRabu, btnKamis, btnJumat)

        txtNamaDokter = listOf(
            findViewById(R.id.txtNamaDokter1),
            findViewById(R.id.txtNamaDokter2),
            findViewById(R.id.txtNamaDokter3),
            findViewById(R.id.txtNamaDokter4),
            findViewById(R.id.txtNamaDokter5)
        )

        txtSpesialis = listOf(
            findViewById(R.id.txtSpesialis1),
            findViewById(R.id.txtSpesialis2),
            findViewById(R.id.txtSpesialis3),
            findViewById(R.id.txtSpesialis4),
            findViewById(R.id.txtSpesialis5)
        )

        txtJam = listOf(
            findViewById(R.id.txtJam1),
            findViewById(R.id.txtJam2),
            findViewById(R.id.txtJam3),
            findViewById(R.id.txtJam4),
            findViewById(R.id.txtJam5)
        )

        fun pilihHari(tombol: TextView, hari: String) {
            listHari.forEach {
                it.setBackgroundResource(R.drawable.bg_time_outline)
                it.setTextColor(Color.parseColor("#346C57"))
            }

            tombol.setBackgroundResource(R.drawable.bg_time_active)
            tombol.setTextColor(Color.WHITE)

            loadJadwalDokter(hari)
        }

        btnSenin.setOnClickListener { pilihHari(btnSenin, "Senin") }
        btnSelasa.setOnClickListener { pilihHari(btnSelasa, "Selasa") }
        btnRabu.setOnClickListener { pilihHari(btnRabu, "Rabu") }
        btnKamis.setOnClickListener { pilihHari(btnKamis, "Kamis") }
        btnJumat.setOnClickListener { pilihHari(btnJumat, "Jumat") }

        pilihHari(btnSenin, "Senin")
    }

    private fun loadJadwalDokter(hari: String) {
        val request = Request.Builder()
            .url("$baseUrl/jadwal-dokter/hari/$hari")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    txtNamaDokter[0].text = "Gagal mengambil data"
                    txtSpesialis[0].text = "Cek koneksi API"
                    txtJam[0].text = "-"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string() ?: return

                runOnUiThread {
                    try {
                        val jsonArray = JSONArray(body)

                        for (i in 0 until 5) {
                            if (i < jsonArray.length()) {
                                val item = jsonArray.getJSONObject(i)

                                if (item.has("nama_dokter")) {
                                    txtNamaDokter[i].text = item.getString("nama_dokter")
                                }

                                if (item.has("spesialis")) {
                                    txtSpesialis[i].text = item.getString("spesialis")
                                }

                                val jamMulai = item.getString("jam_mulai").substring(0, 5)
                                val jamSelesai = item.getString("jam_selesai").substring(0, 5)

                                txtJam[i].text = "◷ $jamMulai–$jamSelesai"
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


            }
        })
    }
}