package com.example.rickandmortyplatzihilt.requestmanager.di

import com.example.rickandmortyplatzihilt.data.datasources.RemoteCharacterDataSource
import com.example.rickandmortyplatzihilt.requestmanager.CharacterRetrofitDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface APIModuleBinders {

    @Binds
    fun bindRemoteCharacterDataSource(
        characterRetrofitDataSource: CharacterRetrofitDataSource
    ): RemoteCharacterDataSource

}
