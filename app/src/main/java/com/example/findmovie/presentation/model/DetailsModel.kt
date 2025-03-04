package com.example.findmovie.presentation.model

import java.util.UUID

data class DetailsModel(
    val id: String = UUID.randomUUID().toString(),
    val type: DetailType = DetailType.GENRE,
    val value: String,
    val isSelected: Boolean = false
)