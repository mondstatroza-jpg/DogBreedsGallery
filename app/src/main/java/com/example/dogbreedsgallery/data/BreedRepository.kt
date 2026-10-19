package com.example.dogbreedsgallery.data

import com.example.dogbreedsgallery.data.local.BreedDao
import com.example.dogbreedsgallery.data.local.BreedEntity
import com.example.dogbreedsgallery.data.remote.BreedDto
import com.example.dogbreedsgallery.data.remote.DogApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class BreedRepository(
    private val api: DogApi,
    private val dao: BreedDao
) {
    val allBreeds: Flow<List<BreedEntity>> = dao.getAllBreeds()
    val favoriteBreeds: Flow<List<BreedEntity>> = dao.getFavoriteBreeds()
    fun getBreedById(id: Int): Flow<BreedEntity?> = dao.getBreedById(id)

    suspend fun refreshBreeds() {
        try {
            val remoteBreeds = api.getBreeds()
            if (remoteBreeds.isNotEmpty()) {
                dao.insertBreeds(remoteBreeds.map { it.toEntity() })
            } else {
                insertTestBreeds()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            insertTestBreeds()
        }
    }

    suspend fun toggleFavorite(breedId: Int) {
        val breed = dao.getBreedById(breedId).first()
        breed?.let { dao.updateBreed(it.copy(isFavorite = !it.isFavorite)) }
    }

    private suspend fun insertTestBreeds() {
        val existing = dao.getAllBreeds().first()
        if (existing.isEmpty()) {
            dao.insertBreeds(
                listOf(
                    BreedEntity(1, "Лабрадор-ретривер", "Дружелюбный, активный", "Канада", "10-12 лет", "Популярная семейная порода.", "https://cdn2.thedogapi.com/images/B1-3nq9VQ.jpg", false),
                    BreedEntity(2, "Немецкая овчарка", "Умный, верный", "Германия", "9-13 лет", "Служебная порода с высоким интеллектом.", "https://cdn2.thedogapi.com/images/SJyBfg5NQ.jpg", false),
                    BreedEntity(3, "Сибирский хаски", "Энергичный, независимый", "Россия", "12-14 лет", "Порода для работы в упряжке.", "https://cdn2.thedogapi.com/images/BJ1Jf35VX.jpg", false),
                    BreedEntity(4, "Золотистый ретривер", "Добрый, ласковый", "Шотландия", "10-12 лет", "Идеальная семейная собака.", "https://cdn2.thedogapi.com/images/HJ70zgqEQ.jpg", false),
                    BreedEntity(5, "Бульдог", "Спокойный, дружелюбный", "Англия", "8-10 лет", "Добрый и ласковый компаньон.", "https://cdn2.thedogapi.com/images/r1ifZl5E7.jpg", false),
                    BreedEntity(6, "Бигль", "Любопытный, весёлый", "Англия", "12-15 лет", "Активная гончая порода.", "https://cdn2.thedogapi.com/images/1-7cgoZSh.jpg", false)
                )
            )
        }
    }

    private fun BreedDto.toEntity(): BreedEntity {
        val imgUrl = referenceImageId?.let { "https://cdn2.thedogapi.com/images/$it.jpg" } ?: ""
        return BreedEntity(id, name, temperament, origin, lifeSpan, description, imgUrl, false)
    }
}