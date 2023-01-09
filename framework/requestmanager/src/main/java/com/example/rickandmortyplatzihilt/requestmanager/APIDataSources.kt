package com.example.rickandmortyplatzihilt.requestmanager

import com.example.rickandmortyplatzihilt.data.datasources.RemoteCharacterDataSource
import com.example.rickandmortyplatzihilt.data.datasources.RemoteEpisodeDataSource
import com.example.rickandmortyplatzihilt.domain.Character
import com.example.rickandmortyplatzihilt.domain.Episode
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@ViewModelScoped
class CharacterRetrofitDataSource @Inject constructor(
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

class EpisodeRetrofitDataSource @Inject constructor(
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
