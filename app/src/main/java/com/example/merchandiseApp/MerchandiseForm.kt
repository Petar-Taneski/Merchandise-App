package com.example.merchandiseApp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.merchandiseApp.databinding.MerchandiseFormBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.itextpdf.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class MerchandiseForm : AppCompatActivity() {
    private lateinit var binding: MerchandiseFormBinding
    private lateinit var apiService: RetrofitInterface
    private var selectedImageUri: Uri? = null
    private var generatedPDFUri: Uri? = null
    val firebaseFileRef = Firebase.storage.reference
    private var companyName: String = ""

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MerchandiseFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkForStoragePermission()
        getListOfItemsFromApi()

        binding.pickImage.setOnClickListener{
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }

        binding.btnSave.setOnClickListener{
            val name = binding.etName.text.toString()
            companyName = binding.etCompany.text.toString()
            val market = binding.spDrowpdown.selectedItem.toString()
            PdfGenerator.generatePdf(name, companyName, market, selectedImageUri, this)
            generatedPDFUri = PdfGenerator.getGeneratedUri()
        }
        binding.btnUpload.setOnClickListener{
            val fileName: String? = PdfGenerator.getFileName()
            uploadFileToFirebase("$fileName")
        }


    }

    private fun uploadFileToFirebase(filename: String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            generatedPDFUri?.let {
                firebaseFileRef.child(filename).putFile(it).await()
                withContext(Dispatchers.Main){
                    Toast.makeText(this@MerchandiseForm, "File uploaded.", Toast.LENGTH_LONG).show()
                }
            }

        }catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@MerchandiseForm, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                selectedImageUri = imgUri

                binding.selectedImage.setImageURI(imgUri)
            }
        }

    private fun checkForStoragePermission(){
        var f1 = false
        var f2 = false
        val permissionsToRequest = mutableListOf<String>()
        if(ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission
            .WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        )f1 = true
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission
                    .READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        )f2 = true

        if(!f1)
            permissionsToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if(!f2)
            permissionsToRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        if(permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this,permissionsToRequest.toTypedArray(),0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty()){
            for (i in grantResults.indices){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    Log.d("PermissionsRequest", "${permissions[i]} granted.")
            }
        }
    }
    private fun getListOfItemsFromApi() {

        apiService = RetrofitInstance.retrofit.create(RetrofitInterface::class.java)
        lifecycleScope.launch {
            val call: Call<List<ToDoItem>> = apiService.getTodos()
            call.enqueue(object : Callback<List<ToDoItem>>{
                override fun onResponse(
                    call: Call<List<ToDoItem>>,
                    response: Response<List<ToDoItem>>
                ) {
                    if (response.isSuccessful) {
                        val items: List<ToDoItem>? = response.body()
                        val spinnerItems: MutableList<String> = mutableListOf("No item selected")
                        val titles: List<String> = items?.map { it.title } ?: emptyList()
                        spinnerItems.addAll(titles)
                        val adapter = ArrayAdapter(
                            this@MerchandiseForm,
                            android.R.layout.simple_spinner_item,
                            spinnerItems
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spDrowpdown.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<List<ToDoItem>>, t: Throwable) {
                    binding.tvSelectItem.text = "No items have been received"
                }
            })
        }
    }
}

