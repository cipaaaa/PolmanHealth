package com.example.polmanhealth

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.AdminAntreanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class daftar_pasien : AppCompatActivity() {

    private lateinit var layoutDaftarPasien: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_pasien)

        layoutDaftarPasien = findViewById(R.id.layoutDaftarPasien)
        loadDaftarAntrean()
    }

    private fun loadDaftarAntrean() {
        RetrofitClient.instance.getDaftarAntreanAdmin()
            .enqueue(object : Callback<List<AdminAntreanResponse>> {
                override fun onResponse(
                    call: Call<List<AdminAntreanResponse>>,
                    response: Response<List<AdminAntreanResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val dataAntrean = response.body()!!
                        layoutDaftarPasien.removeAllViews()

                        if (dataAntrean.isEmpty()) {
                            tampilkanKosong()
                        } else {
                            dataAntrean.forEach { antrean ->
                                tambahCardPasien(antrean)
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@daftar_pasien,
                            "Gagal mengambil daftar antrean",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<AdminAntreanResponse>>, t: Throwable) {
                    Toast.makeText(
                        this@daftar_pasien,
                        "Gagal terhubung ke server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun tambahCardPasien(antrean: AdminAntreanResponse) {
        val card = LinearLayout(this)
        card.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(100)
        ).apply {
            topMargin = dpToPx(14)
        }

        card.setBackgroundResource(R.drawable.bg_card_strong)
        card.elevation = dpToPx(8).toFloat()
        card.gravity = Gravity.CENTER_VERTICAL
        card.orientation = LinearLayout.HORIZONTAL
        card.setPadding(dpToPx(18), dpToPx(18), dpToPx(18), dpToPx(18))

        val icon = TextView(this)
        icon.layoutParams = LinearLayout.LayoutParams(dpToPx(54), dpToPx(54))
        icon.setBackgroundResource(R.drawable.bg_icon_green_light)
        icon.gravity = Gravity.CENTER
        icon.text = "👤"
        icon.textSize = 24f

        val info = LinearLayout(this)
        info.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        ).apply {
            leftMargin = dpToPx(16)
        }
        info.orientation = LinearLayout.VERTICAL

        val nama = TextView(this)
        nama.text = antrean.nama_pasien
        nama.setTextColor(android.graphics.Color.parseColor("#18382F"))
        nama.textSize = 17f
        nama.setTypeface(null, android.graphics.Typeface.BOLD)

        val poli = TextView(this)
        poli.text = "${antrean.spesialis} - ${antrean.nama_dokter}"
        poli.setTextColor(android.graphics.Color.parseColor("#7A9389"))
        poli.textSize = 13f
        poli.setTypeface(null, android.graphics.Typeface.BOLD)

        info.addView(nama)
        info.addView(poli)

        val nomor = TextView(this)
        nomor.layoutParams = LinearLayout.LayoutParams(dpToPx(76), dpToPx(36))
        nomor.setBackgroundResource(R.drawable.bg_status_green)
        nomor.gravity = Gravity.CENTER
        nomor.text = antrean.nomor_antrean.toString()
        nomor.setTextColor(android.graphics.Color.parseColor("#08785B"))
        nomor.textSize = 14f
        nomor.setTypeface(null, android.graphics.Typeface.BOLD)

        card.addView(icon)
        card.addView(info)
        card.addView(nomor)

        card.setOnClickListener {
            bukaInputDiagnosa(antrean)
        }

        layoutDaftarPasien.addView(card)
    }

    private fun tampilkanKosong() {
        val textKosong = TextView(this)
        textKosong.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dpToPx(20)
        }

        textKosong.text = "Belum ada antrean pasien"
        textKosong.gravity = Gravity.CENTER
        textKosong.setTextColor(android.graphics.Color.parseColor("#7A9389"))
        textKosong.textSize = 15f
        textKosong.setTypeface(null, android.graphics.Typeface.BOLD)

        layoutDaftarPasien.addView(textKosong)
    }

    private fun bukaInputDiagnosa(antrean: AdminAntreanResponse) {
        val intent = Intent(this, InputDiagnosaActivity::class.java)
        intent.putExtra("id_pendaftaran", antrean.id_pendaftaran)
        intent.putExtra("nama_pasien", antrean.nama_pasien)
        intent.putExtra("nomor_antrean", antrean.nomor_antrean.toString())
        intent.putExtra("spesialis", antrean.spesialis)
        intent.putExtra("nama_dokter", antrean.nama_dokter)
        startActivity(intent)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}