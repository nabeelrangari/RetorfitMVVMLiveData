package com.nabeel.erostestapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akbartravel.erostest.view.base.BaseViewModel
import com.nabeel.erostestapp.data.database.Entity.FavouriteMovie
import com.nabeel.erostestapp.data.network.NetworkConnectionInterceptor
import com.nabeel.erostestapp.data.network.Resource
import com.nabeel.erostestapp.util.App.Companion.repository
import kotlinx.coroutines.Dispatchers

class TopRatedViewModel : BaseViewModel() {

    fun getTopRatedMovies(page: Int) = liveData(Dispatchers.IO) {
        try {
            emit(Resource.loading())
            val response = repository.getTopRatedMovies(page)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                emit(Resource.error(response.errorBody().toString()))
            }
        } catch (e: NetworkConnectionInterceptor.NoConnectivityException) {
            emit(Resource.error(e.localizedMessage))
        }
    }

    fun getSearchResult(input: String, page: Int) = liveData(Dispatchers.IO) {
        try {
            emit(Resource.loading())
            val response = repository.getSearchResult(input, page)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                emit(Resource.error(response.errorBody().toString()))
            }
        } catch (e: NetworkConnectionInterceptor.NoConnectivityException) {
            emit(Resource.error(e.localizedMessage))
        }
    }

    fun addFavouriteMovie(input: FavouriteMovie) = liveData(Dispatchers.IO) {
        try {
            repository.addFavouriteMovie(input)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(e.localizedMessage)
        }
    }

    fun deleteFavouriteMovie(id: Int) = liveData(Dispatchers.IO) {
        try {
            repository.deleteFavouriteMovie(id)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(e.localizedMessage)
        }
    }

    fun getFavouriteMovies(): LiveData<List<FavouriteMovie>> = liveData(Dispatchers.IO) {
        val data = repository.getAllFavouriteMovies()
        emitSource(data)
    }
}