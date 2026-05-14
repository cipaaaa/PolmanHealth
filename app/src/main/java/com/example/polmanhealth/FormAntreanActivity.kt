package com.example.polmanhealth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.DokterResponse
import com.example.polmanhealth.model.JadwalHariResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FormAntreanActivity : AppCompatActivity() {

    private var tvTanggalTerpilih: TextView? = null
    private var btnAmbilAntrean: TextView? = null
    private var spinnerDokter: Spinner? = null

    private var btnJam0800: TextView? = null
    private var btnJam0900: TextView? = null
    private var btnJam1000: TextView? = null
    private var btnJam1100: TextView? = null
    private var btnJam1300: TextView? = null
    private var btnJam1400: TextView? = null

    private var tanggalDipilih: String = ""
    private var jamDipilih: String = "-"
    private val calendarDipilih = Calendar.getInstance()

    private var listDokter: List<DokterResponse> = emptyList()
    private var idDokterDipilih: Int = 0
    private var namaDokterDipilih: String = ""
    private var spesialisDipilih: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_antrean)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        tvTanggalTerpilih = findViewById(R.id.tvTanggalTerpilih)
        btnAmbilAntrean = findViewById(R.id.btnAmbilAntrean)
        spinnerDokter = findViewById(R.id.spinnerDokter)

        btnJam0800 = findViewById(R.id.btnJam0800)
        btnJam0900 = findViewById(R.id.btnJam0900)


        setupTampilanJamAwal()
        loadDokter()

        tanggalDipilih = formatTanggal(calendarDipilih)
        updateTanggalJam()

        calendarView?.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendarDipilih.set(year, month, dayOfMonth)
            tanggalDipilih = formatTanggal(calendarDipilih)
            updateTanggalJam()
            loadJamPraktek()
        }

        btnAmbilAntrean?.setOnClickListener {
            if (idDokterDipilih == 0) {
                Toast.makeText(this, "Silakan pilih dokter terlebih dahulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (jamDipilih == "-" || jamDipilih == "Tidak ada jadwal") {
                Toast.makeText(this, "Jadwal dokter tidak tersedia", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, KonfirmasiAntreanActivity::class.java)
            intent.putExtra("tanggal", tanggalDipilih)
            intent.putExtra("waktu", "$jamDipilih WIB")
            intent.putExtra("poli", spesialisDipilih)
            intent.putExtra("namaDokter", namaDokterDipilih)
            intent.putExtra("idDokter", idDokterDipilih)
            val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
            val namaPasien = sharedPref.getString("nama_pasien", "Pasien")
            val idPasien = sharedPref.getInt("id_pasien", 0)

            intent.putExtra("nomorAntrean", "1")
            intent.putExtra("namaPasien", namaPasien)
            intent.putExtra("nomorPasien", idPasien.toString())
            startActivity(intent)
        }

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

    private fun loadDokter() {
        RetrofitClient.instance.getAllDokter().enqueue(object : Callback<List<DokterResponse>> {
            override fun onResponse(
                call: Call<List<DokterResponse>>,
                response: Response<List<DokterResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    listDokter = response.body()!!

                    val daftarNamaDokter = mutableListOf("Silakan pilih dokter...")
                    daftarNamaDokter.addAll(
                        listDokter.map {
                            "${it.nama_dokter} - ${it.spesialis}"
                        }
                    )

                    val adapter = object : ArrayAdapter<String>(
                        this@FormAntreanActivity,
                        android.R.layout.simple_spinner_item,
                        daftarNamaDokter
                    ) {
                        override fun isEnabled(position: Int): Boolean {
                            return position != 0
                        }
                    }

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerDokter?.adapter = adapter

                    spinnerDokter?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: android.view.View?,
                            position: Int,
                            id: Long
                        ) {
                            if (position == 0) {
                                idDokterDipilih = 0
                                namaDokterDipilih = ""
                                spesialisDipilih = ""
                                jamDipilih = "-"
                                btnJam0900?.text = "Pilih dokter dulu"
                                updateTanggalJam()

                                return
                            }

                            val dokter = listDokter[position - 1]
                            idDokterDipilih = dokter.id_dokter
                            namaDokterDipilih = dokter.nama_dokter
                            spesialisDipilih = dokter.spesialis
                            loadJamPraktek()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}
                    }
                }
            }

            override fun onFailure(call: Call<List<DokterResponse>>, t: Throwable) {
                Toast.makeText(
                    this@FormAntreanActivity,
                    "Gagal mengambil data dokter",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun loadJamPraktek() {
        if (idDokterDipilih == 0) return

        val hari = getHariDariTanggal(calendarDipilih)

        RetrofitClient.instance.getJadwalByHari(hari).enqueue(object : Callback<List<JadwalHariResponse>> {
            override fun onResponse(
                call: Call<List<JadwalHariResponse>>,
                response: Response<List<JadwalHariResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val jadwalDokter = response.body()!!.firstOrNull {
                        it.id_dokter == idDokterDipilih
                    }

                    if (jadwalDokter != null) {
                        val jamMulai = jadwalDokter.jam_mulai.substring(0, 5)
                        val jamSelesai = jadwalDokter.jam_selesai.substring(0, 5)

                        jamDipilih = "$jamMulai - $jamSelesai"

                        btnJam0900?.text = jamDipilih
                        btnJam0900?.visibility = View.VISIBLE
                        btnJam0900?.setBackgroundResource(R.drawable.bg_time_active)
                        btnJam0900?.setTextColor(Color.WHITE)

                        updateTanggalJam()
                    } else {
                        jamDipilih = "Tidak ada jadwal"
                        btnJam0900?.text = jamDipilih
                        updateTanggalJam()
                    }
                }
            }

            override fun onFailure(call: Call<List<JadwalHariResponse>>, t: Throwable) {
                Toast.makeText(
                    this@FormAntreanActivity,
                    "Gagal mengambil jadwal dokter",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun setupTampilanJamAwal() {
        btnJam0800?.visibility = View.GONE
        btnJam1000?.visibility = View.GONE
        btnJam1100?.visibility = View.GONE
        btnJam1300?.visibility = View.GONE
        btnJam1400?.visibility = View.GONE

        btnJam0900?.text = "Memuat..."
        btnJam0900?.setBackgroundResource(R.drawable.bg_time_active)
        btnJam0900?.setTextColor(Color.WHITE)
        btnJam0900?.isClickable = false
    }

    private fun updateTanggalJam() {
        val teksJam = if (
            jamDipilih == "-" ||
            jamDipilih == "Tidak ada jadwal"
        ) {
            "⏰ $jamDipilih"
        } else {
            "⏰ $jamDipilih WIB"
        }

        tvTanggalTerpilih?.text = "🗓 $tanggalDipilih\n$teksJam"
    }
    private fun formatTanggal(calendar: Calendar): String {
        val format = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        return format.format(calendar.time)
    }

    private fun getHariDariTanggal(calendar: Calendar): String {
        val format = SimpleDateFormat("EEEE", Locale("id", "ID"))
        return format.format(calendar.time)
    }
}