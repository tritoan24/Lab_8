package com.ph32395.lab_8

data class FilmApiResponse(
    val filmName: String,
    val duration: String,
    val releaseDate: String,
    val genre: String,
    val national: String,
    val description: String,
    val image: String,
    val filmId: String
)
