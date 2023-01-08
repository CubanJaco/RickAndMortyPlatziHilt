package com.example.rickandmortyplatzihilt.usescases

import com.example.rickandmortyplatzihilt.domain.Episode
import com.example.rickandmortyplatzihilt.usescases.repositories.EpisodeRepository
import io.reactivex.Single

class GetEpisodeFromCharacterUseCase(
    private val episodeRepository: EpisodeRepository
) {

    fun invoke(episodeUrlList: List<String>): Single<List<Episode>> =
        episodeRepository.getEpisodeFromCharacter(episodeUrlList)
}
