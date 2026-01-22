package com.example.movieapp.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.movieapp.data.local.FavoriteMovieEntity
import com.example.movieapp.databinding.ItemMovieBinding
import com.example.movieapp.utils.Constants

class FavoritesAdapter(
    private val onLongClick: (Int) -> Unit
) : ListAdapter<FavoriteMovieEntity, FavoritesAdapter.FavoriteViewHolder>(DiffCallback) {

    inner class FavoriteViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: FavoriteMovieEntity) {
            binding.textTitle.text = movie.title
            binding.imagePoster.load(
                Constants.IMAGE_BASE_URL + movie.posterPath
            )

            // 🗑 delete on long click
            binding.root.setOnLongClickListener {
                onLongClick(movie.id)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FavoriteMovieEntity>() {
            override fun areItemsTheSame(
                oldItem: FavoriteMovieEntity,
                newItem: FavoriteMovieEntity
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: FavoriteMovieEntity,
                newItem: FavoriteMovieEntity
            ) = oldItem == newItem
        }
    }
}

