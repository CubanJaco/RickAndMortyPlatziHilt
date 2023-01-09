package com.example.rickandmortyplatzihilt.databasemanager.di

import com.example.rickandmortyplatzihilt.data.datasources.LocalCharacterDataSource
import com.example.rickandmortyplatzihilt.databasemanager.CharacterRoomDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DatabaseModuleBinders {

    @Binds
    fun bindLocalCharacterDataSource(
        characterRoomDataSource: CharacterRoomDataSource
    ): LocalCharacterDataSource

}
