package com.example.rickandmortyplatzihilt

import  android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class RickAndMortyPlatziApp: Application() {

    //region Override Methods & Callbacks

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    //endregion

}
