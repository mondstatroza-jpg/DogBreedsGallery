package com.example.dogbreedsgallery.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.dogbreedsgallery.data.local.BreedEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    breedId: Int,
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel = viewModel(factory = DetailsViewModelFactory(breedId))
) {
    val breed by viewModel.breed.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(breed?.name ?: "Загрузка...") },
                navigationIcon = { IconButton(onClick = onBackClick) { Icon(Icons.Default.ArrowBack, "Назад") } },
                actions = {
                    breed?.let {
                        val isFav = it.isFavorite
                        val tint by animateColorAsState(if (isFav) Color.Red else Color.Gray)
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "Избранное", tint = tint)
                        }
                    }
                }
            )
        }
    ) { padding ->
        breed?.let { b ->
            Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
                AsyncImage(
                    model = b.imageUrl,
                    contentDescription = b.name,
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    contentScale = ContentScale.Crop
                )
                Column(Modifier.padding(16.dp)) {
                    InfoChip("Происхождение", b.origin ?: "Неизвестно")
                    Spacer(Modifier.height(12.dp))
                    InfoChip("Продолжительность жизни", b.lifeSpan)
                    Spacer(Modifier.height(20.dp))
                    Text("Темперамент", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(b.temperament.ifEmpty { "Не указан" }, style = MaterialTheme.typography.bodyLarge)
                    if (!b.description.isNullOrBlank()) {
                        Spacer(Modifier.height(20.dp))
                        Text("Описание", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text(b.description, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
}

@Composable
fun InfoChip(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)) {
            Text(value, modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
        }
    }
}

class DetailsViewModelFactory(private val breedId: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailsViewModel(breedId) as T
}