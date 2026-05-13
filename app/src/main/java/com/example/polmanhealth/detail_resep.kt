package com.example.polmanhealth

import android.content.ContentValues
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class detail_resep : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_resep)

        val dokter = intent.getStringExtra("dokter") ?: "Ahmad-Idar, MG."
        val tanggal = intent.getStringExtra("tanggal") ?: "15 Mar 2024"
        val status = intent.getStringExtra("status") ?: "Selesai"

        findViewById<TextView>(R.id.tvDokterDetail).text = dokter
        findViewById<TextView>(R.id.tvTanggalDetail).text = tanggal
        findViewById<TextView>(R.id.tvStatusDetail).text = status

        findViewById<TextView>(R.id.btnBackDetail).setOnClickListener {
            startActivity(Intent(this, riwayat::class.java))
            finish()
        }

        findViewById<TextView>(R.id.btnDownloadPdf).setOnClickListener {
            buatPdfResep(dokter, tanggal, status)
        }
    }

    private fun buatPdfResep(dokter: String, tanggal: String, status: String) {
        try {
            val pdfDocument = PdfDocument()
            val paint = Paint()

            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas

            paint.textSize = 22f
            paint.isFakeBoldText = true
            canvas.drawText("PolmanHealth+ - Detail Resep", 50f, 60f, paint)

            paint.textSize = 15f
            paint.isFakeBoldText = false
            canvas.drawText("Tanggal: $tanggal", 50f, 105f, paint)
            canvas.drawText("Dokter: $dokter", 50f, 130f, paint)
            canvas.drawText("Status: $status", 50f, 155f, paint)

            paint.textSize = 17f
            paint.isFakeBoldText = true
            canvas.drawText("Diagnosa", 50f, 205f, paint)

            paint.textSize = 14f
            paint.isFakeBoldText = false
            canvas.drawText("Pengasaman lambung ringan, perlu pengobatan rutin", 50f, 235f, paint)
            canvas.drawText("dan istirahat cukup selama pemulihan.", 50f, 255f, paint)

            paint.textSize = 17f
            paint.isFakeBoldText = true
            canvas.drawText("Resep Obat", 50f, 315f, paint)

            paint.textSize = 14f
            paint.isFakeBoldText = false
            canvas.drawText("1. Paracetamol - Tablet - 3x1 hari - Sesudah makan", 50f, 350f, paint)
            canvas.drawText("2. Amoxicillin - Kapsul - 3x1 hari - Sesudah makan", 50f, 375f, paint)
            canvas.drawText("3. Antasida - Tablet - 2x1 hari - Sebelum makan", 50f, 400f, paint)

            paint.textSize = 12f
            canvas.drawText("Dokumen ini dibuat otomatis oleh aplikasi PolmanHealth+.", 50f, 760f, paint)

            pdfDocument.finishPage(page)

            val fileName = "resep_polmanhealth_${System.currentTimeMillis()}.pdf"

            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

            if (uri != null) {
                contentResolver.openOutputStream(uri).use { outputStream ->
                    pdfDocument.writeTo(outputStream)
                }

                Toast.makeText(
                    this,
                    "PDF berhasil disimpan di folder Download",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(this, "Gagal membuat file PDF", Toast.LENGTH_SHORT).show()
            }

            pdfDocument.close()

        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}