package com.example.fetchexercise.api

import retrofit2.Call
import retrofit2.http.GET

interface FetchAPI {
    @GET("/hiring.json")
    suspend fun listItems(): List<DataItem>
}
