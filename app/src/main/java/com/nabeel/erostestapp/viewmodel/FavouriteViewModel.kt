package com.nabeel.erostestapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.akbartravel.erostest.view.base.BaseViewModel
import com.nabeel.erostestapp.data.database.Entity.FavouriteMovie
import com.nabeel.erostestapp.util.App.Companion.repository
import kotlinx.coroutines.Dispatchers

class FavouriteViewModel : BaseViewModel() {

    fun getFavouriteMovies(): LiveData<List<FavouriteMovie>> = liveData (Dispatchers.IO){
        val data = repository.getAllFavouriteMovies()
        emitSource(data)
    }

    fun deleteFavouriteMovie(id: Int) = liveData(Dispatchers.IO) {
        try {
            repository.deleteFavouriteMovie(id)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(e.localizedMessage)
        }
    }

}