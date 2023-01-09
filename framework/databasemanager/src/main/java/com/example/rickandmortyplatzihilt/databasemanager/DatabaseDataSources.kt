package com.example.rickandmortyplatzihilt.databasemanager

import com.example.rickandmortyplatzihilt.data.datasources.LocalCharacterDataSource
import com.example.rickandmortyplatzihilt.domain.Character
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@ViewModelScoped
class CharacterRoomDataSource @Inject constructor(
    database: CharacterDatabase
): LocalCharacterDataSource {

    //region Fields

    private val characterDao by lazy { database.characterDao() }

    //endregion

    //region

    override fun getAllFavoriteCharacters(): Flowable<List<Character>> = characterDao
        .getAllFavoriteCharacters()
        .map(List<CharacterEntity>::toCharacterDomainList)
        .onErrorReturn { emptyList() }
        .subscribeOn(Schedulers.io())

    override fun getFavoriteCharacterStatus(characterId: Int): Maybe<Boolean> {
        return characterDao.getCharacterById(characterId)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                Maybe.just(!isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    override fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean> {
        val characterEntity = character.toCharacterEntity()
        return characterDao.getCharacterById(characterEntity.id)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                if(isEmpty){
                    characterDao.insertCharacter(characterEntity)
                }else{
                    characterDao.deleteCharacter(characterEntity)
                }
                Maybe.just(isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    //endregion

}
