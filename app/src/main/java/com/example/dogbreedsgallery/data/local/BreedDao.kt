package com.example.dogbreedsgallery.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {
    @Query("SELECT * FROM breeds ORDER BY name ASC")
    fun getAllBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteBreeds(): Flow<List<BreedEntity>>

    @Query("SELECT * FROM breeds WHERE id = :breedId")
    fun getBreedById(breedId: Int): Flow<BreedEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreeds(breeds: List<BreedEntity>)

    @Update
    suspend fun updateBreed(breed: BreedEntity)
}