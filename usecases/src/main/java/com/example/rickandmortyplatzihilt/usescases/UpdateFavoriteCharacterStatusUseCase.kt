package com.example.rickandmortyplatzihilt.usescases

import com.example.rickandmortyplatzihilt.domain.Character
import com.example.rickandmortyplatzihilt.usescases.repositories.CharacterRepository
import io.reactivex.Maybe

class UpdateFavoriteCharacterStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(character: Character): Maybe<Boolean> =
        characterRepository.updateFavoriteCharacterStatus(character)
}
