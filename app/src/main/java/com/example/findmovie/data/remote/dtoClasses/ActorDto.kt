package com.example.findmovie.data.remote.dtoClasses

import kotlinx.serialization.Serializable

@Serializable
data class ActorDto(
    val id: String,
    val name: String,
    val photo: String,
    val surname: String
)