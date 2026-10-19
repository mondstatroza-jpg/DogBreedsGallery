package com.example.dogbreedsgallery.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET

@JsonClass(generateAdapter = true)
data class BreedDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "temperament") val temperament: String,
    @Json(name = "origin") val origin: String?,
    @Json(name = "life_span") val lifeSpan: String,
    @Json(name = "description") val description: String?,
    @Json(name = "reference_image_id") val referenceImageId: String?
)

interface DogApi {
    @GET("breeds")
    suspend fun getBreeds(): List<BreedDto>
}