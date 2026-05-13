package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // BACK BUTTON
        findViewById<TextView>(R.id.btnBack).setOnClickListener {
            finish()
        }

        // EDIT PROFIL
        findViewById<TextView>(R.id.btnEditProfile).setOnClickListener {
            val alamat = findViewById<EditText>(R.id.etAlamat)
            alamat.isEnabled = true
            alamat.requestFocus()
            alamat.setSelection(alamat.text.length)
        }

        // LOGOUT
        findViewById<TextView>(R.id.btnLogout).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}