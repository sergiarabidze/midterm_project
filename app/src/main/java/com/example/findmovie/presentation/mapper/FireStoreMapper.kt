package com.example.findmovie.presentation.mapper

import com.example.findmovie.presentation.model.ActorModel
import com.example.findmovie.presentation.model.MoviesModel


fun MoviesModel.toFirestoreMap(): Map<String, Any> {
    return mapOf(
        "actorModels" to actorModels.map { it.toFirestoreMap() },
        "description" to description,
        "duration" to duration,
        "genres" to genres,
        "id" to id,
        "language" to language,
        "poster" to poster,
        "rating" to rating,
        "releaseDate" to releaseDate,
        "title" to title,
        "trailer" to trailer
    )
}
fun ActorModel.toFirestoreMap(): Map<String, Any> {
    return mapOf(
        "id" to id,
        "name" to this.fullName,
        "photo" to photo
    )
}