package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class daftar_obat : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_obat)

        findViewById<TextView>(R.id.btnBackObat).setOnClickListener {
            startActivity(Intent(this, dashboard_admin::class.java))
            finish()
        }

        Toast.makeText(
            this,
            "Peringatan: stok Salep Luka menipis",
            Toast.LENGTH_LONG
        ).show()
    }
}