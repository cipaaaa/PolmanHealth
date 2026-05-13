package com.example.polmanhealth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class FormAntreanActivity : AppCompatActivity() {

    // Gunakan null-safety agar tidak crash jika ID tidak ketemu di XML
    private var tvTanggalTerpilih: TextView? = null
    private var btnAmbilAntrean: TextView? = null
    private var btnJam0800: TextView? = null
    private var btnJam0900: TextView? = null
    private var btnJam1000: TextView? = null
    private var btnJam1100: TextView? = null
    private var btnJam1300: TextView? = null
    private var btnJam1400: TextView? = null

    private var tanggalDipilih: String = ""
    private var jamDipilih: String = "09:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_antrean)

        // INISIALISASI dengan findViewById (Tanpa lateinit agar aman)
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        tvTanggalTerpilih = findViewById(R.id.tvTanggalTerpilih)
        btnAmbilAntrean = findViewById(R.id.btnAmbilAntrean)

        btnJam0800 = findViewById(R.id.btnJam0800)
        btnJam0900 = findViewById(R.id.btnJam0900)
        btnJam1000 = findViewById(R.id.btnJam1000)
        btnJam1100 = findViewById(R.id.btnJam1100)
        btnJam1300 = findViewById(R.id.btnJam1300)
        btnJam1400 = findViewById(R.id.btnJam1400)

        // 1. DEFAULT TANGGAL
        val calendar = Calendar.getInstance()
        tanggalDipilih = formatTanggal(calendar)
        updateTanggalJam()

        // 2. CALENDAR LISTENER
        calendarView?.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            tanggalDipilih = formatTanggal(calendar)
            updateTanggalJam()
        }

        // 3. KLIK JAM (Gunakan helper function)
        btnJam0800?.setOnClickListener { pilihJam("08:00", btnJam0800) }
        btnJam0900?.setOnClickListener { pilihJam("09:00", btnJam0900) }
        btnJam1000?.setOnClickListener { pilihJam("10:00", btnJam1000) }
        btnJam1100?.setOnClickListener { pilihJam("11:00", btnJam1100) }
        btnJam1300?.setOnClickListener { pilihJam("13:00", btnJam1300) }
        btnJam1400?.setOnClickListener { pilihJam("14:00", btnJam1400) }

        // 4. BUTTON AMBIL ANTREAN
        btnAmbilAntrean?.setOnClickListener {
            val intent = Intent(this, KonfirmasiAntreanActivity::class.java)
            intent.putExtra("nomorAntrean", "#A-013")
            intent.putExtra("tanggal", tanggalDipilih)
            intent.putExtra("waktu", "$jamDipilih WIB")
            intent.putExtra("poli", "Poli Umum")
            intent.putExtra("namaPasien", "Ahmad")
            intent.putExtra("nomorPasien", "2103401")
            startActivity(intent)
        }

        // 5. BACK BUTTON
        findViewById<TextView>(R.id.btnBack)?.setOnClickListener { finish() }

        // 6. BOTTOM NAV (Sesuaikan dengan nama di manifest kamu: jadwal, riwayat)
        findViewById<TextView>(R.id.navHome)?.setOnClickListener {
            startActivity(Intent(this, dashboard::class.java))
        }
        findViewById<TextView>(R.id.navJadwal)?.setOnClickListener {
            startActivity(Intent(this, jadwal::class.java))
        }
        findViewById<TextView>(R.id.navRiwayat)?.setOnClickListener {
            startActivity(Intent(this, riwayat::class.java))
        }
        findViewById<TextView>(R.id.navProfil)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    private fun pilihJam(jam: String, buttonAktif: TextView?) {
        jamDipilih = jam
        val semuaJam = listOf(btnJam0800, btnJam0900, btnJam1000, btnJam1100, btnJam1300, btnJam1400)

        semuaJam.forEach { button ->
            button?.setBackgroundResource(R.drawable.bg_time_outline)
            button?.setTextColor(Color.parseColor("#346C57"))
        }

        buttonAktif?.setBackgroundResource(R.drawable.bg_time_active)
        buttonAktif?.setTextColor(Color.WHITE)
        updateTanggalJam()
    }

    private fun updateTanggalJam() {
        tvTanggalTerpilih?.text = "🗓 $tanggalDipilih\n⏰ $jamDipilih WIB"
    }

    private fun formatTanggal(calendar: Calendar): String {
        val format = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return format.format(calendar.time)
    }
}