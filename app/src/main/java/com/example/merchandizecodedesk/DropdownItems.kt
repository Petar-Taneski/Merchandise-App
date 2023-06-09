package com.example.merchandizecodedesk

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.merchandizecodedesk.databinding.DrowpdownMenuBinding
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DropdownItems : AppCompatActivity() {
    private lateinit var binding: DrowpdownMenuBinding
    private lateinit var apiService: RetrofitInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DrowpdownMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getListOfItemsFromApi()

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
                            this@DropdownItems,
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

