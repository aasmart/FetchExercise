package com.example.fetchexercise.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.fetchexercise.viewmodels.MainViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fetchexercise.api.DataItem
import com.example.fetchexercise.compose.components.LoadingWheel

@Composable
fun FetchApp(
    viewModel: MainViewModel = hiltViewModel()
) {
    val itemsState by viewModel.data.collectAsState(emptyList())
    val state = viewModel.state
    LaunchedEffect(Unit) {
        viewModel.getListItems()
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            if(state.isLoadingItems) {
                item {
                    LoadingWheel {
                        Text(text = "Loading Items")
                    }
                }
            } else {
                val items = itemsState
                    .filter { !it.name.isNullOrEmpty() }
                    .sortedWith(compareBy({ it.listId }, { it.name }))

                items(items) { item ->
                    item.name?.let {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = item.listId.toString())
                            Text(text = item.name)
                            Text(text = item.id.toString())
                        }
                    }
                }
            }
        }
    }
}