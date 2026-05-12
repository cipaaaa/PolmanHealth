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

        val menuForm = findViewById<LinearLayout>(R.id.menuFormAntrean)
        val menuJadwal = findViewById<LinearLayout>(R.id.menuJadwal)
        val menuRiwayat = findViewById<LinearLayout>(R.id.menuRiwayat)
        val menuProfil = findViewById<LinearLayout>(R.id.menuProfil)

        menuForm.setOnClickListener {
            menuForm.setBackgroundResource(R.drawable.bg_menu_active)
            startActivity(Intent(this, form_antrean::class.java))
        }

        menuJadwal.setOnClickListener {
            menuJadwal.setBackgroundResource(R.drawable.bg_menu_active)
            startActivity(Intent(this, jadwal::class.java))
        }

        menuRiwayat.setOnClickListener {
            menuRiwayat.setBackgroundResource(R.drawable.bg_menu_active)
            startActivity(Intent(this, riwayat::class.java))
        }

        menuProfil.setOnClickListener {
            menuProfil.setBackgroundResource(R.drawable.bg_menu_active)
        }

        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navJadwal).setOnClickListener {
            startActivity(Intent(this, jadwal::class.java))
        }

        findViewById<TextView>(R.id.navAntrean).setOnClickListener {
            startActivity(Intent(this, form_antrean::class.java))
        }

        findViewById<TextView>(R.id.navRiwayat).setOnClickListener {
            startActivity(Intent(this, riwayat::class.java))
        }
    }
}