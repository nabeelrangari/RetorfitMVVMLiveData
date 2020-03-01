package com.nabeel.erostestapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.akbartravel.erostest.view.base.BaseViewModel
import com.nabeel.erostestapp.data.database.Entity.FavouriteMovie
import com.nabeel.erostestapp.data.network.NetworkConnectionInterceptor
import com.nabeel.erostestapp.data.network.Resource
import com.nabeel.erostestapp.util.App.Companion.repository
import kotlinx.coroutines.Dispatchers

class MovieDetailsViewModel : BaseViewModel() {

    fun movieDetails(id: String) = liveData(Dispatchers.IO) {
        try {
            emit(Resource.loading())
            val response = repository.getMovieDetails(id)
            if (response.isSuccessful) {
                emit(Resource.success(response.body()))
            } else {
                emit(Resource.error(response.errorBody().toString()))
            }
        } catch (e: NetworkConnectionInterceptor.NoConnectivityException) {
            emit(Resource.error(e.localizedMessage))
        }
    }


}