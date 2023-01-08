package com.example.rickandmortyplatzihilt.usescases

import com.example.rickandmortyplatzihilt.domain.Character
import com.example.rickandmortyplatzihilt.usescases.repositories.CharacterRepository
import io.reactivex.Single

class GetAllCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(currentPage: Int): Single<List<Character>> =
        characterRepository.getAllCharacters(currentPage)
}