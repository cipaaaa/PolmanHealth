package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.AdminLoginResponse
import com.example.polmanhealth.model.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            if (email == "admin@gmail.com") {
                loginAdmin(email, password)
            } else {
                loginPasien(email, password)
            }
        }

        tvToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginPasien(email: String, password: String) {
        RetrofitClient.instance.loginPasien(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val pasien = response.body()!!

                        Toast.makeText(
                            this@LoginActivity,
                            "Login berhasil: ${pasien.nama_pasien}",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@LoginActivity, dashboard::class.java)
                        intent.putExtra("id_pasien", pasien.id_pasien)
                        intent.putExtra("nama_pasien", pasien.nama_pasien)
                        intent.putExtra("email", pasien.email)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email atau password pasien salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Gagal konek ke server: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun loginAdmin(email: String, password: String) {
        RetrofitClient.instance.loginAdmin(email, password)
            .enqueue(object : Callback<AdminLoginResponse> {
                override fun onResponse(
                    call: Call<AdminLoginResponse>,
                    response: Response<AdminLoginResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login admin berhasil",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@LoginActivity, dashboard_admin::class.java)
                        intent.putExtra("email", response.body()!!.email)
                        intent.putExtra("role", response.body()!!.role)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email atau password admin salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<AdminLoginResponse>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Gagal konek ke server: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}