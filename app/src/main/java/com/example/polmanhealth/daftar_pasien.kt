package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class daftar_pasien : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_pasien)

        val namaDokter = intent.getStringExtra("nama_dokter") ?: "Dokter"
        val poli = intent.getStringExtra("poli") ?: "Umum"

        findViewById<TextView>(R.id.tvNamaDokter).text = "Halo, $namaDokter 👋"
        findViewById<TextView>(R.id.tvPoliDokter).text = "Daftar pasien Poli $poli"

        val pasien1 = findViewById<LinearLayout>(R.id.cardPasien1)
        val pasien2 = findViewById<LinearLayout>(R.id.cardPasien2)
        val pasien3 = findViewById<LinearLayout>(R.id.cardPasien3)

        pasien1.setOnClickListener {
            pasien1.setBackgroundResource(R.drawable.bg_menu_active)
            bukaInputDiagnosa("Ahmad Rizki", "#A-012")
        }

        pasien2.setOnClickListener {
            pasien2.setBackgroundResource(R.drawable.bg_menu_active)
            bukaInputDiagnosa("Naila Maharani", "#A-013")
        }

        pasien3.setOnClickListener {
            pasien3.setBackgroundResource(R.drawable.bg_menu_active)
            bukaInputDiagnosa("Diva Putri", "#A-014")
        }
    }

    private fun bukaInputDiagnosa(nama: String, antrean: String) {
        val intent = Intent(this, input_diagnosa::class.java)
        intent.putExtra("nama_pasien", nama)
        intent.putExtra("nomor_antrean", antrean)
        startActivity(intent)
    }
}