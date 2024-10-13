package com.example.fetchexercise.api

interface FetchRepository {
    suspend fun listItems(): List<DataItem>
}