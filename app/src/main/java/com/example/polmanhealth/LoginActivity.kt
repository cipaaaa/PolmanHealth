package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etLoginEmail: EditText
    private lateinit var etLoginPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvToRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etLoginEmail = findViewById(R.id.etLoginEmail)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvToRegister = findViewById(R.id.tvToRegister)

        btnLogin.setOnClickListener {
            val email = etLoginEmail.text.toString()
            val password = etLoginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("emailPasien", email)
                startActivity(intent)
                finish()
            }
        }

        tvToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}