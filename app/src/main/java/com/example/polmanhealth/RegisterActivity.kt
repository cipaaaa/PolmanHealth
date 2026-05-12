package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNama: EditText
    private lateinit var etPasswordRegister: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etNoTelp: EditText

    private lateinit var btnRegister: Button
    private lateinit var tvToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNama = findViewById(R.id.etNama)
        etPasswordRegister = findViewById(R.id.etPasswordRegister)
        etAlamat = findViewById(R.id.etAlamat)
        etNoTelp = findViewById(R.id.etNoTelp)

        btnRegister = findViewById(R.id.btnRegister)
        tvToLogin = findViewById(R.id.tvToLogin)

        btnRegister.setOnClickListener {

            val nama = etNama.text.toString()
            val password = etPasswordRegister.text.toString()
            val alamat = etAlamat.text.toString()
            val noTelp = etNoTelp.text.toString()

            if (nama.isEmpty() ||
                password.isEmpty() ||
                alamat.isEmpty() ||
                noTelp.isEmpty()
            ) {

                Toast.makeText(
                    this,
                    "Semua data harus diisi",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Registrasi berhasil",
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this, LoginActivity::class.java)

                intent.putExtra("nama", nama)

                startActivity(intent)
                finish()
            }
        }

        tvToLogin.setOnClickListener {

            startActivity(
                Intent(this, LoginActivity::class.java)
            )

            finish()
        }
    }
}