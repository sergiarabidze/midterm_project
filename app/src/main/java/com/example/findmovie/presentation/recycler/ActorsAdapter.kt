package com.example.findmovie.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.findmovie.databinding.ActorItemBinding
import com.example.findmovie.presentation.extension.loadActorImage
import com.example.findmovie.presentation.model.ActorModel
import com.example.findmovie.presentation.extension.loadImage

class ActorsAdapter : ListAdapter<ActorModel, ActorsAdapter.ActorsViewHolder>(ActorsDiffUtil()){
    inner class ActorsViewHolder(private val binding: ActorItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(actorModel: ActorModel){
            with(binding){
                imageId.loadActorImage(actorModel.photo)
                fullNameId.text = actorModel.fullName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsViewHolder {
        return ActorsViewHolder(ActorItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ActorsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class ActorsDiffUtil : DiffUtil.ItemCallback<ActorModel>() {
    override fun areItemsTheSame(oldItem: ActorModel, newItem: ActorModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ActorModel, newItem: ActorModel): Boolean {
        return oldItem == newItem
    }

}
