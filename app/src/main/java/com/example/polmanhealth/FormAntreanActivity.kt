package com.example.polmanhealth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.DokterResponse
import com.example.polmanhealth.model.JadwalHariResponse
import com.example.polmanhealth.model.PendaftaranRequest
import com.example.polmanhealth.model.PendaftaranResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FormAntreanActivity : AppCompatActivity() {

    private var tvTanggalTerpilih: TextView? = null
    private var btnAmbilAntrean: TextView? = null
    private var spinnerDokter: Spinner? = null
    private var etKeluhan: EditText? = null

    private var btnJam0800: TextView? = null
    private var btnJam0900: TextView? = null
    private var btnJam1000: TextView? = null
    private var btnJam1100: TextView? = null
    private var btnJam1300: TextView? = null
    private var btnJam1400: TextView? = null

    private var tanggalDipilih: String = ""
    private var tanggalDb: String = ""
    private var jamDipilih: String = "-"
    private val calendarDipilih = Calendar.getInstance()

    private var listDokter: List<DokterResponse> = emptyList()
    private var idDokterDipilih: Int = 0
    private var idJadwalDipilih: Int = 0
    private var namaDokterDipilih: String = ""
    private var spesialisDipilih: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_antrean)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        tvTanggalTerpilih = findViewById(R.id.tvTanggalTerpilih)
        btnAmbilAntrean = findViewById(R.id.btnAmbilAntrean)
        spinnerDokter = findViewById(R.id.spinnerDokter)
        etKeluhan = findViewById(R.id.etKeluhan)

        btnJam0800 = findViewById(R.id.btnJam0800)
        btnJam0900 = findViewById(R.id.btnJam0900)

        setupTampilanJamAwal()
        loadDokter()

        tanggalDipilih = formatTanggal(calendarDipilih)
        tanggalDb = formatTanggalDb(calendarDipilih)
        updateTanggalJam()

        calendarView?.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendarDipilih.set(year, month, dayOfMonth)
            tanggalDipilih = formatTanggal(calendarDipilih)
            tanggalDb = formatTanggalDb(calendarDipilih)
            updateTanggalJam()
            loadJamPraktek()
        }

        btnAmbilAntrean?.setOnClickListener {
            buatPendaftaran()
        }

        findViewById<TextView>(R.id.navHome)?.setOnClickListener {
            pindahHalaman(Intent(this, dashboard::class.java), 0)
        }

        findViewById<TextView>(R.id.navJadwal)?.setOnClickListener {
            pindahHalaman(Intent(this, jadwal::class.java), 1)
        }

        findViewById<TextView>(R.id.navAntrean)?.setOnClickListener {
            // Sudah di Antrean
        }

        findViewById<TextView>(R.id.navRiwayat)?.setOnClickListener {
            pindahHalaman(Intent(this, riwayat::class.java), 3)
        }

        findViewById<TextView>(R.id.navProfil)?.setOnClickListener {
            pindahHalaman(Intent(this, ProfileActivity::class.java), 4)
        }
    }

    private fun pindahHalaman(intent: Intent, targetIndex: Int) {
        val currentIndex = 2 // Antrean

        startActivity(intent)

        if (targetIndex > currentIndex) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        } else {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        finish()
    }

    private fun buatPendaftaran() {
        val sharedPref = getSharedPreferences("USER_SESSION", MODE_PRIVATE)
        val idPasien = sharedPref.getInt("id_pasien", 0)
        val namaPasien = sharedPref.getString("nama_pasien", "Pasien") ?: "Pasien"
        val keluhan = etKeluhan?.text.toString().trim()

        if (idPasien == 0) {
            Toast.makeText(this, "Session pasien tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        if (idDokterDipilih == 0) {
            Toast.makeText(this, "Silakan pilih dokter terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        if (idJadwalDipilih == 0) {
            Toast.makeText(this, "Jadwal dokter belum dipilih", Toast.LENGTH_SHORT).show()
            return
        }

        if (keluhan.isEmpty()) {
            Toast.makeText(this, "Keluhan harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (jamDipilih == "-" || jamDipilih == "Tidak ada jadwal") {
            Toast.makeText(this, "Jadwal dokter tidak tersedia", Toast.LENGTH_SHORT).show()
            return
        }

        val request = PendaftaranRequest(
            id_pasien = idPasien,
            id_dokter = idDokterDipilih,
            id_jadwal = idJadwalDipilih,
            tanggal = tanggalDb,
            keluhan = keluhan,
            status = "Menunggu"
        )

        RetrofitClient.instance.buatPendaftaran(request)
            .enqueue(object : Callback<PendaftaranResponse> {
                override fun onResponse(
                    call: Call<PendaftaranResponse>,
                    response: Response<PendaftaranResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!

                        val intent = Intent(this@FormAntreanActivity, KonfirmasiAntreanActivity::class.java)
                        intent.putExtra("nomorAntrean", data.nomor_antrean.toString())
                        intent.putExtra("tanggal", tanggalDipilih)
                        intent.putExtra("waktu", "$jamDipilih WIB")
                        intent.putExtra("poli", spesialisDipilih)
                        intent.putExtra("namaDokter", namaDokterDipilih)
                        intent.putExtra("idDokter", idDokterDipilih)
                        intent.putExtra("namaPasien", namaPasien)
                        intent.putExtra("nomorPasien", idPasien.toString())
                        startActivity(intent)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("ERROR BUAT PENDAFTARAN: $errorBody")

                        Toast.makeText(
                            this@FormAntreanActivity,
                            "Gagal membuat antrean: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<PendaftaranResponse>, t: Throwable) {
                    Toast.makeText(
                        this@FormAntreanActivity,
                        "Gagal terhubung ke server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
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
                                idJadwalDipilih = 0
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
                        idJadwalDipilih = jadwalDokter.id_jadwal

                        val jamMulai = jadwalDokter.jam_mulai.substring(0, 5)
                        val jamSelesai = jadwalDokter.jam_selesai.substring(0, 5)

                        jamDipilih = "$jamMulai - $jamSelesai"

                        btnJam0900?.text = jamDipilih
                        btnJam0900?.visibility = View.VISIBLE
                        btnJam0900?.setBackgroundResource(R.drawable.bg_time_active)
                        btnJam0900?.setTextColor(Color.WHITE)

                        updateTanggalJam()
                    } else {
                        idJadwalDipilih = 0
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

    private fun formatTanggalDb(calendar: Calendar): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        return format.format(calendar.time)
    }

    private fun getHariDariTanggal(calendar: Calendar): String {
        val format = SimpleDateFormat("EEEE", Locale("id", "ID"))
        return format.format(calendar.time)
    }
}