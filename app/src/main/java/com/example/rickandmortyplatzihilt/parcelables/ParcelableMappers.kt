package com.example.rickandmortyplatzihilt.parcelables

import com.example.rickandmortyplatzihilt.domain.Character
import com.example.rickandmortyplatzihilt.domain.Location
import com.example.rickandmortyplatzihilt.domain.Origin

fun Character.toCharacterParcelable() = CharacterParcelable(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginParcelable(),
    location.toLocationParcelable(),
    episodeList
)

fun Location.toLocationParcelable() = LocationParcelable(
    name,
    url
)

fun Origin.toOriginParcelable() = OriginParcelable(
    name,
    url
)

fun CharacterParcelable.toCharacterDomain() = Character(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginDomain(),
    location.toLocationDomain(),
    episodeList
)

fun LocationParcelable.toLocationDomain() = Location(
    name,
    url
)

fun OriginParcelable.toOriginDomain() = Origin(
    name,
    url
)
