package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
            val email = etLoginEmail.text.toString().trim().lowercase()
            val password = etLoginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (email == "admin@gmail.com" && password == "admin123") {
                Toast.makeText(this, "Login sebagai Admin", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, dashboard_admin::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Login sebagai User", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, dashboard::class.java)
                intent.putExtra("nama", "Ahmad")
                startActivity(intent)
                finish()
            }
        }

        tvToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}