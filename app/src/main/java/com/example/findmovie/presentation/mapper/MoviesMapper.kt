package com.example.findmovie.presentation.mapper

import com.example.findmovie.data.remote.dtoClasses.ActorDto
import com.example.findmovie.data.remote.dtoClasses.MoviesDto
import com.example.findmovie.presentation.extension.toDateFormat
import com.example.findmovie.presentation.model.ActorModel
import com.example.findmovie.presentation.model.MoviesModel


fun ActorDto.toActor() : ActorModel {
    return ActorModel(
        id = id,
        fullName = "$name $surname",
        photo = photo
    )
}

fun MoviesDto.mapToMovies() : MoviesModel {
    return MoviesModel(
        actorModels = actors.map { it.toActor() },
        description = description,
        duration = "${duration/60}h ${(if (duration%60!=0) duration%60 else "")}min",
        genres = genres,
        id = id,
        language = language,
        poster = poster,
        rating = rating,
        releaseDate = releaseDate.toDateFormat(),
        title = title,
        trailer = trailer
    )
}

fun Map<String, Any>.toMoviesModel(): MoviesModel {
    return MoviesModel(
        actorModels = (this["actorModels"] as? List<Map<String, Any>>)?.map { it.toActorModel() } ?: emptyList(),
        description = this["description"] as? String ?: "",
        duration = this["duration"] as? String ?: "",
        genres = this["genres"] as? List<String> ?: emptyList(),
        id = this["id"] as? String ?: "",
        language = this["language"] as? String ?: "",
        poster = this["poster"] as? String ?: "",
        rating = (this["rating"] as? Number)?.toDouble() ?: 0.0,
        releaseDate = this["releaseDate"] as? String ?: "",
        title = this["title"] as? String ?: "",
        trailer = this["trailer"] as? String ?: ""
    )
}

fun Map<String, Any>.toActorModel(): ActorModel {
    return ActorModel(
        id = this["id"] as? String ?: "",
        fullName = this["name"] as? String ?: "",
        photo = this["photo"] as? String ?: ""
    )
}
