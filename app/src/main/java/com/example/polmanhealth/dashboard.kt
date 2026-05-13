package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val nama = intent.getStringExtra("nama") ?: "Ahmad"

        findViewById<TextView>(R.id.tvNamaUser).text = "Halo, $nama 👋"
        findViewById<TextView>(R.id.tvStatusUser).text = "Masyarakat Umum"

        // MENU GRID (Sesuaikan dengan nama di Manifest)
        findViewById<LinearLayout>(R.id.menuFormAntrean).setOnClickListener {
            startActivity(Intent(this, FormAntreanActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.menuJadwal).setOnClickListener {
            startActivity(Intent(this, jadwal::class.java)) // GANTI: JadwalActivity -> jadwal
        }

        findViewById<LinearLayout>(R.id.menuRiwayat).setOnClickListener {
            startActivity(Intent(this, riwayat::class.java)) // GANTI: RiwayatActivity -> riwayat
        }

        findViewById<LinearLayout>(R.id.menuProfil).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // BOTTOM NAV (Navigasi Bawah)
        findViewById<TextView>(R.id.navHome).setOnClickListener {
            // Sudah di home
        }

        findViewById<TextView>(R.id.navJadwal).setOnClickListener {
            startActivity(Intent(this, jadwal::class.java)) // GANTI: JadwalActivity -> jadwal
        }

        findViewById<TextView>(R.id.navAntrean).setOnClickListener {
            startActivity(Intent(this, FormAntreanActivity::class.java))
        }

        findViewById<TextView>(R.id.navRiwayat).setOnClickListener {
            startActivity(Intent(this, riwayat::class.java)) // GANTI: RiwayatActivity -> riwayat
        }

        findViewById<TextView>(R.id.navProfil).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}