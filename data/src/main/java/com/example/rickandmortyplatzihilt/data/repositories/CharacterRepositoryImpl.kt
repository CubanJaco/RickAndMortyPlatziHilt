package com.example.rickandmortyplatzihilt.data.repositories

import com.example.rickandmortyplatzihilt.data.datasources.LocalCharacterDataSource
import com.example.rickandmortyplatzihilt.data.datasources.RemoteCharacterDataSource
import com.example.rickandmortyplatzihilt.domain.Character
import com.example.rickandmortyplatzihilt.usescases.repositories.CharacterRepository
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

@ViewModelScoped
class CharacterRepositoryImpl @Inject constructor(
    private val remoteCharacterDataSource: RemoteCharacterDataSource,
    private val localCharacterDataSource: LocalCharacterDataSource
) : CharacterRepository {

    //region Public Methods

    override fun getAllCharacters(page: Int): Single<List<Character>> =
        remoteCharacterDataSource.getAllCharacters(page)

    override fun getAllFavoriteCharacters(): Flowable<List<Character>> =
        localCharacterDataSource.getAllFavoriteCharacters()

    override fun getFavoriteCharacterStatus(characterId: Int): Maybe<Boolean> =
        localCharacterDataSource.getFavoriteCharacterStatus(characterId)

    override fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean> =
        localCharacterDataSource.updateFavoriteCharacterStatus(character)

    //endregion
}