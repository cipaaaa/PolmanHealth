package com.example.polmanhealth

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.DetailResepObatResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class detail_resep : AppCompatActivity() {

    private lateinit var layoutDetailResep: LinearLayout
    private lateinit var tvKosongResep: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_resep)

        val idRekamMedis = intent.getIntExtra("id_rekam_medis", 0)
        val dokter = intent.getStringExtra("dokter") ?: "-"
        val tanggal = intent.getStringExtra("tanggal") ?: "-"
        val status = intent.getStringExtra("status") ?: "Selesai"
        val diagnosa = intent.getStringExtra("diagnosa") ?: "-"
        val catatan = intent.getStringExtra("catatan") ?: "-"

        layoutDetailResep = findViewById(R.id.layoutDetailResep)
        tvKosongResep = findViewById(R.id.tvKosongResep)

        findViewById<TextView>(R.id.tvDokterDetail).text = dokter
        findViewById<TextView>(R.id.tvTanggalDetail).text = tanggal
        findViewById<TextView>(R.id.tvStatusDetail).text = status

        findViewById<TextView>(R.id.tvDiagnosaDetail).text =
            if (catatan != "-" && catatan.isNotEmpty()) {
                catatan
            } else {
                diagnosa
            }

        if (idRekamMedis != 0) {
            loadDetailResep(idRekamMedis)
        } else {
            tvKosongResep.text = "ID rekam medis tidak ditemukan"
        }
    }

    private fun loadDetailResep(idRekamMedis: Int) {
        RetrofitClient.instance.getDetailResepByRekamMedis(idRekamMedis)
            .enqueue(object : Callback<List<DetailResepObatResponse>> {
                override fun onResponse(
                    call: Call<List<DetailResepObatResponse>>,
                    response: Response<List<DetailResepObatResponse>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val dataResep = response.body()!!

                        if (dataResep.isEmpty()) {
                            tvKosongResep.visibility = View.VISIBLE
                            tvKosongResep.text = "Belum ada resep obat"
                        } else {
                            tvKosongResep.visibility = View.GONE
                            layoutDetailResep.removeAllViews()

                            dataResep.forEach { resep ->
                                tambahCardObat(resep)
                            }
                        }
                    } else {
                        tvKosongResep.visibility = View.VISIBLE
                        tvKosongResep.text = "Gagal mengambil resep"
                    }
                }

                override fun onFailure(call: Call<List<DetailResepObatResponse>>, t: Throwable) {
                    Toast.makeText(
                        this@detail_resep,
                        "Gagal terhubung ke server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun tambahCardObat(resep: DetailResepObatResponse) {
        val card = LinearLayout(this)
        card.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(74)
        ).apply {
            bottomMargin = dpToPx(12)
        }

        card.setBackgroundResource(R.drawable.bg_card_medicine)
        card.gravity = Gravity.CENTER_VERTICAL
        card.orientation = LinearLayout.HORIZONTAL
        card.setPadding(dpToPx(14), 0, dpToPx(14), 0)

        val icon = TextView(this)
        icon.layoutParams = LinearLayout.LayoutParams(
            dpToPx(38),
            dpToPx(38)
        )
        icon.setBackgroundResource(R.drawable.bg_icon_green_light)
        icon.gravity = Gravity.CENTER
        icon.text = "💊"
        icon.textSize = 18f

        val obat = TextView(this)
        obat.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        ).apply {
            leftMargin = dpToPx(12)
        }
        obat.text = "${resep.nama_obat}\n${resep.jenis_obat}"
        obat.setTextColor(Color.parseColor("#18382F"))
        obat.textSize = 13f
        obat.setTypeface(null, android.graphics.Typeface.BOLD)

        val dosis = TextView(this)
        dosis.layoutParams = LinearLayout.LayoutParams(
            dpToPx(64),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        dosis.gravity = Gravity.CENTER
        dosis.text = resep.dosis
        dosis.setTextColor(Color.parseColor("#18382F"))
        dosis.textSize = 12f
        dosis.setTypeface(null, android.graphics.Typeface.BOLD)

        val aturan = TextView(this)
        aturan.layoutParams = LinearLayout.LayoutParams(
            dpToPx(86),
            dpToPx(32)
        )
        aturan.setBackgroundResource(R.drawable.bg_status_grey)
        aturan.gravity = Gravity.CENTER
        aturan.text = resep.aturan_pakai.replace(" ", "\n")
        aturan.setTextColor(Color.parseColor("#444444"))
        aturan.textSize = 10f
        aturan.setTypeface(null, android.graphics.Typeface.BOLD)

        card.addView(icon)
        card.addView(obat)
        card.addView(dosis)
        card.addView(aturan)

        layoutDetailResep.addView(card)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}