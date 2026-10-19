package com.example.dogbreedsgallery.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.BreedRepository
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.di.AppDependencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val breeds: List<BreedEntity> = emptyList(),
    val searchQuery: String = ""
)

class MainViewModel : ViewModel() {
    private val repository = AppDependencies.getRepository()
    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState> = _state

    init {
        viewModelScope.launch {
            repository.allBreeds.collect { breeds ->
                filterAndEmit(breeds, _state.value.searchQuery)
            }
        }
        loadData()
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
        viewModelScope.launch {
            repository.allBreeds.collect { breeds ->
                filterAndEmit(breeds, query)
            }
        }
    }

    private fun filterAndEmit(breeds: List<BreedEntity>, query: String) {
        val filtered = if (query.isBlank()) breeds else breeds.filter {
            it.name.contains(query, ignoreCase = true)
        }
        _state.update { it.copy(breeds = filtered, isLoading = false) }
    }

    fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            repository.refreshBreeds()
            _state.update { it.copy(isRefreshing = false) }
        }
    }
}