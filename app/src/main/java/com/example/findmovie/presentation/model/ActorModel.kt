package com.example.findmovie.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ActorModel(
    val id: String,
    val fullName: String,
    val photo: String,
) : Parcelable