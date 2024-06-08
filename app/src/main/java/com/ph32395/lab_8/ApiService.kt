package com.ph32395.lab_8

import retrofit2.http.GET

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class MovieResponse(
    val filmName: String,
    val duration: String,
    val releaseDate: String,
    val genre: String,
    val national: String,
    val description: String,
    val image: String,
    val filmId: String
)

interface ApiService {
    @GET("Film")
    suspend fun getMovies(): List<MovieResponse>
}

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://6663aeb5932baf9032a8c635.mockapi.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}



