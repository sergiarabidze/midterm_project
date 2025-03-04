package com.example.findmovie.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findmovie.R
import com.example.findmovie.databinding.GenreItemBinding
import com.example.findmovie.presentation.model.DetailType
import com.example.findmovie.presentation.model.DetailsModel

class DetailsRecycler : ListAdapter<DetailsModel, DetailsRecycler.DetailsViewHolder>(DetailsDiffUtil()) {

    inner class DetailsViewHolder(private val binding: GenreItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detailsModel: DetailsModel) {
            binding.genreId.text = detailsModel.value
            if (detailsModel.type == DetailType.QUALITY){
                binding.genreId.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.star, 0, 0, 0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val binding = GenreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DetailsDiffUtil : DiffUtil.ItemCallback<DetailsModel>() {
    override fun areItemsTheSame(oldItem: DetailsModel, newItem: DetailsModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DetailsModel, newItem: DetailsModel): Boolean {
        return oldItem == newItem
    }
}
