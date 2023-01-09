package com.example.rickandmortyplatzihilt.databasemanager.di

import android.content.Context
import com.example.rickandmortyplatzihilt.databasemanager.CharacterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object DatabaseModuleProviders {

    @Provides
    fun provideRemoteCharacterDataSource(
        @ApplicationContext context: Context
    ): CharacterDatabase = CharacterDatabase.getDatabase(context)

}
