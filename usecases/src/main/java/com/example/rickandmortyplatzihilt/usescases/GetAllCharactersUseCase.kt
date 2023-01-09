package com.example.rickandmortyplatzihilt.usescases

import com.example.rickandmortyplatzihilt.domain.Character
import com.example.rickandmortyplatzihilt.usescases.repositories.CharacterRepository
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.Single
import javax.inject.Inject

@ViewModelScoped
class GetAllCharactersUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {

    fun invoke(currentPage: Int): Single<List<Character>> =
        characterRepository.getAllCharacters(currentPage)
}