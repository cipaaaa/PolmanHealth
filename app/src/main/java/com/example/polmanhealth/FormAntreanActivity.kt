package com.example.polmanhealth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FormAntreanActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var tvTanggalTerpilih: TextView
    private lateinit var btnAmbilAntrean: TextView

    private lateinit var btnJam0800: TextView
    private lateinit var btnJam0900: TextView
    private lateinit var btnJam1000: TextView
    private lateinit var btnJam1100: TextView
    private lateinit var btnJam1300: TextView
    private lateinit var btnJam1400: TextView

    private var tanggalDipilih: String = ""
    private var jamDipilih: String = "09:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_antrean)

        calendarView = findViewById(R.id.calendarView)
        tvTanggalTerpilih = findViewById(R.id.tvTanggalTerpilih)
        btnAmbilAntrean = findViewById(R.id.btnAmbilAntrean)

        btnJam0800 = findViewById(R.id.btnJam0800)
        btnJam0900 = findViewById(R.id.btnJam0900)
        btnJam1000 = findViewById(R.id.btnJam1000)
        btnJam1100 = findViewById(R.id.btnJam1100)
        btnJam1300 = findViewById(R.id.btnJam1300)
        btnJam1400 = findViewById(R.id.btnJam1400)

        val calendar = Calendar.getInstance()
        tanggalDipilih = formatTanggal(calendar)

        updateTanggalJam()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            tanggalDipilih = formatTanggal(calendar)
            updateTanggalJam()
        }

        btnJam0800.setOnClickListener { pilihJam("08:00", btnJam0800) }
        btnJam0900.setOnClickListener { pilihJam("09:00", btnJam0900) }
        btnJam1000.setOnClickListener { pilihJam("10:00", btnJam1000) }
        btnJam1100.setOnClickListener { pilihJam("11:00", btnJam1100) }
        btnJam1300.setOnClickListener { pilihJam("13:00", btnJam1300) }
        btnJam1400.setOnClickListener { pilihJam("14:00", btnJam1400) }

        btnAmbilAntrean.setOnClickListener {
            val intent = Intent(this, KonfirmasiAntreanActivity::class.java)
            intent.putExtra("nomorAntrean", "#A-013")
            intent.putExtra("tanggal", tanggalDipilih)
            intent.putExtra("waktu", "$jamDipilih WIB")
            intent.putExtra("poli", "Poli Umum")
            intent.putExtra("namaPasien", "Ahmad")
            intent.putExtra("nomorPasien", "2103401")
            startActivity(intent)
        }
    }

    private fun pilihJam(jam: String, buttonAktif: TextView) {
        jamDipilih = jam

        val semuaJam = listOf(
            btnJam0800,
            btnJam0900,
            btnJam1000,
            btnJam1100,
            btnJam1300,
            btnJam1400
        )

        semuaJam.forEach {
            it.setBackgroundResource(R.drawable.bg_time_outline)
            it.setTextColor(Color.parseColor("#346C57"))
        }

        buttonAktif.setBackgroundResource(R.drawable.bg_time_active)
        buttonAktif.setTextColor(Color.WHITE)

        updateTanggalJam()
    }

    private fun updateTanggalJam() {
        tvTanggalTerpilih.text = "🗓   $tanggalDipilih ·\n$jamDipilih"
    }

    private fun formatTanggal(calendar: Calendar): String {
        val localeIndonesia = Locale("id", "ID")
        val format = SimpleDateFormat("EEEE, dd MMMM yyyy", localeIndonesia)
        return format.format(calendar.time)
    }
}