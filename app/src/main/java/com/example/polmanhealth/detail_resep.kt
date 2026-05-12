package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class detail_resep : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_resep)

        val dokter = intent.getStringExtra("dokter") ?: "Ahmad-Idar, MG."
        val tanggal = intent.getStringExtra("tanggal") ?: "15 Mar 2024"
        val status = intent.getStringExtra("status") ?: "Selesai"

        findViewById<TextView>(R.id.tvDokterDetail).text = dokter
        findViewById<TextView>(R.id.tvTanggalDetail).text = tanggal
        findViewById<TextView>(R.id.tvStatusDetail).text = status

        findViewById<TextView>(R.id.btnBackDetail).setOnClickListener {
            startActivity(Intent(this, riwayat::class.java))
            finish()
        }
    }
}