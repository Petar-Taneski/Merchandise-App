package com.example.merchandiseApp

import retrofit2.Call
import retrofit2.http.GET

interface RetrofitInterface {
    @GET("/todos")
    fun getTodos(): Call<List<ToDoItem>>

}