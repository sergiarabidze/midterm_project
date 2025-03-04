package com.example.findmovie.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findmovie.databinding.MyListItemBinding
import com.example.findmovie.presentation.model.MoviesModel

class MoviesAdapter(private val onClick: ((MoviesModel) -> Unit)) : ListAdapter<MoviesModel, MoviesAdapter.MoviesViewHolder>(MoviesDiffUtil()) {

    inner class MoviesViewHolder(private val binding: MyListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(moviesModel: MoviesModel) {
            binding.titleId.text = moviesModel.title
            Glide.with(binding.root.context)
                .load(moviesModel.poster)
                .into(binding.posterId)

            binding.ratingBar.rating = moviesModel.rating.toFloat()
            binding.root.setOnClickListener {
                onClick.invoke(moviesModel)
            }
        }

    }


    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(MyListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

}

class MoviesDiffUtil : DiffUtil.ItemCallback<MoviesModel>() {


    override fun areItemsTheSame(oldItem: MoviesModel, newItem: MoviesModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MoviesModel, newItem: MoviesModel): Boolean {
        return oldItem == newItem
    }

}