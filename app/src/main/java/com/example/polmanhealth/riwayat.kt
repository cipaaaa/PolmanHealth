package com.example.polmanhealth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.RiwayatRekamMedisResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class riwayat : AppCompatActivity() {

    private lateinit var layoutRiwayatMedis: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        layoutRiwayatMedis = findViewById(R.id.layoutRiwayatMedis)

        loadRiwayatMedis()

        findViewById<TextView>(R.id.navHome).setOnClickListener {
            pindahHalaman(Intent(this, dashboard::class.java), 0)
        }

        findViewById<TextView>(R.id.navJadwal).setOnClickListener {
            pindahHalaman(Intent(this, jadwal::class.java), 1)
        }

        findViewById<TextView>(R.id.navAntrean).setOnClickListener {
            pindahHalaman(Intent(this, FormAntreanActivity::class.java), 2)
        }

        findViewById<TextView>(R.id.navRiwayat).setOnClickListener {
            // Sudah di Riwayat
        }

        findViewById<TextView>(R.id.navProfil).setOnClickListener {
            pindahHalaman(Intent(this, ProfileActivity::class.java), 4)
        }
    }

    private fun pindahHalaman(intent: Intent, targetIndex: Int) {
        val currentIndex = 3 // Riwayat

        startActivity(intent)

        if (targetIndex > currentIndex) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        finish()
    }

    private fun loadRiwayatMedis() {
        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val idPasien = sharedPref.getInt("id_pasien", 0)

        if (idPasien == 0) {
            tampilkanBelumAdaRiwayat()
            return
        }

        RetrofitClient.instance.getRiwayatRekamMedis(idPasien)
            .enqueue(object : Callback<List<RiwayatRekamMedisResponse>> {
                override fun onResponse(
                    call: Call<List<RiwayatRekamMedisResponse>>,
                    response: Response<List<RiwayatRekamMedisResponse>>
                ) {
                    layoutRiwayatMedis.removeAllViews()

                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!

                        if (data.isEmpty()) {
                            tampilkanBelumAdaRiwayat()
                        } else {
                            data.reversed().forEach { riwayat ->
                                tambahCardRiwayat(riwayat)
                            }
                        }
                    } else {
                        tampilkanBelumAdaRiwayat()
                    }
                }

                override fun onFailure(
                    call: Call<List<RiwayatRekamMedisResponse>>,
                    t: Throwable
                ) {
                    layoutRiwayatMedis.removeAllViews()
                    tampilkanBelumAdaRiwayat()
                }
            })
    }

    private fun tambahCardRiwayat(data: RiwayatRekamMedisResponse) {
        val card = LinearLayout(this)
        card.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(145)
        ).apply {
            topMargin = dpToPx(14)
        }

        card.setBackgroundResource(R.drawable.bg_card_strong)
        card.elevation = dpToPx(8).toFloat()
        card.gravity = Gravity.CENTER_VERTICAL
        card.orientation = LinearLayout.HORIZONTAL
        card.setPadding(dpToPx(18), dpToPx(18), dpToPx(18), dpToPx(18))

        val icon = TextView(this)
        icon.layoutParams = LinearLayout.LayoutParams(dpToPx(64), dpToPx(64))
        icon.setBackgroundResource(R.drawable.bg_icon_green_light)
        icon.gravity = Gravity.CENTER
        icon.text = "🩺"
        icon.textSize = 28f

        val info = LinearLayout(this)
        info.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        ).apply {
            leftMargin = dpToPx(18)
        }
        info.orientation = LinearLayout.VERTICAL

        val judul = TextView(this)
        judul.text = data.nama_dokter
        judul.setTextColor(Color.parseColor("#18382F"))
        judul.textSize = 18f
        judul.setTypeface(null, android.graphics.Typeface.BOLD)

        val sub = TextView(this)
        sub.text = data.spesialis
        sub.setTextColor(Color.parseColor("#7A9389"))
        sub.textSize = 14f
        sub.setTypeface(null, android.graphics.Typeface.BOLD)

        val tanggal = TextView(this)
        tanggal.text = data.tanggal_pemeriksaan
        tanggal.setTextColor(Color.parseColor("#346C57"))
        tanggal.textSize = 13f

        info.addView(judul)
        info.addView(sub)
        info.addView(tanggal)

        val btnLihat = TextView(this)
        btnLihat.layoutParams = LinearLayout.LayoutParams(dpToPx(70), dpToPx(34))
        btnLihat.setBackgroundResource(R.drawable.bg_status_green)
        btnLihat.gravity = Gravity.CENTER
        btnLihat.text = "Lihat"
        btnLihat.setTextColor(Color.parseColor("#08785B"))
        btnLihat.textSize = 13f
        btnLihat.setTypeface(null, android.graphics.Typeface.BOLD)

        btnLihat.setOnClickListener {
            bukaDetailResep(data)
        }

        card.setOnClickListener {
            bukaDetailResep(data)
        }

        card.addView(icon)
        card.addView(info)
        card.addView(btnLihat)

        layoutRiwayatMedis.addView(card)
    }

    private fun tampilkanBelumAdaRiwayat() {
        val card = LinearLayout(this)
        card.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(145)
        ).apply {
            topMargin = dpToPx(14)
        }

        card.setBackgroundResource(R.drawable.bg_card_strong)
        card.elevation = dpToPx(8).toFloat()
        card.gravity = Gravity.CENTER_VERTICAL
        card.orientation = LinearLayout.HORIZONTAL
        card.setPadding(dpToPx(18), dpToPx(18), dpToPx(18), dpToPx(18))

        val icon = TextView(this)
        icon.layoutParams = LinearLayout.LayoutParams(dpToPx(64), dpToPx(64))
        icon.setBackgroundResource(R.drawable.bg_icon_green_light)
        icon.gravity = Gravity.CENTER
        icon.text = "🩺"
        icon.textSize = 28f

        val info = LinearLayout(this)
        info.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        ).apply {
            leftMargin = dpToPx(18)
        }
        info.orientation = LinearLayout.VERTICAL

        val judul = TextView(this)
        judul.text = "Belum ada riwayat pemeriksaan"
        judul.setTextColor(Color.parseColor("#18382F"))
        judul.textSize = 18f
        judul.setTypeface(null, android.graphics.Typeface.BOLD)

        val sub = TextView(this)
        sub.text = "-"
        sub.setTextColor(Color.parseColor("#7A9389"))
        sub.textSize = 14f
        sub.setTypeface(null, android.graphics.Typeface.BOLD)

        val tanggal = TextView(this)
        tanggal.text = "-"
        tanggal.setTextColor(Color.parseColor("#346C57"))
        tanggal.textSize = 13f

        info.addView(judul)
        info.addView(sub)
        info.addView(tanggal)

        val btnLihat = TextView(this)
        btnLihat.layoutParams = LinearLayout.LayoutParams(dpToPx(70), dpToPx(34))
        btnLihat.setBackgroundResource(R.drawable.bg_status_green)
        btnLihat.gravity = Gravity.CENTER
        btnLihat.text = "Lihat"
        btnLihat.alpha = 0.5f
        btnLihat.isEnabled = false
        btnLihat.setTextColor(Color.parseColor("#08785B"))
        btnLihat.textSize = 13f
        btnLihat.setTypeface(null, android.graphics.Typeface.BOLD)

        card.addView(icon)
        card.addView(info)
        card.addView(btnLihat)

        layoutRiwayatMedis.addView(card)
    }

    private fun bukaDetailResep(data: RiwayatRekamMedisResponse) {
        val intent = Intent(this, detail_resep::class.java)
        intent.putExtra("id_rekam_medis", data.id_rekam_medis)
        intent.putExtra("tanggal", data.tanggal_pemeriksaan)
        intent.putExtra("diagnosa", data.diagnosa)
        intent.putExtra("kode_icd", data.kode_icd ?: "-")
        intent.putExtra("catatan", data.catatan ?: "-")
        intent.putExtra("dokter", data.nama_dokter)
        intent.putExtra("status", "Selesai")
        startActivity(intent)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}