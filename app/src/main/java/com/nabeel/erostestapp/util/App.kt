package com.nabeel.erostestapp.util

import android.app.Application
import android.content.Context
import com.nabeel.erostestapp.repository.Repository

val repository: Repository by lazy {
    App.repository
}

class App : Application() {
    companion object {
        lateinit var appContext: Context
        lateinit var repository: Repository
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        repository = Repository(applicationContext)
    }
}