package com.example.merchandiseApp.pdfViewer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.versionedparcelable.ParcelField
import com.example.merchandiseApp.databinding.ActivityPdfDetailsBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File

class PdfDetails : AppCompatActivity() {
    private lateinit var binding: ActivityPdfDetailsBinding
    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fileName = intent.getStringExtra("fileName") ?: ""
        Log.d("filenameindetails", fileName)

        val storage = FirebaseStorage.getInstance()
        val storageReference: StorageReference = storage.reference.child(fileName)
        val localFile = File.createTempFile("temp","pdf")

        storageReference.getFile(localFile)
            .addOnSuccessListener { taskSnapshot ->
                displayPdf(localFile)

            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to download file", Toast.LENGTH_SHORT).show()
                Log.e("PdfDetails", "Error downloading file: ${exception.message}")
            }

    }

    private fun displayPdf(file: File) {
        try {
            val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(fileDescriptor)

            val imageView: ImageView = binding.pdfImageView

            val pageIndex = 0
            val page = pdfRenderer.openPage(pageIndex)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            imageView.setImageBitmap(bitmap)

            page.close()
            pdfRenderer.close()
            fileDescriptor.close()
        }catch (e: Exception){
            Toast.makeText(this,"Error loading PDF.", Toast.LENGTH_LONG).show()
            Log.e("PdfDetails", "Error loading Pdf, + ${e.message}")
        }

    }
}