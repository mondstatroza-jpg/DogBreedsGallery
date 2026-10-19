package com.example.dogbreedsgallery.di

import android.content.Context
import com.example.dogbreedsgallery.data.BreedRepository
import com.example.dogbreedsgallery.data.local.AppDatabase
import com.example.dogbreedsgallery.data.remote.DogApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object AppDependencies {
    private lateinit var repository: BreedRepository

    fun init(context: Context) {
        if (::repository.isInitialized) return
        val database = AppDatabase.getDatabase(context)
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thedogapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        repository = BreedRepository(retrofit.create(DogApi::class.java), database.breedDao())
    }

    fun getRepository(): BreedRepository = repository
}