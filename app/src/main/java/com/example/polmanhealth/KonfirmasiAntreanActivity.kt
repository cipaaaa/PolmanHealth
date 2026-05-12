package com.example.polmanhealth

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class KonfirmasiAntreanActivity : AppCompatActivity() {

    private lateinit var tvNomorAntrean: TextView
    private lateinit var tvTanggal: TextView
    private lateinit var tvWaktu: TextView
    private lateinit var tvPoli: TextView
    private lateinit var tvPasien: TextView
    private lateinit var tvBottomAntrean: TextView
    private lateinit var btnKembaliBeranda: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi)

        tvNomorAntrean = findViewById(R.id.tvNomorAntrean)
        tvTanggal = findViewById(R.id.tvTanggal)
        tvWaktu = findViewById(R.id.tvWaktu)
        tvPoli = findViewById(R.id.tvPoli)
        tvPasien = findViewById(R.id.tvPasien)
        tvBottomAntrean = findViewById(R.id.tvBottomAntrean)
        btnKembaliBeranda = findViewById(R.id.btnKembaliBeranda)

        val nomorAntrean = intent.getStringExtra("nomorAntrean") ?: "#A-013"
        val tanggal = intent.getStringExtra("tanggal") ?: "Jumat, 15 Maret 2024"
        val waktu = intent.getStringExtra("waktu") ?: "09:00 WIB"
        val poli = intent.getStringExtra("poli") ?: "Poli Umum"
        val namaPasien = intent.getStringExtra("namaPasien") ?: "Ahmad"
        val nomorPasien = intent.getStringExtra("nomorPasien") ?: "2103401"

        tvNomorAntrean.text = nomorAntrean
        tvTanggal.text = tanggal
        tvWaktu.text = waktu
        tvPoli.text = poli
        tvPasien.text = "$namaPasien · $nomorPasien"
        tvBottomAntrean.text = nomorAntrean.replace("#A-", "#")

        btnKembaliBeranda.setOnClickListener {
            finish()
        }
    }
}