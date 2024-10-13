package com.example.fetchexercise.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fetchexercise.api.DataItem
import com.example.fetchexercise.api.FetchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: FetchRepository
) : ViewModel() {
    var state by mutableStateOf(MainState())
    private val dataChannel = Channel<List<DataItem>>()
    val data = dataChannel.receiveAsFlow()

    fun getListItems() = viewModelScope.launch {
        state = state.copy(isLoadingItems = true)
        val items = repository.listItems()
        dataChannel.send(items)
        state = state.copy(isLoadingItems = false)
    }
}