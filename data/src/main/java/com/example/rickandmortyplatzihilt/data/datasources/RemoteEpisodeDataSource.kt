package com.example.rickandmortyplatzihilt.data.datasources

import com.example.rickandmortyplatzihilt.domain.Episode
import io.reactivex.Single

interface RemoteEpisodeDataSource {
    fun getEpisodeFromCharacter(episodeUrlList: List<String>): Single<List<Episode>>
}
