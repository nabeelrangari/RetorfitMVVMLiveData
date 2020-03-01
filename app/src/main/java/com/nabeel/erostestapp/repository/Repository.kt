package com.nabeel.erostestapp.repository

import android.content.Context
import com.nabeel.erostestapp.data.database.AppDatabase
import com.nabeel.erostestapp.data.database.Entity.FavouriteMovie
import com.nabeel.erostestapp.data.network.NetworkService
import com.nabeel.erostestapp.data.network.Networking
import com.nabeel.erostestapp.util.AppConstant

class Repository constructor(context: Context) {
    private val api: NetworkService = Networking.getApiService(context, false)
    private val database: AppDatabase = AppDatabase.getDatabase(context)
    private var favouriteMovie = database.movieDao()

    suspend fun getTopRatedMovies(page: Int) = api.getTopRatedMovies(AppConstant.API_KEY, page)

    suspend fun getSearchResult(input: String, page: Int) =
        api.getSearchResult(AppConstant.API_KEY, input, page)

    suspend fun getMovieDetails(id: String) = api.getMovieDetails(id, AppConstant.API_KEY)

    fun addFavouriteMovie(input: FavouriteMovie) =
        favouriteMovie.addFavouriteMovieToDb(input)

    fun deleteFavouriteMovie(id: Int) = favouriteMovie.deleteFavouriteMovie(id)

    fun getAllFavouriteMovies() = favouriteMovie.getAllFavouriteMovieList()
}