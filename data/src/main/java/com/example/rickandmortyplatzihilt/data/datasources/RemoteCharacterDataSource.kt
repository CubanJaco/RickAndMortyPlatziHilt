package com.example.rickandmortyplatzihilt.data.datasources

import com.example.rickandmortyplatzihilt.domain.Character
import io.reactivex.Single

interface RemoteCharacterDataSource {
    fun getAllCharacters(page: Int): Single<List<Character>>
}