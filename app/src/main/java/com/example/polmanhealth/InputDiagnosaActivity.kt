package com.example.polmanhealth

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

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

    private val listObat = ArrayList<String>()
    private var aturanMinumTerpilih = "Sesudah Makan"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_diagnosa)

        // Binding Diagnosa
        edtDiagnosa = findViewById(R.id.edtDiagnosa)
        edtKodeIcd = findViewById(R.id.edtKodeIcd)
        edtCatatanDiagnosa = findViewById(R.id.edtCatatanDiagnosa)

        // Binding Obat
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

        // Logic Aturan Minum Selection
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
            val diagnosa = edtDiagnosa.text.toString()

            if (diagnosa.isEmpty()) {
                Toast.makeText(this, "Diagnosa harus diisi", Toast.LENGTH_SHORT).show()
            } else if (listObat.isEmpty()) {
                Toast.makeText(this, "Minimal tambahkan 1 obat", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Diagnosa dan resep berhasil disimpan", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<TextView>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }

    private fun tambahObat() {
        val namaObat = edtNamaObat.text.toString()
        val jenisObat = edtJenisObat.text.toString()
        val frekuensi = edtFrekuensi.text.toString()
        val lamaMinum = edtLamaMinum.text.toString()

        if (namaObat.isEmpty() || jenisObat.isEmpty() || frekuensi.isEmpty() || lamaMinum.isEmpty()) {
            Toast.makeText(this, "Data obat harus lengkap", Toast.LENGTH_SHORT).show()
            return
        }

        val aturanLengkap = "$frekuensi, $lamaMinum"
        val dataObat = "$namaObat\n$jenisObat\n$aturanLengkap\n$aturanMinumTerpilih"
        listObat.add(dataObat)

        // Hide empty message if there are medications
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

        // Reset Medication Form
        edtNamaObat.text.clear()
        edtJenisObat.text.clear()
        edtFrekuensi.text.clear()
        edtLamaMinum.text.clear()
    }
}
