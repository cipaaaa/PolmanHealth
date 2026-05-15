package com.example.polmanhealth

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.api.RetrofitClient
import com.example.polmanhealth.model.RekamMedisRequest
import com.example.polmanhealth.model.RekamMedisResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import com.example.polmanhealth.model.ObatRequest
import com.example.polmanhealth.model.ObatResponse
import com.example.polmanhealth.model.DetailResepRequest
import com.example.polmanhealth.model.DetailResepResponse

class InputDiagnosaActivity : AppCompatActivity() {

    private lateinit var edtDiagnosa: EditText
    private lateinit var edtKodeIcd: EditText
    private lateinit var edtCatatanDiagnosa: EditText

    private lateinit var edtNamaObat: EditText
    private lateinit var edtJenisObat: EditText
    private lateinit var edtFrekuensi: EditText
    private lateinit var edtLamaMinum: EditText

    private lateinit var btnSesudahMakan: TextView
    private lateinit var btnSebelumMakan: TextView
    private lateinit var btnTambahObat: TextView
    private lateinit var btnSimpan: Button
    private lateinit var layoutListObat: LinearLayout
    private lateinit var tvKosongObat: TextView

    private val listObat = ArrayList<ObatInput>()

    data class ObatInput(
        val namaObat: String,
        val jenisObat: String,
        val dosis: String,
        val aturanPakai: String,
        val jumlah: Int,
        val keterangan: String?
    )
    private var aturanMinumTerpilih = "Sesudah Makan"

    private var idPendaftaran: Int = 0
    private var namaPasien: String = ""
    private var nomorAntrean: String = ""
    private var spesialis: String = ""
    private var namaDokter: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_diagnosa)

        idPendaftaran = intent.getIntExtra("id_pendaftaran", 0)
        namaPasien = intent.getStringExtra("nama_pasien") ?: "Nama pasien"
        nomorAntrean = intent.getStringExtra("nomor_antrean") ?: "-"
        spesialis = intent.getStringExtra("spesialis") ?: "-"
        namaDokter = intent.getStringExtra("nama_dokter") ?: "-"

        val tvNamaPasien = findViewById<TextView>(R.id.tvNamaPasien)
        val tvNomorAntrean = findViewById<TextView>(R.id.tvNomorAntrean)
        val tvPoliDokter = findViewById<TextView>(R.id.tvPoliDokter)

        tvNamaPasien.text = namaPasien
        tvNomorAntrean.text = "Antrean: $nomorAntrean"
        tvPoliDokter.text = "$spesialis - $namaDokter"

        edtDiagnosa = findViewById(R.id.edtDiagnosa)
        edtKodeIcd = findViewById(R.id.edtKodeIcd)
        edtCatatanDiagnosa = findViewById(R.id.edtCatatanDiagnosa)

        edtNamaObat = findViewById(R.id.edtNamaObat)
        edtJenisObat = findViewById(R.id.edtJenisObat)
        edtFrekuensi = findViewById(R.id.edtFrekuensi)
        edtLamaMinum = findViewById(R.id.edtLamaMinum)

        btnSesudahMakan = findViewById(R.id.btnSesudahMakan)
        btnSebelumMakan = findViewById(R.id.btnSebelumMakan)
        btnTambahObat = findViewById(R.id.btnTambahObat)
        btnSimpan = findViewById(R.id.btnSimpan)
        layoutListObat = findViewById(R.id.layoutListObat)
        tvKosongObat = findViewById(R.id.tvKosongObat)

        btnSesudahMakan.setOnClickListener {
            aturanMinumTerpilih = "Sesudah Makan"
            btnSesudahMakan.setBackgroundResource(R.drawable.bg_rule_selected)
            btnSebelumMakan.setBackgroundResource(R.drawable.bg_rule_normal)
        }

        btnSebelumMakan.setOnClickListener {
            aturanMinumTerpilih = "Sebelum Makan"
            btnSebelumMakan.setBackgroundResource(R.drawable.bg_rule_selected)
            btnSesudahMakan.setBackgroundResource(R.drawable.bg_rule_normal)
        }

        btnTambahObat.setOnClickListener {
            tambahObat()
        }

        btnSimpan.setOnClickListener {
            simpanRekamMedis()
        }

        findViewById<TextView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun simpanRekamMedis() {


        val diagnosa = edtDiagnosa.text.toString().trim()
        val kodeIcd = edtKodeIcd.text.toString().trim()
        val catatan = edtCatatanDiagnosa.text.toString().trim()

        if (idPendaftaran == 0) {
            Toast.makeText(this, "ID pendaftaran tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        if (diagnosa.isEmpty()) {
            Toast.makeText(this, "Diagnosa harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        if (listObat.isEmpty()) {
            Toast.makeText(this, "Minimal tambahkan 1 obat", Toast.LENGTH_SHORT).show()
            return
        }

        val tanggalHariIni = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
            .format(Date())

        val request = RekamMedisRequest(
            id_pendaftaran = idPendaftaran,
            diagnosa = diagnosa,
            kode_icd = if (kodeIcd.isEmpty()) null else kodeIcd,
            catatan = if (catatan.isEmpty()) null else catatan,
            tanggal_pemeriksaan = tanggalHariIni
        )

        RetrofitClient.instance.createRekamMedis(request)
            .enqueue(object : Callback<RekamMedisResponse> {
                override fun onResponse(
                    call: Call<RekamMedisResponse>,
                    response: Response<RekamMedisResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val idRekamMedis = response.body()!!.id_rekam_medis
                        simpanDetailResep(idRekamMedis)

                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("ERROR SIMPAN REKAM MEDIS: $errorBody")

                        Toast.makeText(
                            this@InputDiagnosaActivity,
                            "Gagal menyimpan diagnosa: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<RekamMedisResponse>, t: Throwable) {
                    Toast.makeText(
                        this@InputDiagnosaActivity,
                        "Gagal terhubung ke server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun simpanDetailResep(idRekamMedis: Int) {
        var jumlahBerhasil = 0

        listObat.forEach { obatInput ->

            val obatRequest = ObatRequest(
                nama_obat = obatInput.namaObat,
                jenis_obat = obatInput.jenisObat,
                stok = 100
            )

            RetrofitClient.instance.getOrCreateObat(obatRequest)
                .enqueue(object : Callback<ObatResponse> {

                    override fun onResponse(
                        call: Call<ObatResponse>,
                        response: Response<ObatResponse>
                    ) {

                        if (response.isSuccessful && response.body() != null) {

                            val obat = response.body()!!

                            val detailRequest = DetailResepRequest(
                                id_rekam_medis = idRekamMedis,
                                id_obat = obat.id_obat,
                                dosis = obatInput.dosis,
                                aturan_pakai = obatInput.aturanPakai,
                                jumlah = obatInput.jumlah,
                                keterangan = obatInput.keterangan
                            )

                            RetrofitClient.instance.createDetailResep(detailRequest)
                                .enqueue(object : Callback<DetailResepResponse> {

                                    override fun onResponse(
                                        call: Call<DetailResepResponse>,
                                        response: Response<DetailResepResponse>
                                    ) {

                                        if (response.isSuccessful) {

                                            jumlahBerhasil++

                                            if (jumlahBerhasil == listObat.size) {

                                                Toast.makeText(
                                                    this@InputDiagnosaActivity,
                                                    "Diagnosa dan resep berhasil disimpan",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                finish()
                                            }

                                        } else {

                                            Toast.makeText(
                                                this@InputDiagnosaActivity,
                                                "Gagal menyimpan detail resep",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<DetailResepResponse>,
                                        t: Throwable
                                    ) {

                                        Toast.makeText(
                                            this@InputDiagnosaActivity,
                                            "Gagal terhubung saat simpan resep",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })

                        } else {

                            Toast.makeText(
                                this@InputDiagnosaActivity,
                                "Gagal mengambil data obat",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<ObatResponse>,
                        t: Throwable
                    ) {

                        Toast.makeText(
                            this@InputDiagnosaActivity,
                            "Gagal terhubung ke server",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    private fun tambahObat() {
        val namaObat = edtNamaObat.text.toString().trim()
        val jenisObat = edtJenisObat.text.toString().trim()
        val frekuensi = edtFrekuensi.text.toString().trim()
        val lamaMinum = edtLamaMinum.text.toString().trim()

        if (namaObat.isEmpty() || jenisObat.isEmpty() || frekuensi.isEmpty() || lamaMinum.isEmpty()) {
            Toast.makeText(this, "Data obat harus lengkap", Toast.LENGTH_SHORT).show()
            return
        }

        val aturanLengkap = "$frekuensi, $lamaMinum"
        val dataObat = ObatInput(
            namaObat = namaObat,
            jenisObat = jenisObat,
            dosis = "$frekuensi $lamaMinum",
            aturanPakai = aturanMinumTerpilih,
            jumlah = 1,
            keterangan = null
        )

        listObat.add(dataObat)

        tvKosongObat.visibility = View.GONE

        val cardObat = TextView(this)
        cardObat.text = "💊  $namaObat\n$jenisObat\nAturan: $aturanLengkap\n$aturanMinumTerpilih"
        cardObat.textSize = 15f
        cardObat.setTextColor(Color.parseColor("#18382F"))
        cardObat.setPadding(24, 20, 24, 20)
        cardObat.setBackgroundResource(R.drawable.bg_obat_card)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 18)
        cardObat.layoutParams = params

        layoutListObat.addView(cardObat)

        edtNamaObat.text.clear()
        edtJenisObat.text.clear()
        edtFrekuensi.text.clear()
        edtLamaMinum.text.clear()
    }
}