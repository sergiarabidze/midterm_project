package com.example.findmovie.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.findmovie.R
import com.example.findmovie.databinding.PopularMoviesItemBinding
import com.example.findmovie.presentation.model.MoviesModel
import com.example.findmovie.presentation.extension.loadImage

class MoviesPagerAdapter(private val onClickListener: (MoviesModel) -> Unit) : ListAdapter<MoviesModel,MoviesPagerAdapter.PopularMoviesViewHolder>(MoviesDiffUtil()){
    inner class PopularMoviesViewHolder(private val binding: PopularMoviesItemBinding): ViewHolder(binding.root){
        fun bind(moviesModel: MoviesModel){
            with(binding){
                titleId.text = moviesModel.title
                posterId.loadImage(moviesModel.poster)
                genreId.text = moviesModel.genres.drop(1).fold(moviesModel.genres.first()){acc, s -> "$acc,$s" }

                releaseId.text = root.context.getString(
                    R.string.releaseDateAndLanguage,
                    moviesModel.releaseDate,
                    moviesModel.language
                )
                durationId.text = moviesModel.duration
                ratingText.text = moviesModel.rating.toString()
                root.setOnClickListener {
                    onClickListener(moviesModel)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
       return PopularMoviesViewHolder(PopularMoviesItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}