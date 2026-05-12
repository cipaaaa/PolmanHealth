package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class dashboard_admin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)

        val menuDaftarPasien = findViewById<LinearLayout>(R.id.menuDaftarPasien)
        val menuDaftarObat = findViewById<LinearLayout>(R.id.menuDaftarObat)

        menuDaftarPasien.setOnClickListener {
            menuDaftarPasien.setBackgroundResource(R.drawable.bg_menu_active)
            startActivity(Intent(this, daftar_pasien::class.java))
        }

        menuDaftarObat.setOnClickListener {
            menuDaftarObat.setBackgroundResource(R.drawable.bg_menu_active)
            startActivity(Intent(this, daftar_obat::class.java))
        }
    }
}