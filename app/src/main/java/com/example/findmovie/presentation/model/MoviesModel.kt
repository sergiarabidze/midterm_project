package com.example.findmovie.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MoviesModel(
    val actorModels: List<ActorModel>,
    val description: String,
    val duration: String,
    val genres: List<String>,
    val id: String,
    val language: String,
    val poster: String,
    val rating: Double,
    val releaseDate: String,
    val title: String,
    val trailer: String
) : Parcelable