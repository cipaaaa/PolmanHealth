package com.example.polmanhealth

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.PasienResponse
import com.example.polmanhealth.model.RegisterRequest

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etNama = findViewById<EditText>(R.id.etNama)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegister)
        val etNoTelp = findViewById<EditText>(R.id.etNoTelp)
        val etAlamat = findViewById<EditText>(R.id.etAlamat)

        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {

            val request = RegisterRequest(
                nama_pasien = etNama.text.toString(),
                email = etEmail.text.toString(),
                password = etPassword.text.toString(),
                no_telp = etNoTelp.text.toString(),
                alamat = etAlamat.text.toString()
            )

            RetrofitClient.instance.registerPasien(request)
                .enqueue(object : Callback<PasienResponse> {

                    override fun onResponse(
                        call: Call<PasienResponse>,
                        response: Response<PasienResponse>
                    ) {

                        if (response.isSuccessful) {

                            Toast.makeText(
                                this@RegisterActivity,
                                "Register Berhasil",
                                Toast.LENGTH_SHORT
                            ).show()

                        } else {

                            Log.d(
                                "API_ERROR",
                                response.errorBody()?.string().toString()
                            )

                            Toast.makeText(
                                this@RegisterActivity,
                                "Register Gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<PasienResponse>,
                        t: Throwable
                    ) {

                        Log.d("API_FAILURE", t.message.toString())

                        Toast.makeText(
                            this@RegisterActivity,
                            t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }
}