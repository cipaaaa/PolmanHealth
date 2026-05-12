package com.example.polmanhealth

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.polmanhealth.R
class InputDiagnosaActivity : AppCompatActivity() {

    private lateinit var edtDiagnosa: EditText
    private lateinit var edtNamaObat: EditText
    private lateinit var edtJenisObat: EditText
    private lateinit var edtAturanMinum: EditText
    private lateinit var spinnerKeterangan: Spinner
    private lateinit var btnTambahObat: Button
    private lateinit var btnSimpan: Button
    private lateinit var layoutListObat: LinearLayout

    private val listObat = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_diagnosa)

        edtDiagnosa = findViewById(R.id.edtDiagnosa)
        edtNamaObat = findViewById(R.id.edtNamaObat)
        edtJenisObat = findViewById(R.id.edtJenisObat)
        edtAturanMinum = findViewById(R.id.edtAturanMinum)
        spinnerKeterangan = findViewById(R.id.spinnerKeterangan)
        btnTambahObat = findViewById(R.id.btnTambahObat)
        btnSimpan = findViewById(R.id.btnSimpan)
        layoutListObat = findViewById(R.id.layoutListObat)

        val pilihanKeterangan = arrayOf("Sebelum Makan", "Sesudah Makan")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            pilihanKeterangan
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKeterangan.adapter = adapter

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
    }

    private fun tambahObat() {
        val namaObat = edtNamaObat.text.toString()
        val jenisObat = edtJenisObat.text.toString()
        val aturanMinum = edtAturanMinum.text.toString()
        val keterangan = spinnerKeterangan.selectedItem.toString()

        if (namaObat.isEmpty() || jenisObat.isEmpty() || aturanMinum.isEmpty()) {
            Toast.makeText(this, "Data obat harus lengkap", Toast.LENGTH_SHORT).show()
            return
        }

        val dataObat = "$namaObat\n$jenisObat\n$aturanMinum\n$keterangan"
        listObat.add(dataObat)

        val cardObat = TextView(this)
        cardObat.text = "💊  $namaObat\n$jenisObat\nAturan: $aturanMinum\n$keterangan"
        cardObat.textSize = 15f
        cardObat.setTextColor(android.graphics.Color.parseColor("#18382F"))
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
        edtAturanMinum.text.clear()
        spinnerKeterangan.setSelection(0)
    }
}