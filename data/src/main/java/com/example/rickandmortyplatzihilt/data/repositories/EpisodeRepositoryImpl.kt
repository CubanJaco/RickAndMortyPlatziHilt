package com.example.rickandmortyplatzihilt.data.repositories

import com.example.rickandmortyplatzihilt.data.datasources.RemoteEpisodeDataSource
import com.example.rickandmortyplatzihilt.domain.Episode
import com.example.rickandmortyplatzihilt.usescases.repositories.EpisodeRepository
import io.reactivex.Single

class EpisodeRepositoryImpl(
    private val remoteEpisodeDataSource: RemoteEpisodeDataSource
): EpisodeRepository {

    //region Public Methods

    override fun getEpisodeFromCharacter(episodeUrlList: List<String>): Single<List<Episode>> =
        remoteEpisodeDataSource.getEpisodeFromCharacter(episodeUrlList)

    //endregion
}
