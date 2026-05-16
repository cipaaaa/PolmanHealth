package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.PasienResponse
import com.example.polmanhealth.model.PasienUpdateRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.activity.OnBackPressedCallback

class ProfileActivity : AppCompatActivity() {

    private lateinit var etNamaProfile: EditText
    private lateinit var etEmailProfile: EditText
    private lateinit var etPhoneProfile: EditText
    private lateinit var etAlamat: EditText
    private lateinit var btnEditProfile: TextView

    private var sedangEdit = false
    private var idPasien = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val intent = Intent(this@ProfileActivity, dashboard::class.java)
                    startActivity(intent)

                    overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )

                    finish()
                }
            }
        )

        etNamaProfile = findViewById(R.id.etNamaProfile)
        etEmailProfile = findViewById(R.id.etEmailProfile)
        etPhoneProfile = findViewById(R.id.etPhoneProfile)
        etAlamat = findViewById(R.id.etAlamat)
        btnEditProfile = findViewById(R.id.btnEditProfile)

        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)

        idPasien = sharedPref.getInt("id_pasien", 0)

        etNamaProfile.setText(sharedPref.getString("nama_pasien", "Pasien"))
        etEmailProfile.setText(sharedPref.getString("email", "-"))
        etPhoneProfile.setText(sharedPref.getString("no_telp", "-"))
        etAlamat.setText(sharedPref.getString("alamat", "-"))

        setModeEdit(false)

        btnEditProfile.setOnClickListener {
            if (!sedangEdit) {
                setModeEdit(true)
            } else {
                simpanPerubahan()
            }
        }

        findViewById<TextView>(R.id.btnLogout).setOnClickListener {
            sharedPref.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun setModeEdit(aktif: Boolean) {
        sedangEdit = aktif

        etNamaProfile.isEnabled = aktif
        etEmailProfile.isEnabled = aktif
        etPhoneProfile.isEnabled = aktif
        etAlamat.isEnabled = aktif

        btnEditProfile.text = if (aktif) {
            "Simpan Perubahan"
        } else {
            "Edit Profil"
        }

        if (aktif) {
            etNamaProfile.requestFocus()
            etNamaProfile.setSelection(etNamaProfile.text.length)
        }
    }

    private fun simpanPerubahan() {
        val nama = etNamaProfile.text.toString().trim()
        val email = etEmailProfile.text.toString().trim()
        val noTelp = etPhoneProfile.text.toString().trim()
        val alamat = etAlamat.text.toString().trim()

        if (idPasien == 0) {
            Toast.makeText(this, "Session pasien tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        if (nama.isEmpty() || email.isEmpty() || noTelp.isEmpty() || alamat.isEmpty()) {
            Toast.makeText(this, "Semua data profil harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val request = PasienUpdateRequest(
            nama_pasien = nama,
            email = email,
            no_telp = noTelp,
            alamat = alamat
        )

        RetrofitClient.instance.updatePasien(idPasien, request)
            .enqueue(object : Callback<PasienResponse> {
                override fun onResponse(
                    call: Call<PasienResponse>,
                    response: Response<PasienResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val pasien = response.body()!!

                        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
                        sharedPref.edit()
                            .putString("nama_pasien", pasien.nama_pasien)
                            .putString("email", pasien.email)
                            .putString("no_telp", pasien.no_telp ?: "-")
                            .putString("alamat", pasien.alamat ?: "-")
                            .apply()

                        etNamaProfile.setText(pasien.nama_pasien)
                        etEmailProfile.setText(pasien.email)
                        etPhoneProfile.setText(pasien.no_telp ?: "-")
                        etAlamat.setText(pasien.alamat ?: "-")

                        Toast.makeText(
                            this@ProfileActivity,
                            "Profil berhasil diperbarui",
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@ProfileActivity, dashboard::class.java)
                        startActivity(intent)

                        overridePendingTransition(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right
                        )

                        finish()

                    } else {
                        Toast.makeText(
                            this@ProfileActivity,
                            "Gagal memperbarui profil: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<PasienResponse>, t: Throwable) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "Gagal terhubung ke server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}