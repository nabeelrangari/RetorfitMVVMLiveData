package com.nabeel.erostestapp.data.network

import com.nabeel.erostestapp.model.MovieDetails
import com.nabeel.erostestapp.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    @GET("3/movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key") api_key: String, @Query("page") page: Int): Response<Movies>

    @GET("3/movie/{id}")
    suspend fun getMovieDetails(@Path("id") id: String, @Query("api_key") api_key: String): Response<MovieDetails>

    @GET("3/search/movie")
    suspend fun getSearchResult(
        @Query("api_key") api_key: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<Movies>


}