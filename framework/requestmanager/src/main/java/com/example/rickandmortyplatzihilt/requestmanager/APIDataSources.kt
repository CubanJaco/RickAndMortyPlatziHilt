package com.example.rickandmortyplatzihilt.requestmanager

import com.example.rickandmortyplatzihilt.data.datasources.RemoteCharacterDataSource
import com.example.rickandmortyplatzihilt.data.datasources.RemoteEpisodeDataSource
import com.example.rickandmortyplatzihilt.domain.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CharacterRetrofitDataSource(
    private val characterRequest: CharacterRequest
) : RemoteCharacterDataSource {

    override fun getAllCharacters(page: Int): Single<List<Character>> {
        return characterRequest
            .getService<CharacterService>()
            .getAllCharacters(page)
            .map(CharacterResponseServer::toCharacterDomainList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}

class EpisodeRetrofitDataSource(
    private val episodeRequest: EpisodeRequest
) : RemoteEpisodeDataSource {

    override fun getEpisodeFromCharacter(episodeUrlList: List<String>): Single<List<Episode>> {
        return Observable.fromIterable(episodeUrlList)
            .flatMap { episode: String ->
                episodeRequest.baseUrl = episode
                episodeRequest
                    .getService<EpisodeService>()
                    .getEpisode()
                    .map(EpisodeServer::toEpisodeDomain)
                    .toObservable()
            }
            .toList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}
