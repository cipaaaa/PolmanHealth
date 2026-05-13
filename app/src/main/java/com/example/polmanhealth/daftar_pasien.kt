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

        val namaAdmin = intent.getStringExtra("nama_admin") ?: "Admin"
        findViewById<TextView>(R.id.tvNamaAdmin).text = "Halo, $namaAdmin 👋"

        findViewById<TextView>(R.id.btnBack).setOnClickListener {
            startActivity(Intent(this, dashboard_admin::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.cardPasien1).setOnClickListener {
            bukaInputDiagnosa("Ahmad Rizki", "#A-012")
        }

        findViewById<LinearLayout>(R.id.cardPasien2).setOnClickListener {
            bukaInputDiagnosa("Naila Maharani", "#A-013")
        }

        findViewById<LinearLayout>(R.id.cardPasien3).setOnClickListener {
            bukaInputDiagnosa("Diva Putri", "#A-014")
        }
    }

    private fun bukaInputDiagnosa(nama: String, antrean: String) {
        val intent = Intent(this, InputDiagnosaActivity::class.java)
        intent.putExtra("nama_pasien", nama)
        intent.putExtra("nomor_antrean", antrean)
        startActivity(intent)
    }
}