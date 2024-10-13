package com.example.fetchexercise.api

interface FetchRepository {
    /**
     * Queries the Fetch API to obtain a list of items
     * @return A list of DataItems
     */
    suspend fun listItems(): List<DataItem>
}