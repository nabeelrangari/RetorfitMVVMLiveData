package com.nabeel.erostestapp.data.database.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FavouriteMovie")
data class FavouriteMovie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "rank")
    val rank: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "poster")
    val poster: String
)