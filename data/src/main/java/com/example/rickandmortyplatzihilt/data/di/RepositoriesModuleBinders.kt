package com.example.rickandmortyplatzihilt.data.di

import com.example.rickandmortyplatzihilt.data.repositories.CharacterRepositoryImpl
import com.example.rickandmortyplatzihilt.usescases.repositories.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoriesModuleBinders {

    @Binds
    fun bindCharacterRepository(
        characterRepositoryImpl: CharacterRepositoryImpl
    ): CharacterRepository

}
