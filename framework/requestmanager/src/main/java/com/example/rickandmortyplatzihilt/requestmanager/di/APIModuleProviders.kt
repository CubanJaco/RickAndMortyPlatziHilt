package com.example.rickandmortyplatzihilt.requestmanager.di

import com.example.rickandmortyplatzihilt.requestmanager.APIConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object APIModuleProviders {

    @BaseUrlQualifier
    @Provides
    fun provideBaseUrl(): String = APIConstants.BASE_API_URL

}
