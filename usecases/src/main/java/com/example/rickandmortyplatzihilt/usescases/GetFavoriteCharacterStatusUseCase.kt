package com.example.rickandmortyplatzihilt.usescases

import com.example.rickandmortyplatzihilt.usescases.repositories.CharacterRepository
import io.reactivex.Maybe

class GetFavoriteCharacterStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(characterId: Int): Maybe<Boolean> =
        characterRepository.getFavoriteCharacterStatus(characterId)
}
