package com.example.merchandizecodedesk

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object PdfGenerator {
    fun generatePdf(
        name: String,
        company: String,
        market: String,
        selectedImageUri: Uri?,
        context: Context
    ) {
        val mDoc = com.itextpdf.text.Document()
        val fileName = company + market + getCurrentDate()
        val data =
            "Submitted by: $name\nCompany: $company\nMarket: $market"
        val fileDir = context.getExternalFilesDir(null)
        val filePath = File(fileDir, "$fileName.pdf")

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(filePath))
            mDoc.open()
            mDoc.add(Paragraph(data))

            val tempImageFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
            selectedImageUri?.let { uri ->
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val outputStream = FileOutputStream(tempImageFile)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()
                } catch (e: FileNotFoundException) {
                    Log.e("PdfGenerator", "FileNotFoundException: ${e.printStackTrace()}")
                } catch (e: IOException) {
                    Log.e("PdfGenerator", "IOException: ${e.printStackTrace()}")
                }
            }

            val image = com.itextpdf.text.Image.getInstance(tempImageFile.absolutePath)
            val itextImage = com.itextpdf.text.Image.getInstance(image)
            val pageSize = mDoc.pageSize
            val picWidth = pageSize.width * 0.5f
            val picHeight = pageSize.height * 0.5f
            val offsetX = (pageSize.width - picWidth) / 2
            val offsetY = (pageSize.height - picHeight) / 2
            itextImage.setAbsolutePosition(offsetX, offsetY)
            itextImage.scaleAbsolute(picWidth, picHeight)
            mDoc.add(itextImage)

            mDoc.close()

            Toast.makeText(context, "File Created.", Toast.LENGTH_SHORT).show()
        } catch (e: FileNotFoundException) {
            Log.e("PdfGenerator", "FileNotFoundException ${e.printStackTrace()}")
        } catch (e: Exception) {
            Log.e("PdfGenerator", "Other exception ${e.printStackTrace()}")
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ITALY)
        return dateFormat.format(currentDate).toString()
    }
}
