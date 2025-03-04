package com.example.findmovie.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findmovie.R
import com.example.findmovie.databinding.GenreFilterItemBinding
import com.example.findmovie.presentation.model.DetailsModel

class GenreAdapter(private val onClick: ((DetailsModel) -> Unit)) : ListAdapter<DetailsModel, GenreAdapter.GenresViewHolder>(GenresDiffUtil()) {

    inner class GenresViewHolder(private val binding: GenreFilterItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detailsModel: DetailsModel) {
            binding.genreId.text = detailsModel.value
            binding.root.isSelected = detailsModel.isSelected
            binding.root.setOnClickListener { onClick(detailsModel) }

            if (detailsModel.isSelected) {
                binding.genreId.background = ContextCompat.getDrawable(binding.root.context, R.drawable.selected_genre)
            } else {
                binding.genreId.background = ContextCompat.getDrawable(binding.root.context, R.drawable.not_selected_genre)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
        val binding = GenreFilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenresViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class GenresDiffUtil : DiffUtil.ItemCallback<DetailsModel>() {
    override fun areItemsTheSame(oldItem: DetailsModel, newItem: DetailsModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DetailsModel, newItem: DetailsModel): Boolean {
        return oldItem == newItem
    }
}
