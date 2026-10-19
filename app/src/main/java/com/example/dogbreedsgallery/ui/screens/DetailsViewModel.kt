package com.example.dogbreedsgallery.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dogbreedsgallery.data.BreedRepository
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.di.AppDependencies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(private val breedId: Int) : ViewModel() {
    private val repository = AppDependencies.getRepository()
    private val _breed = MutableStateFlow<BreedEntity?>(null)
    val breed: StateFlow<BreedEntity?> = _breed

    init {
        viewModelScope.launch {
            repository.getBreedById(breedId).collect { _breed.value = it }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch { repository.toggleFavorite(breedId) }
    }
}