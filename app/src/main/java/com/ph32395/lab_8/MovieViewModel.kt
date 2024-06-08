package com.ph32395.lab_8
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMovies()
                _movies.value = response.map {
                    Movie(
                        title = it.filmName,
                        year = "",
                        posterUrl = it.image,
                        duration = it.duration,
                        releaseDate = it.releaseDate,
                        genre = it.genre,
                        shortDescription = it.description
                    )
                }
            } catch (e: Exception) {
                // Handle the error
            }
        }
    }
}
