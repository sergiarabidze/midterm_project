package com.example.findmovie.presentation.helper

import com.example.findmovie.presentation.model.DetailsModel

 object Genres {
    val genres = listOf(
        DetailsModel(value = "All", isSelected = true),
        DetailsModel(value = "Action"),
        DetailsModel(value = "Sci-Fi"),
        DetailsModel(value = "Drama"),
        DetailsModel(value = "Adventure"),
        DetailsModel(value = "Animation"),
        DetailsModel(value = "Comedy"),
        DetailsModel(value = "Biography"),
        DetailsModel(value = "Romance")
    )
}