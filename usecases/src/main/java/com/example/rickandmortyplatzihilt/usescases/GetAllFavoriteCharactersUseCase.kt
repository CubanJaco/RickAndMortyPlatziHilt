package com.example.rickandmortyplatzihilt.usescases

import com.example.rickandmortyplatzihilt.domain.Character
import com.example.rickandmortyplatzihilt.usescases.repositories.CharacterRepository
import io.reactivex.Flowable

class GetAllFavoriteCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(): Flowable<List<Character>> = characterRepository.getAllFavoriteCharacters()
}