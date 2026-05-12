package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class jadwal : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jadwal)

        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navJadwal).setOnClickListener {
            startActivity(Intent(this, jadwal::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navAntrean).setOnClickListener {
            startActivity(Intent(this, FormAntreanActivity::class.java))
            finish()
        }

        findViewById<TextView>(R.id.navRiwayat).setOnClickListener {
            startActivity(Intent(this, riwayat::class.java))
            finish()
        }
    }
}