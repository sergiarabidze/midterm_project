package com.example.findmovie.data.remote.dtoClasses

import kotlinx.serialization.Serializable

@Serializable
data class MoviesDto(
    val actors: List<ActorDto>,
    val description: String,
    val duration: Int,
    val genres: List<String>,
    val id: String,
    val language: String,
    val poster: String,
    val rating: Double,
    val releaseDate: Long,
    val title: String,
    val trailer: String
)