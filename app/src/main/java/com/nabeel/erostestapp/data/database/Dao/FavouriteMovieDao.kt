package com.nabeel.erostestapp.data.database.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nabeel.erostestapp.data.database.Entity.FavouriteMovie

@Dao
interface FavouriteMovieDao {
    @Query("SELECT * from FavouriteMovie ORDER BY id DESC")
    fun getAllFavouriteMovieList(): LiveData<List<FavouriteMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavouriteMovieToDb(Req: FavouriteMovie)

    @Query("DELETE FROM FavouriteMovie WHERE rank = :rank")
    fun deleteFavouriteMovie(rank: Int)

    @Query("DELETE FROM FavouriteMovie")
    fun deleteAll()
}