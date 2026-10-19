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

data class FavoritesUiState(val favorites: List<BreedEntity> = emptyList(), val isEmpty: Boolean = true)

class FavoritesViewModel : ViewModel() {
    private val repository = AppDependencies.getRepository()
    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state

    init {
        viewModelScope.launch {
            repository.favoriteBreeds.collect { breeds ->
                _state.update { it.copy(favorites = breeds, isEmpty = breeds.isEmpty()) }
            }
        }
    }
}