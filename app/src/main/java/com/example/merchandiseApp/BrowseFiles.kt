package com.example.merchandiseApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.merchandiseApp.databinding.ActivityBrowseFilesBinding
import com.example.merchandiseApp.pdfViewer.PdfDetails
import com.google.firebase.storage.FirebaseStorage

class BrowseFiles : AppCompatActivity() {
    private lateinit var binding: ActivityBrowseFilesBinding
    private var pdfFiles: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrowseFilesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchFileNames()

    }

    private fun fetchFileNames() {
        val storageReference = FirebaseStorage.getInstance().reference

        storageReference.listAll()
            .addOnSuccessListener {  listResult ->
                for(fileRef in listResult.items){
                    val fileName = fileRef.name
                    pdfFiles.add(fileName)
                }

                val adapter = PdfListAdapter(pdfFiles){ selectedPdfFile ->

                    openDetailsActivity(selectedPdfFile)
                }
                binding.rvPdfList.adapter = adapter
                binding.rvPdfList.layoutManager = LinearLayoutManager(this)
            }
            .addOnFailureListener{exception ->
                Toast.makeText(this@BrowseFiles, "failed to retrieve files.", Toast.LENGTH_LONG).show()
            }
    }

    private fun openDetailsActivity(selectedPdfFile: String) {
        val intent = Intent(this, PdfDetails::class.java)
        intent.putExtra("fileName", selectedPdfFile)
        startActivity(intent)
    }

}