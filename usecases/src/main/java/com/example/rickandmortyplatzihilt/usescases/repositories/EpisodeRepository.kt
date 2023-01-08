package com.example.rickandmortyplatzihilt.usescases.repositories

import com.example.rickandmortyplatzihilt.domain.Episode
import io.reactivex.Single

interface EpisodeRepository {
    fun getEpisodeFromCharacter(episodeUrlList: List<String>): Single<List<Episode>>
}
