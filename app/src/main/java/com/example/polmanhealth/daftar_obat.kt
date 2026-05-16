package com.example.polmanhealth

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.ObatResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class daftar_obat : AppCompatActivity() {

    private lateinit var layoutDaftarObat: LinearLayout
    private lateinit var etCariObat: EditText

    private val batasStokMenipis = 10
    private var semuaObat: List<ObatResponse> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_obat)

        layoutDaftarObat = findViewById(R.id.layoutDaftarObat)
        etCariObat = findViewById(R.id.etCariObat)

        etCariObat.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {

                filterObat(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        loadDaftarObat()
    }

    private fun loadDaftarObat() {

        RetrofitClient.instance.getAllObat()
            .enqueue(object : Callback<List<ObatResponse>> {

                override fun onResponse(
                    call: Call<List<ObatResponse>>,
                    response: Response<List<ObatResponse>>
                ) {

                    if (response.isSuccessful && response.body() != null) {

                        val dataObat = response.body()!!

                        semuaObat = dataObat

                        layoutDaftarObat.removeAllViews()

                        if (dataObat.isEmpty()) {

                            tampilkanKosong()

                        } else {

                            dataObat.forEach { obat ->
                                tambahCardObat(obat)
                            }

                            val obatMenipis = dataObat.firstOrNull {
                                it.stok <= batasStokMenipis
                            }

                            if (obatMenipis != null) {

                                Toast.makeText(
                                    this@daftar_obat,
                                    "Peringatan: stok ${obatMenipis.nama_obat} menipis",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    } else {

                        Toast.makeText(
                            this@daftar_obat,
                            "Gagal mengambil data obat",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<List<ObatResponse>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@daftar_obat,
                        "Gagal terhubung ke server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun filterObat(keyword: String) {

        val hasilFilter = semuaObat.filter {

            it.nama_obat.contains(keyword, ignoreCase = true) ||
                    it.jenis_obat.contains(keyword, ignoreCase = true)
        }

        layoutDaftarObat.removeAllViews()

        if (hasilFilter.isEmpty()) {

            tampilkanKosong()

        } else {

            hasilFilter.forEach { obat ->
                tambahCardObat(obat)
            }
        }
    }

    private fun tambahCardObat(obat: ObatResponse) {

        val stokMenipis = obat.stok <= batasStokMenipis

        val card = LinearLayout(this)

        card.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            dpToPx(if (stokMenipis) 110 else 100)
        ).apply {
            topMargin = dpToPx(14)
        }

        card.setBackgroundResource(R.drawable.bg_card_strong)
        card.elevation = dpToPx(8).toFloat()
        card.gravity = Gravity.CENTER_VERTICAL
        card.orientation = LinearLayout.HORIZONTAL
        card.setPadding(dpToPx(18), dpToPx(18), dpToPx(18), dpToPx(18))

        val icon = TextView(this)

        icon.layoutParams = LinearLayout.LayoutParams(
            dpToPx(54),
            dpToPx(54)
        )

        icon.setBackgroundResource(R.drawable.bg_icon_green_light)
        icon.gravity = Gravity.CENTER
        icon.text = "💊"
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

        val namaObat = TextView(this)

        namaObat.text = obat.nama_obat
        namaObat.setTextColor(Color.parseColor("#18382F"))
        namaObat.textSize = 18f
        namaObat.setTypeface(null, android.graphics.Typeface.BOLD)

        val jenisObat = TextView(this)

        jenisObat.text = obat.jenis_obat
        jenisObat.setTextColor(Color.parseColor("#7A9389"))
        jenisObat.textSize = 13f
        jenisObat.setTypeface(null, android.graphics.Typeface.BOLD)

        info.addView(namaObat)
        info.addView(jenisObat)

        if (stokMenipis) {

            val warning = TextView(this)

            warning.text = "⚠ Stok menipis"
            warning.setTextColor(Color.parseColor("#D92D20"))
            warning.textSize = 12f
            warning.setTypeface(null, android.graphics.Typeface.BOLD)

            info.addView(warning)
        }

        val stok = TextView(this)

        stok.layoutParams = LinearLayout.LayoutParams(
            dpToPx(76),
            dpToPx(36)
        )

        stok.setBackgroundResource(
            if (stokMenipis)
                R.drawable.bg_status_red
            else
                R.drawable.bg_status_green
        )

        stok.gravity = Gravity.CENTER
        stok.text = "Stok ${obat.stok}"

        stok.setTextColor(
            Color.parseColor(
                if (stokMenipis)
                    "#B42318"
                else
                    "#08785B"
            )
        )

        stok.textSize = 13f
        stok.setTypeface(null, android.graphics.Typeface.BOLD)

        card.addView(icon)
        card.addView(info)
        card.addView(stok)

        layoutDaftarObat.addView(card)
    }

    private fun tampilkanKosong() {

        val textKosong = TextView(this)

        textKosong.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dpToPx(20)
        }

        textKosong.text = "Obat tidak ditemukan"
        textKosong.gravity = Gravity.CENTER
        textKosong.setTextColor(Color.parseColor("#7A9389"))
        textKosong.textSize = 15f
        textKosong.setTypeface(null, android.graphics.Typeface.BOLD)

        layoutDaftarObat.addView(textKosong)
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}