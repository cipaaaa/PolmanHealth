package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class riwayat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        findViewById<LinearLayout>(R.id.cardRiwayat1).setOnClickListener {
            bukaDetailResep("Ahmad-Idar, MG.", "15 Mar 2024", "Selesai")
        }

        findViewById<LinearLayout>(R.id.cardRiwayat2).setOnClickListener {
            bukaDetailResep("Ana, San, MS.", "10 Mar 2024", "Selesai")
        }

        findViewById<LinearLayout>(R.id.cardRiwayat3).setOnClickListener {
            bukaDetailResep("Harrir Sandia, UD.", "05 Mar 2024", "Selesai")
        }

        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navJadwal).setOnClickListener {
            startActivity(Intent(this, jadwal::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navAntrean).setOnClickListener {
            startActivity(Intent(this, form_antrean::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navRiwayat).setOnClickListener {
            startActivity(Intent(this, riwayat::class.java))
            finish()
        }
    }

    private fun bukaDetailResep(dokter: String, tanggal: String, status: String) {
        val intent = Intent(this, detail_resep::class.java)
        intent.putExtra("dokter", dokter)
        intent.putExtra("tanggal", tanggal)
        intent.putExtra("status", status)
        startActivity(intent)
    }
}