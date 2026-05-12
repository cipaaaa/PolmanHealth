package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val nama = intent.getStringExtra("nama") ?: "Ahmad"

        findViewById<TextView>(R.id.tvNamaUser).text = "Halo, $nama 👋"
        findViewById<TextView>(R.id.tvStatusUser).text = "Masyarakat Umum"

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
            Toast.makeText(this, "Fitur profil belum tersedia", Toast.LENGTH_SHORT).show()
        }

        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
            finish()
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
    }
}