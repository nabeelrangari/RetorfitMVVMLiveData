package com.nabeel.erostestapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nabeel.erostestapp.data.database.Dao.FavouriteMovieDao
import com.nabeel.erostestapp.data.database.Entity.FavouriteMovie

@Database(
    entities = [FavouriteMovie::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): FavouriteMovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, AppDatabase::class.java,
                        "AppDatabase_Main"
                    ).build()
                }
            }
            return INSTANCE as AppDatabase
        }
    }
}