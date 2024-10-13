package com.example.fetchexercise.api

import javax.inject.Inject

class FetchRepositoryImpl @Inject constructor(
    private val fetchAPI: FetchAPI
) : FetchRepository {
    override suspend fun listItems(): List<DataItem> {
        return try {
            val items = fetchAPI.listItems()

            items
        } catch (e: Exception) {
            emptyList()
        }
    }
}